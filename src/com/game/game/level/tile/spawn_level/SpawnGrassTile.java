package com.game.game.level.tile.spawn_level;

import com.game.game.graphics.Screen;
import com.game.game.graphics.Sprite;
import com.game.game.level.tile.Tile;

public class SpawnGrassTile extends Tile {

    public SpawnGrassTile (Sprite sprite){
        super(sprite);
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this);
    }
}
