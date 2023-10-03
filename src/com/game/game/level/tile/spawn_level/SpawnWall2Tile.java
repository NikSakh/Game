package com.game.game.level.tile.spawn_level;

import com.game.game.graphics.Screen;
import com.game.game.graphics.Sprite;
import com.game.game.level.tile.Tile;

public class SpawnWall2Tile extends Tile {

    public SpawnWall2Tile(Sprite sprite) {
        super(sprite);
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this);
    }

    public boolean solid() {
        return true;
    }
}
