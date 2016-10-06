package com.visellico.raine.graphics;

import com.visellico.raine.entity.mob.Chaser;
import com.visellico.raine.entity.mob.Mob;
import com.visellico.raine.entity.mob.Star;
import com.visellico.raine.entity.projectile.Projectile;
import com.visellico.raine.level.tile.Tile;

public class Screen {

	public int width, height;
	public int[] pixels;
	
	public int xOffset, yOffset;
	public static final int ALPHA_COL = 0xffff00ff;
	public static final int ALPHA_COL2 = 0xff7F007F;
	
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
	
	public void renderSheet(int xp, int yp, SpriteSheet sheet, boolean fixed) {
		
		if (fixed) {	//When fixed, xp and yp represent x and y positions relative to a defined origin- the upper left corner of our map
			xp -= xOffset;
			yp -= yOffset;
		}
		//when not fixed, represent the relative position on screen.
		
		for (int y = 0; y < sheet.HEIGHT; y++) {
			int ya = y + yp;
			for (int x = 0; x < sheet.WIDTH; x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				pixels[xa + ya * width] = sheet.pixels[x + y * sheet.WIDTH];	//we can explain what's going on here now, so no need to comment- see renderTile
				//could render these w/o the pink. But I think we need to stick with simpler sprites for particles, large ones create a bit of jerkiness.
			}
		}
		
	}
	
	/**
	 * Draws a sprite to the screen, with the ability to fix it relative to the screen.
	 * @param xp X Location of sprite either ABSOLUTE (on map) or RELATIVE (on screen)
	 * @param yp Y Location of sprite either ABSOLUTE (on map) or RELATIVE (on screen)
	 * @param sprite Sprite to draw
	 * @param fixed FALSE when RELATIVE; TRUE when ABSOLUTE
	 */
	public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed) {
		
		if (fixed) {	//When fixed, xp and yp represent x and y positions relative to a defined origin- the upper left corner of our map
			xp -= xOffset;
			yp -= yOffset;
		}
		//when not fixed, represent the relative position on screen.
		
		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				if (col != ALPHA_COL && col!= ALPHA_COL2) pixels[xa + ya * width] = col;	//If the color is pink (we also count the alpha channel, the first two hex digits), then just don't... draw the pixel
//				pixels[xa + ya * width] = sprite.pixels[x + y * sprite.getWidth()];	//we can explain what's going on here now, so no need to comment- see renderTile
				//could render these w/o the pink. But I think we need to stick with simpler sprites for particles, large ones create a bit of jerkiness.
			}
		}
		
	}
	
	public void renderTextCharacter(int xp, int yp, Sprite sprite, int color, boolean fixed) {
		
		if (fixed) {	//When fixed, xp and yp represent x and y positions relative to a defined origin- the upper left corner of our map
			xp -= xOffset;
			yp -= yOffset;
		}
		//when not fixed, represent the relative position on screen.
		
		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yp;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				if (col != ALPHA_COL && col!= ALPHA_COL2) pixels[xa + ya * width] = color;	//If the color is pink (we also count the alpha channel, the first two hex digits), then just don't... draw the pixel
//				pixels[xa + ya * width] = sprite.pixels[x + y * sprite.getWidth()];	//we can explain what's going on here now, so no need to comment- see renderTile
				//could render these w/o the pink. But I think we need to stick with simpler sprites for particles, large ones create a bit of jerkiness.
			}
		}
		
	}

	//MY DRAW RECT maybe just as efficient, maybe not as efficient. Looks like not as efficient since I do twice as many goarounds
	/*public void drawRect(int xp, int yp, int width, int height, int color, boolean fixed) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		
		for (int y = 0; y < (2 * height); y++) {
			int ya = y / height;
			pixels[(xp + (ya * width)) + (yp + y - height * ya) * this.width] = color;
		}
		for (int x = 0; x < (2 * width); x++) {
			int xa = x / width;
			pixels[(xp + x - width * xa) + (yp + (xa * height)) * this.width] = color;
		}
		pixels[xp + width + (yp + height) * this.width] = color;
		
	}*/
	
	public void drawRect(int xp, int yp, int width, int height, int color, boolean fixed) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;
		}
		
		for (int x = xp; x < xp + width; x++) {
			if (x < 0 || x >= this.width || yp >= this.height) continue;
			if (yp > 0) pixels[x + yp * this.width] = color;
			if (yp + height >= this.height) continue;	//Also TODO get understood this rectangle stuff.
			if (yp + height > 0) pixels[x + (yp + height) * this.width] = color;
		}
		for (int y = yp; y <= yp + height; y++) {	//<= here to cover our last pixel. It is being rendered twice doe but w/e. I think that goes for everything.
			if (xp >= this.width || y < 0 || y >= this.height) continue;
			if (xp > 0) pixels[xp + y * this.width] = color;
			if (xp + width >= this.width) continue;
			if (xp + width > 0) pixels[(xp + width) + y * this.width] = color;	//dont need to wrap (xp + width) since math reasons
		}
		//this also would have covered the last pixel, albeit less efficiently
