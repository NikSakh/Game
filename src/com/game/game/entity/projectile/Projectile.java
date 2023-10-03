package com.game.game.entity.projectile;

import com.game.game.entity.Entity;
import com.game.game.graphics.Sprite;

import java.util.Random;

public abstract class Projectile extends Entity {

    protected final double xOrigin, yOrigin;
    protected double angle;
    protected Sprite sprite;
    protected double x, y;
    protected double nx, ny;
    protected double speed, range, damage;

    protected final Random random = new Random();

    public Projectile(double x, double y, double dir) {
        xOrigin = x;
        yOrigin = y;
        angle = dir;
        this.x=x;
        this.y=y;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getSpriteSize() {
        return sprite.SIZE;
    }

    protected void move() {

    }
}
