package com.game.game.level;

import com.game.game.entity.mob.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpawnLevel extends Level {

    public SpawnLevel(String path) {
        super(path);
    }

    protected void loadLevel (String path) {
        try{
            BufferedImage image = ImageIO.read(new File(path));
            int w = width = image.getWidth();
            int h = height = image.getHeight();
            tiles = new int[w * h];
            image.getRGB(0,0, w, h, tiles, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception! Could not load level file");
        }
        //add(new Chaser(21,55));
        //add(new Star(17, 35));
        add(new Shooter(20,52));
        add(new Shooter(20,55));
        add(new Dummy(18,53));
        for(int i = 0; i < 5; i++){
          //  add(new Dummy(21, 40));
        }
    }

    protected void generateLevel() {
    }
}