//		if (!(xp + width < 0 || xp + width >= this.width || yp + height < 0 || yp + height >= this.height)) pixels[xp + width + (yp + height) * this.width] = color;	//last pixel of the rect
		
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
//		System.out.println(tile);
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
//				System.out.println(pixels[xa + ya * width]);
				
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
	public void renderMob(int xp, int yp, Sprite sprite, int flipState) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.SIZE; y++) {	//we KNOW players are all the same size. But yet. But yet. who knows.
			int ya = y + yp;
			int yModified = y;
			if (flipState == 2 || flipState == 3) yModified = (sprite.SIZE - 1) - y;
			
			for (int x = 0; x < sprite.SIZE; x++) {
				int xa = x + xp;
				int xModified = x;
				if (flipState == 1 || flipState == 3) xModified = (sprite.SIZE - 1) - x;	//reverses the position of a pixel
				/*-sprite.Size (instead of 0) is what TheCherno uses instead of xa < 0, it's xa < -16- but this is leftover from render tile, when the screen would render shit out of screen*/
				
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				
				int col = sprite.pixels[xModified + yModified * sprite.SIZE];
				if (col != ALPHA_COL) pixels[xa+ya*width] = col;	//If the color is pink (we also count the alpha channel, the first two hex digits), then just don't... draw the pixel
					
			}
		}		
	}
	
	public void renderMob(int xp, int yp, Mob mob) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < mob.getSprite().SIZE; y++) {	//we KNOW players are all the same size. But yet. But yet. who knows.
			int ya = y + yp;
			int yModified = y;
			
			for (int x = 0; x < mob.getSprite().SIZE; x++) {
				int xa = x + xp;
				int xModified = x;
				
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				
				int col = mob.getSprite().pixels[xModified + yModified * mob.getSprite().SIZE];
				if (mob instanceof Chaser && col == 0xff472BBF) col = 0xffBA0015;
				if (mob instanceof Star && col == 0xff472BBF) col = 0xffE8E83A;
				if (col != ALPHA_COL) pixels[xa+ya*width] = col;	//If the color is pink (we also count the alpha channel, the first two hex digits), then just don't... draw the pixel
					
			}
		}		
	}
	
	public void renderProjectile(int xp, int yp, Projectile p) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < p.getSpriteSize(); y++) {
			int ya = y + yp;	
			for (int x = 0; x < p.getSpriteSize(); x++) {
				int xa = x + xp;	
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				
				int col = p.getSprite().pixels[x + y * p.getSpriteSize()];
				if (col != ALPHA_COL) pixels[xa+ya*width] = col;
//				 = p.sprite.pixels[x + y * p.sprite.SIZE];
				
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




	/*public void renderMovementPix(int x, int y, int xxa, int yya, Player player) {
		int xt, yt;
		
		pixels[x - xOffset + (y - yOffset) * width] = 0xff00ff00;
		//System.out.println((x - xOffset) + " " + ( - yOffset));
		
		for (int c = 0; c < 4; c++) {
			xt = xxa + (c % 2) * player.xtmp1 + player.xtmp2;
			yt = yya + (c / 2) * player.ytmp1 + player.ytmp2;
			xt -= player.xtmp1 + 2;
			//yt -= player.ytmp1;
			
			try {
				pixels[(xt - xOffset) + ((yt - yOffset) * width)] = 0xff000000;
			} catch (Exception e) {
				
			}
			
			//System.out.println((xt - xOffset) + " " + (yt - yOffset));
		}
		
		
	}*/
	
}
