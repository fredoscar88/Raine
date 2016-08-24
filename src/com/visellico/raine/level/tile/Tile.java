package com.visellico.raine.level.tile;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;

//vaguely abstract (this is a template- it won't be ran itself)
//this is written to be overwritten
//Also I guess we're creating all tiles here? (TODO)
public class Tile {

	public int x, y;
	public Sprite sprite;
	
	//(IMPORTANT) see GrassTile for general notes on a tile. Particularly, rendering. you bastard, Cherno >:V
	
	//GrassTile is a Tile, and has specific properties- particularly rendering. This is one grass tile, certainly, not sure why he did it like this, using static,
	//when surely just creating a new one each time? bleh idk
	//A general purpose grass tile, I guess :I but everytime we use this singular one, it's going to be carbon copied. To be sure, all grass are the same atm anyway, unless in tjhe
	//future we might want to load random sprites or something, or other shit like decorations
	public static Tile grass = new GrassTile(Sprite.grass);
	public static Tile voidTile = new VoidTile(Sprite.voidsprite);
	//Please note that if a parent class of something is returned, and a child of that parent is returned, 
	//then calling a method calls the child's overriding method not the one found in the parent (unless of course the child just inherits the parents method)
	//If the method exists in the child but not the parent, we can't call it (!) since we're looking at the PARENT not the CHILD. very strange but also makes sense to me, finally :I
	
	public Tile(Sprite sprite) {
		
		this.sprite = sprite;
				
	}
	
	//Tiles render themselves! They are called from elsewhere to render themselves! This method will give the command to render!
	public void render(int x, int y, Screen screen) {
	}
	
	//by default, no collision
	public boolean solid() {
		return false;
	}
	
}
