package com.game.game.entity.projectile;

import com.game.game.entity.spawner.ParticleSpawner;
import com.game.game.graphics.Screen;
import com.game.game.graphics.Sprite;

public class WizardProjectile extends Projectile {

    public static final int FIRE_RATE = 10; //Higher is slower

    public WizardProjectile(double x, double y, double dir) {
        super(x, y, dir);
        range = 200;
        speed = 4;
        damage = 20;
        sprite = Sprite.rotate(Sprite.projectile_arrow, angle);
        nx = speed * Math.cos(angle);
        ny = speed * Math.sin(angle);
    }

    private int time = 0;

    public void update() {
        if(level.tileCollision((int) (x + nx), (int) (y + ny), 7, 4, 4)) {
            level.add(new ParticleSpawner((int) x, (int) y, 44, 50, level));
            remove();
        }
        time++;
        if(time % 6 == 0){
            sprite = Sprite.rotate(sprite, Math.PI / 20.0);
        }
        move();
    }

    protected void move() {
        x += nx;
        y += ny;
        if(distance()>range) remove();
    }

    private double distance() {
        double dist = 0;
        dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
        return dist;
    }

    public void render(Screen screen) {
        screen.renderProjectile((int) x - 12,(int) y - 2, this);
    }
}
