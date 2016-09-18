package com.visellico.raine.graphics;

/**
 * An animated sprite constructed from a given spritesheet of any 'sprite dimensions'
 * @author Henry
 *
 */
public class AnimatedSprite extends Sprite {	//Should this EXTEND it? really?? Or implements?
												//TheCherno: I want any sprite to be able to be an animated sprite
	
	private int frame = 0;
	private Sprite sprite;	//Doesn't make sense??? This class extends sprite, so is this the default frame? why not return this? Why not an array of sprites?
	private int rate = 5;	//arbitrary default value
	private int length = -1;
	private int time = 0;
	
	/**
	 * Create a new animated sprite
	 * @param sheet 
	 * @param width
	 * @param height
	 * @param length Amount of frames in this sprite
	 */
	public AnimatedSprite(SpriteSheet sheet, int width, int height, int length) {
		
		super(sheet, width, height);
		this.length = length;
		
		sprite = sheet.getSprites()[0];
		if (length > sheet.getSprites().length) System.err.println("Error! Length of animation longer than sprite array");
		
	}
	
	public void update() {
		time++;
		if (time % rate == 0) {
			
			if (frame >= length - 1) {
				frame = 0;	//if we are trying to access a frame out side of how many we have,
			}
			else {
				frame++;
			}
			
		}
		
//		System.out.println(sprite + ", Frame: " + frame);
		sprite = sheet.getSprites()[frame];	//each update increments frame, accessing a different sprite indice
		
	}
	
	public Sprite getSprite() {
		return sprite;	//@specific frame?
	}
	
	//rate at which we control the thing, not necessarily FPS
	public void setFrameRate(int frames) {
		rate = frames;
	}

	public void setFrame(int index) {
		frame = index;
		if (frame > sheet.getSprites().length - 1) {
			System.err.println("Index out of bounds in " + this);
			return;
		}
		else sprite = sheet.getSprites()[frame];
		
	}

}
