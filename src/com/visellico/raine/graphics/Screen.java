package com.visellico.raine.graphics;

import java.util.Random;

import com.visellico.raine.level.tile.Tile;

public class Screen {

	public int width, height;
	public int[] pixels;
	
	public int xOffset, yOffset;
	

	public Screen(int width, int height) {
		
		this.width = width; 
		this.height = height;
		pixels = new int[width*height];	//50400
		
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;	//should really be same as void color
		}
	}
	
	//offset based system of render, factor players position then offset. The alternative is managing tiles pos seperately, not practical (ep 28 ~2:20)
	//separate method for each type of rendering we have
	/**
	 * Renders a tile onto the screen, offset/relative to the player
	 * @param xp X Position of the tile (Tile level precision) (old- now offset?)
	 * @param yp Y Position of the tile (Tile level precision) (old- now offset?)
	 * @param tile Tile to be rendered
	 */
	public void renderTile(int xp, int yp, Tile tile) { 
		//Tile OR a Sprite could have been used- Tile here because what if water is animated in the future or what if we change the sprite
		
//		xp = xp << 4;	should handle this in the actual tile folder I reckon since size is going to be not-guaranteed @16
//		yp = yp << 4;
		
		xp -= xOffset;	//Okay- this is minus because fo the dolty moving the map. Before this offset is applied, map movement corresponds with key movement, when map movement 
		yp -= yOffset;	//	really should be opposite the keys, as the keys are meant to be the PLAYER moving. Player walks left, map moves right, players spot on the screen stays same.
		//x, y; position of the pixel in the sprite.
		//xp, yp; offset of the pixel in the screen. (so position.)
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			int ya = y + yp;	//ya- Y Absolute w/yp being the offset. Y is the position of the pixel in the TILE, yp offsets it relative/absolute to the world- yp will eventually include the offset
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				int xa = x + xp;	//xp- X Absolute w/xp being the offset. . . . . . . . .^^^
				
				//breaking off when a tile exits the screen also causes a bit of a border :I
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) break;	//dont render tile (pixel really) if it is out of screen. also it will(read: could) crash if we dont include this :I (array index out of bounds)
				pixels[xa+ya*width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];	//location on the screen is offset, but when pulling pixels from the sprite it isn't offset! hence no ya,xa
			}
		}
	}
	
	//Offset created by the player after movement; manipulates offset present in renderTile()
	//This is called from the level rendering, as that is where player position is updated.
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
}
