package com.game.game;

import com.game.game.entity.mob.Player;
import com.game.game.events.Event;
import com.game.game.events.EventListener;
import com.game.game.graphics.Screen;
import com.game.game.graphics.layers.Layer;
import com.game.game.graphics.ui.UIManager;
import com.game.game.input.Keyboard;
import com.game.game.input.Mouse;
import com.game.game.level.Level;
import com.game.game.level.SpawnLevel;
import com.game.game.level.TileCoordinate;
import com.game.game.net.Client;
import com.game.game.net.player.NetPlayer;
import com.game.serialization.RCDatabase;
import com.game.serialization.RCField;
import com.game.serialization.RCObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

;


public class Game extends Canvas implements Runnable, EventListener {
    private static final long serialVersionUID = 1L;

    private static int width = 300 - 80;
    private static int height = 168;
    private static int scale = 3;
    public static String title = "Rain";

    private Thread thread;
    private JFrame frame;
    private Keyboard key;
    private Level level;
    private Player player;
    private boolean running = false;

    private static UIManager uiManager;

    private Screen screen;
    private BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    private List<Layer> layerStack = new ArrayList<Layer>();

    public Game(){
        setSize();

        screen = new Screen(width, height);
        uiManager = new UIManager();
        frame = new JFrame();
        key = new Keyboard();

        Client client = new Client("localhost", 8192);
        if(!client.connect()){

        }

        RCDatabase db = RCDatabase.DeserializeFromFile("src/res/data/screen.bin");
        //client.send(db);

        level = new SpawnLevel("src\\res\\levels\\spawn.png");
        addLayer(level);
        TileCoordinate playerSpawn = new TileCoordinate(20,61);
        player = new Player("Cherno",playerSpawn.x(),playerSpawn.y(), key);
        level.add(player);
        level.addPlayer(new NetPlayer());
        addKeyListener(key);

        Mouse mouse = new Mouse(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        save();
    }

    private void setSize(){
        RCDatabase db = RCDatabase.DeserializeFromFile("src/res/data/screen.bin");
        if (db != null) {
            RCObject obj = db.findObject("Resolution");
            width = obj.findField("width").getInt();
            height = obj.findField("height").getInt();
            scale = obj.findField("scale").getInt();
        }

        Dimension size = new Dimension(width * scale + 80 * 3, height * scale);
        setPreferredSize(size);
    }

    private void save(){
        RCDatabase db = new RCDatabase("Screen");

        RCObject obj = new RCObject("Resolution");
        obj.addField(RCField.Integer("width",width));
        obj.addField(RCField.Integer("height", height));
        obj.addField(RCField.Integer("scale", scale));
        db.addObject(obj);

        db.serializeToFile("src/res/data/screen.bin");
    }

    public static int getWindowWidth() {
        return width * scale;
    }

    public static int getWindowHeight() {
        return height * scale;
    }

    public static UIManager getUiManager(){
        return uiManager;
    }

    public void addLayer(Layer layer) {
        layerStack.add(layer);
    }

    public synchronized void start(){
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop(){
        running = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0;
        int updates = 0;
        requestFocus();
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updates + " ups, " + frames + " fps");
                frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    public void onEvent(Event event){
        for(int i = layerStack.size() - 1; i >= 0; i--){
            layerStack.get(i).onEvent(event);
        }
    }

    public void update(){
        key.update();
        uiManager.update();

        // Update layers here
        for(int i = 0; i < layerStack.size(); i++){
            layerStack.get(i).update();
        }
    }

    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        int xScroll = player.getX() - screen.width/2;
        int yScroll = player.getY() - screen.height/2;
        level.setScroll(xScroll, yScroll);

        // Render layers here
        for(int i = 0; i < layerStack.size(); i++){
            layerStack.get(i).render(screen);
        }


        for (int i=0; i<pixels.length; i++){
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(new Color(0xff00ff));
        g.fillRect(0,0,getWidth(), getHeight());
        g.drawImage(image, 0, 0, width * scale, height * scale, null);
        uiManager.render(g);
        // g.fillRect(Mouse.getX() - 32, Mouse.getY() - 32, 64, 64);
        //if(Mouse.getButton() != -1) g.drawString("Button: " + Mouse.getButton(), 80, 80);
        g.dispose();
        bs.show();
    }

    public static void main(String args[]){
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle(title);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.start();
    }
}

