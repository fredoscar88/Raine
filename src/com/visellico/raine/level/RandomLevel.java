package com.visellico.raine.level;

import java.util.Random;

public class RandomLevel extends Level {

	//If this is nto static, then the little bugger goes and throws a null pointer exception as random dun exist.
	//I cant imagine why, frankly.
	private static final Random random = new Random();
	
	public RandomLevel(int width, int height) {
		
		super(width, height);
		
	}
	
	//will be run from the super constructor
	protected void generateRandomLevel() {
		
		//Used double for loop instead of a single in order to control specific tiles
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if (isBorderTile(x,y)) {
					tiles[x + y * width] = -1;
				}
				else {
					tiles[x + y * width] = random.nextInt(4);	//0,1,2,3
				}
			}
		}
	}
	
	public static final int BORDER_WIDTH = 3;
	
	private boolean isBorderTile(int x, int y) {
		
		if (y < BORDER_WIDTH || y >= height - BORDER_WIDTH || x < BORDER_WIDTH || x >= width - BORDER_WIDTH) {
			return true;
		}
		
		return false;
	}
	
}
