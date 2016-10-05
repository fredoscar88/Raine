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
	public static Sprite voidsprite = new Sprite(16, 0xA0D0A0);	//0x000000 for readability, as opposed to 0 or 0x0
	
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
	public static Sprite projectileArrow = new Sprite(16,1, 1, SpriteSheet.wizardProjectiles);
	
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
		this.SIZE = (w == h) ? w : -1;	//unused for non-square sprites, but generally used as a width
		this.width = w;
		this.height = h;
		pixels = new int[width * height];
		setColor(color);
		
	}
	
	//Uniformly colors the new sprite with one color
	public Sprite(int size, int color) {	//color should be delivered as a hex value
//		this.SIZE = size;
//		this.width = size;
//		this.height = size;
//		pixels = new int[SIZE * SIZE];
//		setColor(color);	//Colors the entire Sprite a uniform hexadecimal color
		this(size, size, color);
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
	
	//extract sprites. ugh this is a bad way to do this. SpriteSheet has a field that is an array of sprites.
	public static Sprite[] split(SpriteSheet sheet) {
		
		//Pixel area of the sheet divided by pixel area of one sprite
		int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.SPRITE_HEIGHT * sheet.SPRITE_WIDTH);	//uh, amount of sprites in the sheet.	//sheet.HEIGHT * sheet.width;
		Sprite[] sprites = new Sprite[amount];
//		int pixels[] = new int[sheet.SPRITE_HEIGHT * sheet.SPRITE_WIDTH];	//THIS SHOULD NOT BE OUT HERE. SO I moved it. But frankly I think it should work out here..
		int current = 0;
		
		for (int yp = 0; yp < sheet.getHeight() / sheet.SPRITE_HEIGHT; yp++) {
			for (int xp = 0; xp < sheet.getWidth() / sheet.SPRITE_WIDTH; xp++) {
				//Sprite coords in sprite precision. Inside this loop looks at one sprite.
																					//My mini novel
				int pixels[] = new int[sheet.SPRITE_HEIGHT * sheet.SPRITE_WIDTH];	//pixels of a single sprite. THIS HAS TO BE IN THIS LEVEL OF THE LOOP NEST
					//	why? it has to do with how java passes arrays. Otherwise the last cell of the font sheet is what every Sprite in pixels[] holds.
					//	So what exactly happens? Every sprite is passed the SAME pixels[] array- or rather, pointers to the same array. Only one space in the heap is allocated 
					//	to pixels[] when we instantiate it once, and every sprite, getting that reference, will be a different sprite but all use the same pixels- which is whatever happens
					//	to be the last pixels read into the array.
					//	This has to do with java's nature as PassByVal- but instead of passing a copy of the pixels[] array it passes a copy of the pointer to it. INB4 that is passbyref.
					//	TL;DR pixels needs to be instantiated inside this loop
					//	The alternative is to, in Sprite class, just copy the array over by declaring Sprite.pixels as a new array, and feeding it values. This way is easier. Or like System.arraycopy.
					//		Actually we might use an alternative since this is creating a lot of unnecessary objects, but I guess it doesnt matter since they are almost immediately dereferenced and the GC should gettem
				for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
					for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
						//Pixel coords in a single sprite. also no, Im not going to use a getter method for SpriteSheet pixels. :V
						int xo = xp * sheet.SPRITE_WIDTH;	//x offset
						int yo = yp * sheet.SPRITE_HEIGHT;	//y offset
						pixels[x + y * sheet.SPRITE_WIDTH] = sheet.getPixels()[(xo + x) + (yo + y) * sheet.getWidth()];
					}
				}
				
//				sprites[xp + yp * sheet.getWidth()] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
				sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
			}
		}
		
		return sprites;
	}
	
	/* failure tbh. I tried writing a spritesheet splitter. didnt work. :c Doesn't help that sometimes a lot of this stuff isnt commented well..
	public static Sprite[] split(SpriteSheet sheet, int spriteW, int spriteH) {
		
		int height = sheet.HEIGHT;	//pixel precision
		int width = sheet.WIDTH;
		
		int w = (height * spriteH);	//pixel precision
		int h = (width * spriteW);	//pixel precision
		
		int sheetHeight = height / spriteH;
		int sheetWidth = width / spriteW;
		
		Sprite[] result = new Sprite[sheetWidth * sheetHeight];
		
		int frame = 0;
		for (int y = 0; y < sheetHeight; y++) {
			for (int x = 0; x < sheetWidth; x++) {
				//one sprite
				int[] spritePixels = new int[spriteW*spriteH];
				
				for (int yy = 0; yy < spriteH; yy++) {
					for (int xx = 0; xx < spriteW; xx++) {
						//A pixel in the sprite
						spritePixels[x + y * spriteW] = sheet.pixels[(xx + x * spriteW) + (yy + y * spriteH) * width];
					}
				}
				result[frame++] = new Sprite(spritePixels, spriteW, spriteH);
			}
		}
		
		return result;
		
	}
*/
	
	public static Sprite rotate(Sprite sprite, double angle) {
		
		//can use private fields because we are in the class in which they're declared :I
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
		
	}
	//could pass in a sprite, but will act on any pixel array
	private static int[] rotate(int[] pixels, int width, int height, double angle) {
		int[] result = new int[width * height];	//NEW integer array!
		
		//again, negative angle here
		//uh.... (TODO) git understood
		double nx_x = rotationX(-angle, 1.0, 0.0);	//y is cancelled out
		double nx_y = rotationY(-angle, 1.0, 0.0);
		double ny_x = rotationX(-angle, 0.0, 1.0);	//y is cancelled out
		double ny_y = rotationY(-angle, 0.0, 1.0);
		
		double x0 = rotationX(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rotationY(-angle, -width / 2.0, -height / 2.0) + height / 2.0;
		
		for (int y = 0; y < height; y++) {
			double x1 = x0;
			double y1 = y0;	//copies of initial x, y vals, animating it a bit.
			for (int x = 0; x < width; x++) {
				int xx = (int) x1;
				int yy = (int) y1;
				int col = 0;
				if (xx < 0 || xx >= width || yy < 0 || yy >= height) col = 0xffff00ff;
				else col = pixels[xx + yy * width];
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;	//x0???
			y0 += ny_y;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param angle
	 * @param x
	 * @param y
	 * @return
	 */
	private static double rotationX(double angle, double x, double y) {
		double cos = Math.cos(angle - (Math.PI/2.0));
		double sin = Math.sin(angle - (Math.PI/2.0));
		//negative y, because we're flipping our unit circle :C
		return x * cos + y * -sin;
	}
	
	private static double rotationY(double angle, double x, double y) {
		double cos = Math.cos(angle - (Math.PI/2.0));
		double sin = Math.sin(angle - (Math.PI/2.0));
		//not sure whats going on
		return x * sin + y * cos;
	}
	
	//thanks youtube comments on TheCherno vid 114
	public static Sprite scale(int[] pixels, int w1, int h1, int w2, int h2) {

		int[] new_pixels = new int[w2 * h2];

		int xr = (int) ((w1 << 16) / w2) + 1;
		int yr = (int) ((h1 << 16) / h2) + 1;

		int x2, y2;

		for (int i = 0; i < h2; i++) {
			for (int j = 0; j < w2; j++) {
				x2 = ((j * xr) >> 16);
				y2 = ((i * yr) >> 16);
				new_pixels[(i * w2) + j] = pixels[(y2 * w1) + x2];
			}
		}
		// Create a new sprite from your new pixels, new width, and new height
		Sprite new_sprite = new Sprite(new_pixels, w2, h2);

		return new_sprite;
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
