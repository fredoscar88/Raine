package com.visellico.raine.graphics;

import java.util.Random;

import com.visellico.raine.entity.mob.Player;
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
//		yp = yp << 4;	this is converting from pixel to tile precision, but again handling it @the tile classes b.c they will have diff sizes
		
		xp -= xOffset;	//Okay- this is minus because of the dolty map moving. Before this offset is applied, map movement corresponds with key movement, when map movement 
		yp -= yOffset;	//	really should be opposite the keys, as the keys are meant to be the PLAYER moving. Player walks left, map moves right, players spot on the screen stays same.
		//x, y; position of the pixel in the sprite.
		//xp, yp; offset of the pixel in the screen. (so position.)
		for (int y = 0; y < tile.sprite.SIZE; y++) {
			int ya = y + yp;	//ya- YAbsolute- w/yp being the offset. Y is the position of the pixel in the TILE, yp offsets it relative/absolute to the world- yp will eventually include the offset
								//this represents the ultimate position of the sprite in the context of the screen of pixels, as opposed to the pixels of the tile map.
			for (int x = 0; x < tile.sprite.SIZE; x++) {
				int xa = x + xp;	//xp- X Absolute w/xp being the offset. . . . . . . . .^^^
				
				//breaking off when a tile exits the screen also causes a bit of a border :I- the black border thing that counts as "off screen"- see like ep 36. fix'd in 38 but I made my own solution, maybe it will be the same
				//If we use CONTINUE instead of BREAK- you'll find that the left side renders fine, too
				//The reason the top side has rendered fine from the beginning is b/c "break" broke out of the X loop, not the y loop, it would just by logic not render
				//	any OOB y pixels but would break out of the x loop whenever a y pixel was illegal, cycling until it found a legal y pixel. Hence why top screen has always worked.
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;	//dont render tile (pixel really) if it is out of screen. also it will(read: could) crash if we dont include this :I (array index out of bounds). 
				pixels[xa+ya*width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];	//location on the screen is offset, but when pulling pixels from the sprite it isn't offset! hence no ya,xa
				
				
				//Well, as for the walltext below, TheCherno used my solution for right/bottom (expanding rendering zone w/corner pins), but my left screen shit is better. Continue and not Break. whew.
				//What he did was if xa < 0 then xa = 0
				/*
				 * There are two schools of thought to fixing the black border stuff. TheCherno have it be more lenient for it's pixel cut offs, in the if... continue statement, by
				 * putting the lower bound at the dimension of a tile (default 16) and increasing the upper bound by 16.
				 * Using the "tile" that was passed to the rendertile method to get that size. Might be a better solution than mine, which is A) make the if...continue not if..break
				 * and B) increasing the "upperbound" borderpins by 1, so it renders a whole one additional tile to the right and down.
				 * However, from a logic standpoint, while it produces the same practical result, the theory is wrong because I shouldn't be controlling for render stuff in the level class.
				 * Plus it doesnt make sense- the machine is already trying to render a tile in the black space, but because atleast one pixel runs off screen it cancels rendering
				 * So my fix is really bully. Instead, make the screen rendering more lenient, so it doesn't cancel tiles that have some portion cut off.
				 */
			}
		}
	}
	
	
	/**
	 * Renders specifically the sprite(s) of the player. Really the same as the tile rendering method, with a little more variety, i.e the ability to flip the sprite.
	 * The player is also generally held to a constant position- at the center of the screen, where tiles are absolute- theiy x and y do not change, but the player's does.
	 * They move at the same rate as the as the map pans, because the offset is the exact same- because the level rendering uses the player's position as that offset.
	 * @param xp X position of the player in the tile map, pixel-wise
	 * @param yp Y position of the player in the tile map, pixel-wise
	 * @param sprite Sprite to render
	 * @param flipState 0 = no flipping, 1 = flip over y-axis (invert x); 2 = flip over x-axis (invert y); 3 = flip both.
	 */
	//For flipState, I used earlier "boolean invert" for if the x was inverted- but the thing is, it's not very versatile. There may come a day I want to flip all over the place.
	//	so yeah Ill stick with more states
	public void renderPlayer(int xp, int yp, Sprite sprite, int flipState) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.SIZE; y++) {	//we KNOW players are all the same size. But yet. But yet. who knows.
			int ya = y + yp;
			int yModified = y;
			if (flipState == 2 || flipState == 3) yModified = (sprite.SIZE - 1) - y;
			
			for (int x = 0; x < sprite.SIZE; x++) {
				int xa = x + xp;
				int xModified = x;
				if (flipState == 1 || flipState == 3) xModified = (sprite.SIZE - 1) - x;
				/*-sprite.Size (instead of 0) is what TheCherno uses instead of xa < 0, it's xa < -16- but this is leftover from render tile, when the screen would render shit out of screen*/
				
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				
				int col = sprite.pixels[xModified + yModified * sprite.SIZE];
				if (col != 0xffff00ff) pixels[xa+ya*width] = col;	//If the color is pink (we also count the alpha channel, the first two hex digits), then just don't... draw the pixel
					
			}
		}		
	}
	
	//Offset is also = to player position
	//Offset created by the player after movement; manipulates offset present in renderTile()
	//This is called from the level rendering, as that is where player position is updated.
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
}
