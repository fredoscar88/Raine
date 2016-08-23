package com.visellico.raine.level.tile;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;

public class VoidTile extends Tile {
	
	public VoidTile(Sprite sprite) {
		super(sprite);
	}
	
	//overrides (May make this just be default in he super class (TODO)
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);	//x, y are in pixel positions, so we need to change this eventually (TODO)
	}
	
	//Overrides solid from Tile parent
	public boolean solid() {
		return true;
	}
	
}
