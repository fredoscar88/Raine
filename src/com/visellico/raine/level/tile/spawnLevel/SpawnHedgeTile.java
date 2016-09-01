package com.visellico.raine.level.tile.spawnLevel;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;
import com.visellico.raine.level.tile.Tile;

public class SpawnHedgeTile extends Tile {

	public SpawnHedgeTile(Sprite sprite) {
		super(sprite);
	}

	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);
	}
	
	public boolean solid() {
		return true;
	}
	
	//Might use this in future! might even put in Tile super class.
	public boolean breakable() {
		return true;
	}
}
