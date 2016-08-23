package com.visellico.raine.level;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.level.tile.Tile;

//there will be two "types" here random gen and data loaded levels
//vaguely abstract
public class Level {
	//Everything that ALL levels inherit goes here- a template

	protected int width, height;	//will be used with random levels- custom made ones will already have a width/height
	protected int[] tiles;
	
	//random level constructor
	public Level (int width, int height) {
		this.width = width;
		this.height = height;
		//Holds data on what type of tile is here
		tiles = new int[width*height];
		generateRandomLevel();
	}
	
	//Load a level
	public Level (String path) {
		loadLevel(path);
	}
	
	
	protected void generateRandomLevel() {
		
	}
	
	private void loadLevel(String path) {
		
	}
	
	//happens sixty updates a second
	public void update() {
		
	}
	
	//time of the level, manages time, like minecraft or something
	private void time() {
		
	}
	
	//xScrll and yScroll are the position of the player or somesuch, how much the screen is "scrolled". xScroll and yScroll are based on, I guess, the upper leftmost corner-
	//	or rather if that corner is 0,0 then x/y Scroll are the relative movements of ALL pixels but they also are the coords of the upper left pixel. hence why it works below when we do the
	//	bit-shift jazz. Kinda clever actually; the amount that EVERY pixel is offset by is also conveniently the coordinates of the upper leftmost pixel!
	//He names 'em xScroll, yScroll. Also all levels render the same, hence why we write this is in the parent class
	public void render(int xScroll, int yScroll, Screen screen) {
		
		//idk how TheCherno plans on fixing this, so Ill leave off on my solution.
//		if (xScroll < 0) {
//			xScroll = 0;	//NB the X and Y value of the player in Game are unaffected, so X can continue to get increasingly negative, it wont be set to 0 since that X represents
//		}					//	the player's position- the player should be able to walk up to the edge of the map- it just won't scroll to that point. When the player walks back it won't
//							// scroll again until it crosses the mid-line threshold, so when you hit the other direction it wont start scrolling that way immediately, have to allow for walk
//							// time.
//		else if (xScroll > width) {
//			xScroll = width;
//		}
		
		screen.setOffset(xScroll, yScroll);	//xScrp;; and yScroll are the position of the player, and the amount the map needs to "scroll" when the player moves- our offset
		
		//corner pins! Ways to tell the rendery bugger to stop. Because he's hit a corner. and also I guess where to start from. Only need two! Area of the level to RENDER!
		int x0 = xScroll >> 4;	//x0 is the left side of the screen- a vertical line (like x=0 in math)- where we start rendering x
								//divided by 16- to make it into tiles, as 16, 2^4, is the size of tiles :V, so pixels 0-15 belong to tile 0, as all of those >>4 == 0 (TODO) make sure to changeme if tiles change size
		int x1 = (xScroll + screen.width) >> 4;	//TILE PRECISION >> 4 MASTERRACE
		int y0 = yScroll >> 4;	//four should be replaced by sqrt(tilesize) tbh
		int y1 = (yScroll + screen.height) >> 4;
		
		//y and x run from, of course, y0 -> y1, x0 -> x1. We could have calculated them inside the for loop but, well, readability I guess.
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				//X and Y here cover all tiles that have parts visible on screen (including semi-hidden tiles)
				//Meaning they are in tile-level precision, not pixel.
				getTile(x, y).render(x, y, screen);	//GetTile accepts x and y and treats them as tile-level precision. <tile>.render however- also uses tile level precision?
				
			}
		}
	}
	
	public Tile getTile(int x, int y) {	//x and y are at Tile-level position
		switch (tiles[x + y * width])	{	//pulls the tile to render from this level's tile map. Get tile is ran for every tile in the level, me supposes, eventually
			case -1: return Tile.levelBorder;
			case 0: return Tile.grass;	//A new grass tile tbh, but we've gone and just created a static version to be used wherever
			default: return Tile.voidTile;
			//no need to break in the switch b/c return
		}
	}
	
}
