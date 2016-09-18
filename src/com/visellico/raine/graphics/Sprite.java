package com.visellico.raine.graphics;

public class Sprite {

	public final int SIZE;	//size of this sprite
	private int width, height;	//w,h of sprite when non-square (where SIZE would normally be both for square ones)
	private int x,y;	//Coordinates of the sprite on the sprite sheet in gridsize
	SpriteSheet sheet;	//sheet that this exists on
	
	public int[] pixels;	//may not need this to be public
	
	//------------------------------------------------------
//----------MISC SPRITES-----------------//	
	//"creating" a sprite- taking a specific region, loading it into file
	//How we load sprites from now on, really.
	public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite rock = new Sprite(16, 2, 0, SpriteSheet.tiles);
//	public static Sprite brick = new Sprite(16, 1, 0, SpriteSheet.tiles);
	public static Sprite voidsprite = new Sprite(16, 0xa0d0a0);	//0x000000 for readability, as opposed to 0 or 0x0
	
//----------SPAWN LEVEL SPRITES-----------------//
	
	public static Sprite spawn_grass = new Sprite(16, 0, 0, SpriteSheet.spawnLevelTiles);
	public static Sprite spawn_hedge = new Sprite(16, 1, 0, SpriteSheet.spawnLevelTiles);
	public static Sprite spawn_water = new Sprite(16, 2, 0, SpriteSheet.spawnLevelTiles);
	public static Sprite spawn_wall1 = new Sprite(16, 0, 1, SpriteSheet.spawnLevelTiles);
	public static Sprite spawn_wall2 = new Sprite(16, 0, 2, SpriteSheet.spawnLevelTiles);
	public static Sprite spawn_floor = new Sprite(16, 1, 1, SpriteSheet.spawnLevelTiles);
	
//----------PLAYER SPRITES-----------------//	
	//we used to render the player by rendering four of the parts of 'em
	//When using the coordinates of a sprite, the sprite grabber multiplies the coords we feed it by the size of the sprite to get the pixel location
//	public static Sprite player_forward = new Sprite(32, 0, 5, SpriteSheet.tiles);	//default sprite for player- but I could access playerSprites
	public static Sprite player_back = new Sprite(32, 2, 5, SpriteSheet.tiles);

	public static Sprite[][] playerSprites = 	{
													{new Sprite(32, 0, 5, SpriteSheet.tiles), new Sprite(32, 0, 6, SpriteSheet.tiles), new Sprite(32, 0, 7, SpriteSheet.tiles)},	//forward
													{new Sprite(32, 1, 5, SpriteSheet.tiles), new Sprite(32, 1, 6, SpriteSheet.tiles), new Sprite(32, 1, 7, SpriteSheet.tiles)},	//side
													{new Sprite(32, 2, 5, SpriteSheet.tiles), new Sprite(32, 2, 6, SpriteSheet.tiles), new Sprite(32, 2, 7, SpriteSheet.tiles)},	//back
													{new Sprite(32, 1, 5, SpriteSheet.tiles), new Sprite(32, 1, 6, SpriteSheet.tiles), new Sprite(32, 1, 7, SpriteSheet.tiles)}		//other side
												};
	//	public static Sprite player_left = new Sprite(32, 3, 5, SpriteSheet.tiles);	//Going to create an invert sprite constructor in Sprite
//	public static Sprite player_left = new Sprite(player_right);
	
//-----------WIZARD PROJECTILE SPRITES-------------//
	public static Sprite projectileWizard = new Sprite(16,0, 0, SpriteSheet.wizardProjectiles);
	
//-----------PARTICLES-----------------------------//
	public static Sprite particle_normal = new Sprite(3, 0xAAAADD);
	//public static Sprite particle_normal = new Sprite(3, 0xAA0000);
	
	protected Sprite(SpriteSheet sheet, int width, int height) {
		
		this.SIZE = (width == height) ? width : -1;	//ternary operations! If square, SIZE is our new dimension. Else size is set to "unused"
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}
	
	//Loads a sprite from the sprite sheet for the new sprite
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this.SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE*SIZE];
		this.x = x * SIZE;	//the XY come as grid coordinates. The X and Y are not pixels, they are grid locations, and the locations are all 16x, so multiply
		this.y = y * SIZE;	//each by 16 to get what the their pixel value would be.
		this.sheet = sheet;
		load();
		
	}
	
	//loads a non-square sprite, of a specific color (like void sprite)
	public Sprite(int w, int h, int color) {
		this.SIZE = -1;	//unused for non-square sprites, but generally used as a width
		this.width = w;
		this.height = h;
		pixels = new int[width * height];
		setColor(color);
		
	}
	
	//Uniformly colors the new sprite with one color
	public Sprite(int size, int color) {	//color should be delivered as a hex value
		this.SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		setColor(color);	//Colors the entire Sprite a uniform hexadecimal color
	}
	
	//Okay this works for the record, but it's not how TheCherno did it- he didnt create another sprite to be stored in memory like this does, he inverted
	//	the sprite @render time which is what Im going to do, but I'll leave this method here. just in casies. Also proof for myself that I knew what to do. 
	//	Even if I had to work it out on pen&paper
	//Creates an a sprite inverted across the y axis (flips all x values)
//	public Sprite(Sprite sprite) {
//		this.SIZE = sprite.SIZE;
//		
//		this.pixels = new int[SIZE * SIZE];
//		
//		for (int y = 0; y < SIZE; y++) {
//			for (int x = 0; x < SIZE; x++) {
//				this.pixels[x + y * SIZE] = sprite.pixels[((SIZE - 1) - x) + y * SIZE];	//((axisOfInversion - x) + axisOfInversion)
//			}
//		}
//		
////		for (int i = 0; i < this.pixels.length; i++) {
////			if (i % SIZE < axisOfInversion) {
////				this.pixels[i] = sprite.pixels[(axisOfInversion - (i % SIZE)) + axisOfInversion];
////			}
////		}
//		
//	}
	
	public Sprite(int[] spritePixels, int width, int height) {
		this.SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		pixels = spritePixels;
//		for (int i = 0; i < spritePixels.length; i++) {
//			pixels[i] = spritePixels[i];
//		}
	}

	//This seems unnecessary, unless we want to change the color during runtime :S
	private void setColor(int color) {
		for (int i = 0; i < pixels.length; i++) {	//length = width * height or SIZE^2
			pixels[i] = color;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private void load() {
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				//"coordinate" stuff similar to what we find in Screen for drawing
				pixels [x+y*SIZE] = sheet.pixels[(x+this.x) + (y + this.y)*sheet.WIDTH];
				
			}
		}
		
		
	}
	
}
