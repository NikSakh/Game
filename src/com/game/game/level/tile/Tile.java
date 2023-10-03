package com.game.game.level.tile;

import com.game.game.graphics.Screen;
import com.game.game.graphics.Sprite;
import com.game.game.level.tile.spawn_level.*;

public class Tile {

    public Sprite sprite;

    public static Tile grass = new GrassTile(Sprite.grass);
    public static Tile flower = new FlowerTile(Sprite.flower);
    public static Tile rock = new RockTile(Sprite.rock);
    public static Tile voidTile = new VoidTile(Sprite.voidSprite);

    public static Tile spawn_grass = new SpawnGrassTile(Sprite.spawn_grass);
    public static Tile spawn_hedge = new SpawnHedgeTile(Sprite.spawn_hedge);
    public static Tile spawn_water = new SpawnWaterTile(Sprite.spawn_water);
    public static Tile spawn_wall1 = new SpawnWall1Tile(Sprite.spawn_wall1);
    public static Tile spawn_wall2 = new SpawnWall2Tile(Sprite.spawn_wall2);
    public static Tile spawn_floor = new SpawnFloorTile(Sprite.spawn_floor);

    public final static int col_spawn_grass = 0xff00ff21;
    public final static int col_spawn_hedge = 0; //unused
    public final static int col_spawn_water = 0; //unused
    public final static int col_spawn_wall1 = 0xff808080;
    public final static int col_spawn_wall2 = 0xff404040;
    public final static int col_spawn_floor = 0xffffffff;

    public Tile(Sprite sprite) {
        this.sprite = sprite;
    }

    public void render(int x, int y, Screen screen) {
    }

    public boolean solid() {
        return false;
    }
}
