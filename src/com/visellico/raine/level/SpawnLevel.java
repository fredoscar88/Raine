package com.visellico.raine.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.level.tile.Tile;

public class SpawnLevel extends Level {

	//one way to do this; slow but precise
	protected Tile[] tiles;
	private int[] levelPixels;
	
	public SpawnLevel(String path) {
		super(path);
		loadLevel(path);
		
	}

	protected void loadLevel(String path) {
		try {
			BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));
			//int w = image.getWidth();
			//int h = image.getHeight();
			width = image.getWidth();
			height = image.getHeight();
			tiles = new Tile[width * height];
			levelPixels = new int[width * height];
			image.getRGB(0, 0, width, height, levelPixels, 0, width);	//stores the pixels into the array of levelPixels where we can read their colors for making the level.
			generateLevel();
		} catch (IOException e) {
			System.out.println("ERROR could not load level file");
			e.printStackTrace();
		}
	}

	//Grass = 0x00FF00 (BUT I MADE COLOR CONSTANTS SO IM NOT DOING THIS)
	//Flower = see Level class
	//Rock = see level class.
	//MIGHT NEED TO INCLUDE THE ALPHA CHANNEL @LEVEL CONSTANTS
	protected void generateLevel() {
		
		for (int i = 0; i < levelPixels.length; i++) {
			//if (levelPixels[i] == 0xff00ff00) System.out.println("GRASS");
			System.out.println(levelPixels[i] + " Rock: " + GRAY);
			switch (levelPixels[i]) {
			case GREEN: tiles[i] = Tile.grass; break;
			case PALE_YELLOW: tiles[i] = Tile.flower; break;
			case GRAY: tiles[i] = Tile.rock; break;
				default: tiles[i] = Tile.grass;//Tile.voidTile;
			}
		}
	}
	
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);	
		int x0 = xScroll >> 4;	
		int x1 = ((xScroll + screen.width) >> 4) + 1;
		int y0 = yScroll >> 4;
		int y1 = ((yScroll + screen.height) >> 4) + 1;
		for (int y = y0; y < y1; y++) {	
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if (x < 0|| y < 0 || x >= width || y >= height ) return Tile.voidTile;	//If we go out of bounds in our map. VoidTile is what we render OOB
		return tiles[x + y * width];
		
	}
}
