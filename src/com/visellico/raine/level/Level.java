package com.visellico.raine.level;

import java.util.ArrayList;
import java.util.List;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.level.tile.Tile;

//there will be two "types" here random gen and data loaded levels
//vaguely abstract. might make abstract.
public class Level {
	//Everything that ALL levels inherit goes here- a template
	//See SpawnLevel for an idea on how individual levels might be treated

	//Color constants;
	protected final int GREEN = 0xFF00FF00;	//grass
	protected final int PALE_YELLOW = 0xFFDDFF38;	//flower
	protected final int GRAY = 0xFF877E79;	//rock
	//-----
	
	protected int width, height;	//will be used with random levels- custom made ones will already have a width/height
	protected int[] tilesInt;
	protected int[] tiles;	//all the level tiles. acknowledging that only one level can be loaded at a time. Stores the colors.
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	//---------------Levels-----
	public static Level spawn = new SpawnLevel("/levels/spawn.png");
	
	//--------------------------
	
	//random level constructor
	public Level (int width, int height) {
		this.width = width;
		this.height = height;
		//Holds data on what type of tile is here
		tilesInt = new int[width*height];
		generateLevel();
	}
	
	//Load a level
	public Level (String path) {
		loadLevel(path);
		generateLevel();
	}
	
	protected void generateLevel() {
		
	}
	
	protected void loadLevel(String path) {
		
	}
	
	//happens sixty updates a second
	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}
	
	//time of the level, manages time, like minecraft or something
	private void time() {
		
	}
	
	//xScrll and yScroll are the position of the player or somesuch, how much the screen is "scrolled". xScroll and yScroll are based on, I guess, the upper leftmost corner-
	//	or rather if that corner is 0,0 then x/y Scroll are the relative movements of ALL pixels but they also are the coords of the upper left pixel. hence why it works below when we do the
	//	bit-shift jazz. Kinda clever actually; the amount that EVERY pixel is offset by is also conveniently the coordinates of the upper leftmost pixel!
	//He names 'em xScroll, yScroll. Also all levels render the same, hence why we write this is in the parent class
	public void render(int xScroll, int yScroll, Screen screen) {
		
		//screen that we're drawing onto
		screen.setOffset(xScroll, yScroll);	//xScrp;; and yScroll are the position of the player, and the amount the map needs to "scroll" when the player moves- our offset
		
		//corner pins! Ways to tell the rendery bugger to stop. Because he's hit a corner. and also I guess where to start from. Only need two! Area of the level to RENDER!
		int x0 = xScroll >> 4;	//x0 is the left side of the screen- a vertical line (like x=0 in math)- where we start rendering x
								//divided by 16- to make it into tiles, as 16, 2^4, is the size of tiles :V, so pixels 0-15 belong to tile 0, as all of those >>4 == 0 (TODO) make sure to changeme if tiles change size
		int x1 = ((xScroll + screen.width) >> 4) + 1;	//TILE PRECISION >> 4 MASTERRACE
		int y0 = yScroll >> 4;	//four should be replaced by sqrt(tilesize) tbh
		int y1 = ((yScroll + screen.height) >> 4) + 1;	//x1 and y1 cornerpins are moved +1 tile out because when we try rendering the tiles in the screen, it gets cut off 
		
		//y and x run from, of course, y0 -> y1, x0 -> x1. We could have calculated them inside the for loop but, well, readability I guess.
		for (int y = y0; y < y1; y++) {	//the y1 and x1 render regions are bigger than the technical correct way.
			for (int x = x0; x < x1; x++) {
				//X and Y here cover all tiles that have parts visible on screen (including semi-hidden tiles)
				//Meaning they are in tile-level precision, not pixel. representing the position of the tile in the level.
				getTile(x, y).render(x, y, screen);	//GetTile accepts x and y and treats them as tile-level precision. <tile>.render however- also uses tile level precision?
				//each tile renders itself, since the tile knows what's diggity
				
				//For "drawn levels"
				
			}
			
		}
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
	}
	
	public void add(Entity e) {
		entities.add(e);
	}
		
	//We are now pulling color constants from Tile
	public Tile getTile(int x, int y) {	//x and y are at Tile-level position
		if (x < 0|| y < 0 || x >= width || y >= height ) return Tile.voidTile;	//If we go out of bounds in our map. VoidTile is what we render OOB
		switch (tiles[x + y * width])	{	//pulls the tile to render from this level's tile map. Get tile is ran for every tile in the level, me supposes, eventually
			case Tile.colSpawnGrass: return Tile.spawnGrass;	//A new grass tile tbh, but we've gone and just created a static version to be used wherever
			case Tile.colSpawnFloor: return Tile.spawnFloor;
			case Tile.colSpawnWall1: return Tile.spawnWall1;
			case Tile.colSpawnWall2: return Tile.spawnWall2;
			case Tile.colSpawnHedge: return Tile.spawnHedge;
			case Tile.colSpawnWater: return Tile.spawnWater;
			default: return Tile.voidTile;//return Tile.voidTile;
			//no need to break in the switch b/c return
		}
	}
	
}
