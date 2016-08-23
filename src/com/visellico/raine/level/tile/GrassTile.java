package com.visellico.raine.level.tile;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;

public class GrassTile extends Tile {

	public GrassTile(Sprite sprite) {
		super(sprite);
	}
	
	//overrides
	public void render(int x, int y, Screen screen) {
		//okay this sorta works
		//The 4 here is for simplicities sake. But really it represents the length of a side of a square tile- (int) Math.sqrt(sprite.SIZE)
		screen.renderTile(x << 4, y << 4, this);	//x, y are in pixel positions, so we need to change this eventually (TODO) (I think???) (YES YOU BASTARD CHERNO! I KNEW THIS WOULD BE A PROBLEM ep 36 ~5 min
										//So what's going on? Mistranslation from tile to pixel precision. x, and y here are received as tile precision. Not pixel level, so it ends up just drawing
										//the upper left most pixel >:V
										//The last tile gets rendered fine as it is in a zone where tiles should not be, out of the 64x resolution. oh man.
	}
	
	//nonsolid, which is our default in Tile
	
}
