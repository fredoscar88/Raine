package com.visellico.raine.graphics;

import java.util.Random;

public class Screen {

	private int width, height;
	public int[] pixels;
	
	//Map Size should be a power of 2 to work most effectively with the mask
	public final int TILE_SIZE = 4;	//The tiles end up being 2^TILE_SIZE, so 2^4 becomes sixteen. Why? We're using bit shifts and this is the easiest way.
	public final int MAP_SIZE = 64;
	public final int MAP_MASK = MAP_SIZE - 1;
	
	public int[] tiles = new int[MAP_SIZE*MAP_SIZE];
	
	private Random random = new Random();

	public Screen(int width, int height) {
		
		this.width = width; 
		this.height = height;
		pixels = new int[width*height];	//50400
		
		//random colors for our 64x64 array of tiles
		for (int i = 0; i < MAP_SIZE*MAP_SIZE; i++) {
			tiles[i] = (random.nextInt(0xffffff));
		}
		
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render(int xOffSet, int yOffSet) {
		
		for (int y = 0; y < height; y++) {
			int yy = y + yOffSet;	//y prime
//			if (yy >= height || yy < 0) break;	//prevents trying to render pixels outside of the map (Y axis)
						
			for (int x = 0; x < width; x++) {
				int xx = x + xOffSet;	//x prime
				
//				if (xx >= width || xx < 0) break;	//prevents trying to render pixels outside of the map (X axis)
				
				//we're using bit shifting here to optimize, >>4 is the same as / 16
//				
				//the two lines below find what TILE a pixel is in and then color the pixel to that tile's color, using the same "coordinate system", x + (y * x)
				//thats the main component anyway
				
				//the bitwise opertation, AND MAP_MASK (63), ensures that only the last six digits are counted. In short, once we get to 100 0000 (64), our actual number is 00 0000; a bit like modular division
				int tileIndex = ((xx >> TILE_SIZE) & MAP_MASK) + ((yy >> TILE_SIZE) & MAP_MASK) * MAP_SIZE;	//tile resolution is 16x (resolution below is 1), 64 is the mapwidth so it's the sameas below
															//Why, essentially, divide by 16? Because 16 is the scale of a tile compared to a pixel. pixels 0-15 belong on tile 0, so divide by 16 to get that 0
//				pixels[x + y * width] = tiles[tileIndex];	//Colors every pixel pink. [x + (y*width)] y = 0; x = 1, 2, 3 ... width - 1, width ||| a jury rigged coordinate system
				
				//& 15 for looping through just the 16 of the lovelies, pixels Size is 16
				pixels[x + y * width] = Sprite.grass.pixels[(xx & 15) + (yy & 15) * Sprite.grass.SIZE];
				
				
			}
		}
		
	}
	
}
