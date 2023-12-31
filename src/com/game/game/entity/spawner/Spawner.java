package com.game.game.entity.spawner;

import com.game.game.entity.Entity;
import com.game.game.level.Level;

public abstract class Spawner extends Entity {

    public enum Type {
        MOB, PARTICLE
    }

    protected Type type;


    public Spawner(int x, int y, Type type, int amount, Level level) {
        init(level);
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
