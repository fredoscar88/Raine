package com.visellico.raine.graphics;

public class Sprite {

	public final int SIZE;	//size of this sprite
	private int x,y;	//Coordinates of the sprite on the sprite sheet in gridsize
	private SpriteSheet sheet;	//sheet that this exists on
	
	public int[] pixels;	//may not need this to be public
	
	//------------------------------------------------------
	
	//"creating" a sprite- taking a specific region, loading it into file
	//How we load sprites from now on, really.
	public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite rock = new Sprite(16, 2, 0, SpriteSheet.tiles);
//	public static Sprite brick = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite voidsprite = new Sprite(16, 0xa0d0a0);	//0x000000 for readability, as opposed to 0 or 0x0
	
	//we used to render the player by rendering four of the parts of 'em
	//When using the coordinates of a sprite, the sprite grabber multiplies the coords we feed it by the size of the sprite to get the pixel location
//	public static Sprite player_forward = new Sprite(32, 0, 5, SpriteSheet.tiles);	//default sprite for player- but I could access playerSprites
	public static Sprite player_back = new Sprite(32, 2, 5, SpriteSheet.tiles);
//	public static Sprite player_side = new Sprite(32, 1, 5, SpriteSheet.tiles);	//formerly player_right
//
//	public static Sprite player_forward_1 = new Sprite(32, 0, 6, SpriteSheet.tiles);
//	public static Sprite player_forward_2 = new Sprite(32, 0, 7, SpriteSheet.tiles);
//	public static Sprite player_back_1 = new Sprite(32, 2, 6, SpriteSheet.tiles);
//	public static Sprite player_back_2 = new Sprite(32, 2, 7, SpriteSheet.tiles);
//	public static Sprite player_side_1 = new Sprite(32, 1, 6, SpriteSheet.tiles);
//	public static Sprite player_side_2 = new Sprite(32, 1, 7, SpriteSheet.tiles);
	
	public static Sprite[][] playerSprites = 	{
													{new Sprite(32, 0, 5, SpriteSheet.tiles), new Sprite(32, 0, 6, SpriteSheet.tiles), new Sprite(32, 0, 7, SpriteSheet.tiles)},	//forward
													{new Sprite(32, 1, 5, SpriteSheet.tiles), new Sprite(32, 1, 6, SpriteSheet.tiles), new Sprite(32, 1, 7, SpriteSheet.tiles)},	//side
													{new Sprite(32, 2, 5, SpriteSheet.tiles), new Sprite(32, 2, 6, SpriteSheet.tiles), new Sprite(32, 2, 7, SpriteSheet.tiles)},	//back
													{new Sprite(32, 1, 5, SpriteSheet.tiles), new Sprite(32, 1, 6, SpriteSheet.tiles), new Sprite(32, 1, 7, SpriteSheet.tiles)}		//other side
												};
	//	public static Sprite player_left = new Sprite(32, 3, 5, SpriteSheet.tiles);	//Going to create an invert sprite constructor in Sprite
//	public static Sprite player_left = new Sprite(player_right);
	
	
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
	
	//Okay this works for the record, but it's not how TheCherno did it- he didnt create another sprite to be stored in memory like this does, he inverted
	//	the sprite @render time which is what Im going to do, but I'll leave this method here. just in casies. Also proof for myself that I knew what to do. 
	//	Even if I had to work it out on pen&paper
	//Creates an a sprite inverted across the y axis (flips all x values)
	public Sprite(Sprite sprite) {
		this.SIZE = sprite.SIZE;
		
		this.pixels = new int[SIZE * SIZE];
		
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				this.pixels[x + y * SIZE] = sprite.pixels[((SIZE - 1) - x) + y * SIZE];	//((axisOfInversion - x) + axisOfInversion)
			}
		}
		
//		for (int i = 0; i < this.pixels.length; i++) {
//			if (i % SIZE < axisOfInversion) {
//				this.pixels[i] = sprite.pixels[(axisOfInversion - (i % SIZE)) + axisOfInversion];
//			}
//		}
		
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
