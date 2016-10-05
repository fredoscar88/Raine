package com.visellico.raine.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;

import org.w3c.dom.ranges.RangeException;

import com.visellico.raine.util.Vector2i;

public class UIProgressBar extends UIComponent {

	//So I am going to be modifiying thecherno's implementation... (TODO)
	private double progress;	//0.0 to 1.0
//	private double progressMax;	//default values are 100
//	private double progressFill;	//percentage value scaled to the size of the progress bar
	
	private Color backgroundColor = new Color(0x707070);
	
	public UIProgressBar(Vector2i position, Vector2i size) {
		this(position, size, 100);
	}
	
	
	public UIProgressBar(Vector2i position, Vector2i size, double progress) {
		super(position);
		this.size = size;
		
		this.progress = progress;
		
	}
	
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	
	public void setProgress(double progress) {
		if (progress < 0.0 || progress > 1.0) throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "Progress  must be between 0.0 and 1.0! (inclusive");
		
		this.progress = progress;
	}
	
	public double getProgress() {
		return progress;
	}

	public void update() {
//		if (progressMax != 0.0) progressFill = progress / progressMax;	// IGNORE ME yes, the multiplication must happen before the division, since we're using integer math here
		//(TODO) note bien; the cherno says progress stuff usually uses doubles. like between 0.0 and 1.0. which is cool I guess.
		//I was previously using the Henry Farr way of doing things, that is, the mathsy, not always-best-readable way, by multiplying progress by size and then dividing by progMax
		//	this is not THE BEST for precision but meh. I might switch back to my way! (TODO) TODO TODO TODO TODO because these progress bars are PROBABLY only going to deal in ints anyway
		//REASON WHY THIS IS BAD: (AND IM GOING TO KEEP THE MULTIPLE TODOS HERE..)
		//	UIProgressBar is for displaying progress. Dassit. We shouldnt be calculating stuff in here, that would be a non-UI class for progress bar, if you read me.
		//	The way we have this implemented now, UIProgressBar should just. be. displayin.
	}

	public void render(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(position.x + offset.x, position.y, size.x, size.y);
		
		g.setColor(color);
		g.fillRect(position.x + offset.x, position.y, (int) (progress * size.x), size.y);
	}
	
	
	
/*	MY IMPLEMENTATION OF UIPROGRESSBAR
	Vector2i size;
	
	public double valMax;
	public double valCurrent;
	public int valXFilled;
	
	private Color backgroundColor;
	
	public UIProgressBar(Vector2i position, Vector2i size) {
		super(position);
		this.size = size;
		
		backgroundColor = new Color(0x707070);
	}

	public void update() {
		valXFilled = (int) (valCurrent * size.x / valMax);
	}
	
	public void render(Graphics g) {
		
		g.setColor(backgroundColor);
		g.fillRect(position.x + offset.x, position.y + offset.y, size.x, size.y);
		g.setColor(color);
		g.fillRect(position.x + offset.x, position.y + offset.y, (int) (valCurrent * size.x/ valMax), size.y);
		
	}
	*/
}
