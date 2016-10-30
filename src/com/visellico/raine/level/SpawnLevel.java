package com.visellico.raine.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.visellico.raine.entity.mob.Dummy;
import com.visellico.raine.entity.mob.Player;
import com.visellico.raine.entity.mob.Shooter;

public class SpawnLevel extends Level {

	//one way to do this; slow but precise
	//protected Tile[] tiles;
	
	//SPAWN 19, 59
	
	public SpawnLevel(String path) {
		super(path);
//		loadLevel(path);	//load level is already called in the super constructor- calling it again ends up with a nullpointerexception on Tiles since we overwrite Level's work on 
							//loading and generating
		
		TileCoordinate playerSpawn = new TileCoordinate(22, 59);
//		add(new Player("Fredo 2", playerSpawn.x(), playerSpawn.y(), null));	//adjusting player spawn. Tile sizes here are 16, multiplied by a coordinate in tile level precision, added by half a tile in pixel precision
		
		
	}

	protected void loadLevel(String path) {
		try {
			BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));	//getResourceAsStream?
////		int w = image.getWidth();
////		int h = image.getHeight();
			//int w = width = image.getWidth();	what TheCherno is using. dunno why he has extra w/h variables
			//int h = height = image.getHeight();
			width = image.getWidth();
			height = image.getHeight();
			tiles = new int[width * height];	//must be instantiated before the next statement which puts stuff in here
			image.getRGB(0, 0, width, height, tiles, 0, width);	//stores the pixels into the array of levelPixels where we can read their colors for making the level.
		} catch (IOException e) {
			System.out.println("ERROR could not load level file");
			e.printStackTrace();
		}
		
		for (int i = 0; i < 1; i++) {
			add(new Shooter(16, 55));
		}
		
		/*for (int i = 0; i < 1; i++) {
			add(new Chaser(20,55));
		}
		
		for (int i = 0; i < 1; i++) {
			add(new Star(18,45));
		}
		*/
		for (int i = 0; i < 1; i++) {
			add(new Dummy(16,59));
		}
//		Dummy dummy = new Dummy(20,59);
//		dummy.init(level);
//		level.add(dummy);
	}

	//Grass = 0x00FF00 (BUT I MADE COLOR CONSTANTS SO IM NOT DOING THIS)
	//Flower = see Level class
	//Rock = see level class.
	//MIGHT NEED TO INCLUDE THE ALPHA CHANNEL @LEVEL CONSTANTS
	protected void generateLevel() {
	}
	
//	public void render(int xScroll, int yScroll, Screen screen) {
//		screen.setOffset(xScroll, yScroll);	
//		int x0 = xScroll >> 4;	
//		int x1 = ((xScroll + screen.width) >> 4) + 1;
//		int y0 = yScroll >> 4;
//		int y1 = ((yScroll + screen.height) >> 4) + 1;
//		for (int y = y0; y < y1; y++) {	
//			for (int x = x0; x < x1; x++) {
//				getTile(x, y).render(x, y, screen);
//			}
//		}
//	}
	
//	public Tile getTile(int x, int y) {
//		if (x < 0|| y < 0 || x >= width || y >= height ) return Tile.voidTile;	//If we go out of bounds in our map. VoidTile is what we render OOB
//		return tiles[x + y * width];
//		
//	}
}
