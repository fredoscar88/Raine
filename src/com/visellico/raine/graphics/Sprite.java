package com.visellico.raine.graphics;

public class Sprite {

	public final int SIZE;	//size of this sprite
	private int x,y;	//Coordinates of the sprite on the sprite sheet (not grid locations)
	private SpriteSheet sheet;	//sheet that this exists on
	
	public int[] pixels;	//may not need this to be public
	
	//------------------------------------------------------
	
	//"creating" a sprite- taking a specific region, loading it into file
	//How we load sprites from now on, really.
	public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite voidsprite = new Sprite(16, 0x00a0a0);	//0x000000 for readability, as opposed to 0 or 0x0
	public static Sprite levelBorder = new Sprite(16, 0xff0000);
	
	//Loads a sprite from the sprite sheet for the new sprite
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this.SIZE = size;
		pixels = new int[SIZE*SIZE];
		this.x = x * SIZE;	//the XY come as grid coordinates. The X and Y are not pixels, they are grid locations, and the locations are all 16x, so multiply
		this.y = y * SIZE;	//each by 16 to get what the their pixel value would be.
		this.sheet = sheet;
		load();
		
	}
	
	//Uniformly colors the new sprite with one color
	public Sprite(int size, int color) {	//color should be delivered as a hex value
		this.SIZE = size;
		pixels = new int[SIZE * SIZE];
		setColor(color);	//Colors the entire Sprite a uniform hexadecimal color
	}
	
	//This seems unnecessary, unless we want to change the color during runtime :S
	private void setColor(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	
	private void load() {
		
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				
				//"coordinate" stuff similar to what we find in Screen for drawing
				pixels [x+y*SIZE] = sheet.pixels[(x+this.x) + (y + this.y)*sheet.SIZE];
				
			}
		}
		
		
	}
	
}
