package com.visellico.raine.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//in charge of any sprite sheets and caching to memory, a variable
public class SpriteSheet {

	private String path;	//path to this spritesheet
	public final int SIZE;	//sprite sheet is square, so only one dimension is needed
	public int[] pixels;
	
	//start off path WITH a forward slash
	public static SpriteSheet tiles = new SpriteSheet("/textures/spritesheet.png",256);
	
	public SpriteSheet(String path, int size) {
		this.path = path;
		this.SIZE = size;	//does this work?? apparently- it can only be initialized once, doesnt have to be at compile time, note that this is in the constructor
		
		pixels = new int[SIZE*SIZE];
		load();
	}
	
	private void load() {
		
		try {
											/*
											 * In my interpretation, class.getResource() is for loading a resource into a memory cache
											 * okay, great lost my train of thought.
											 * Anyway I believe that when you specify a class infront of class, then it uses that class's root folder. maybe. idk.
											 * a commenter on the youtubes mentioned it being jar-safe, so the path won't be wildly varied as it doesnt stem from C: or something, I guess
											 * NOTE: a quick google has shown the getResource does indeed work with a path relative to the code base, NOT file paths.
											 * Specifically, Im guessing, relative to SpriteSheet- or well, the whole project, I dont know why SpriteSheet was specifically invoked
											 */
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));	//whew (TODO) get understood; ep 19 of TheCherno. ~9-10 min mark
			//Buffered Image loads the alpha channel, too
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w);	//(TODO) get understood. This puts the image pixels into the pixels array
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
}
