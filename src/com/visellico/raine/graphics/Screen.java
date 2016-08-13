package com.visellico.raine.graphics;

import java.util.Random;

public class Screen {

	private int width, height;
	public int[] pixels;
	
	public int[] tiles = new int[150*64];
	
	private Random random = new Random();

	public Screen(int width, int height) {
		
		this.width = width; 
		this.height = height;
		pixels = new int[width*height];	//50400
		
		//random colors for our 64x64 array of tiles
		for (int i = 0; i < 64*64; i++) {
			int determiner = (i % 150) / 10;						
			switch (determiner) {
			case 0: tiles[i] = 0xff0000 + (random.nextInt(0x8080) /*<< 8*/); break;
			case 1: tiles[i] = 0xff0000 + (random.nextInt(0xff) << 8); break; /*tiles[i] = 0xff0000 + (random.nextInt(0x80) << 8); break;*/
			case 2: tiles[i] = 0xa00000 + (random.nextInt(0x00ff) << 8); break;
			case 3: tiles[i] = 0x000000 + (random.nextInt(0xa0ff) << 8); break; /*tiles[i] = 0xff0000 + (random.nextInt(0xff) << 8); break;*/
			case 4: tiles[i] = 0x000000 + (random.nextInt(0x00a0) << 8); break;
			case 5: tiles[i] = 0xffffff + (random.nextInt(0x8080) /*<< 8*/); break;
			case 6: tiles[i] = 0xffff + (random.nextInt(0xff) << 8); break;
			case 7: tiles[i] = 0x000000 + (random.nextInt(0x20ff)); break;
			case 8: tiles[i] = (random.nextInt(0xa0) << 16) + (random.nextInt(0xff)); break;
			
			//case 14: tiles[i] = (random.nextInt(0xa0) << 16) + (random.nextInt(0xff)); break;
			}
		}
		
//		//random colors for our 64x64 array of tiles
//		for (int i = 0; i < 64*64; i++) {
//									
//			tiles[i] = 0xff0000 + (random.nextInt(0x8080) /*<< 8*/);
//		}
		
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render() {
		
		for (int y = 0; y < height; y++) {
			if (y >= height || y < 0) break;
			for (int x = 0; x < width; x++) {
				if (x >= width || x < 0) break;
				
				//we're using bit shifting here to optimize, >>2 is the same as / 4
				int tileIndex = (x >> 2) + (y >> 4) * 150;	//tile resolution is 4x (resolution below is 1), 64 is the mapwidth so it's the sameas below
				pixels[x + y * width] = tiles[tileIndex];	//Colors every pixel pink. [x + (y*width)] y = 0; x = 1, 2, 3 ... width - 1, width ||| a jury rigged coordinate system
			}
		}
		
	}
	
}
