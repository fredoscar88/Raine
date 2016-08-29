package com.visellico.raine.level.tile;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;

public class FlowerTile extends Tile {

	public FlowerTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);
	}
	
}
