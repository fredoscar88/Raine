package com.visellico.raine.graphics.ui;

import java.awt.Graphics;

import com.visellico.raine.graphics.Sprite;
import com.visellico.raine.util.Vector2i;

public class UISprite extends UIComponent {

	public Sprite sprite;
	// this class is useless :C
	//	could uise it to render sprites in hi-res I guess, or well, scaled down but as a g.drawimage thingy, ya know ya know? Like what we literally do for our engine
	
	public UISprite(Vector2i position, Sprite sprite) {
		super(position);
		this.sprite = sprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
//		screen.renderSprite(position.x + offset.x, position.y + offset.y, sprite, false);
	}
}
