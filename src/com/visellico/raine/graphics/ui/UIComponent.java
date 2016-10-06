package com.visellico.raine.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;

import com.visellico.raine.util.Vector2i;

public class UIComponent {

	public Vector2i position, size;
	
	protected Vector2i offset;
	protected Color color;
	protected UIPanel panel;	//parent panel of this UI component
	
	boolean active;
	
	public UIComponent(Vector2i position) {
		this.position = position;
		offset = new Vector2i();
		color = new Color(0xFF00FF);
	}	
	
	public UIComponent(Vector2i position, Vector2i size) {
		this.position = position;
		this.size = size;
		offset = new Vector2i();
		color = new Color(0xFF00FF);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {
		
	}
	
	public void init(UIPanel p) {
		this.panel = p;	//sets a pointer to the panel that contains this component
	}
	
	/**
	 * Set color based on an RGB value
	 * Allows inline creating of new components with non-default colors
	 * @param rgb New RGB value the component should have in it's color
	 * @return an instance of UIComponent with the new color constructed from the supplied RGB value.
	 */
	public UIComponent setColor(int rgb) {
		color = new Color(rgb);
		return this;
	}
	
	/**
	 * Set color based on an RGB value
	 * Allows inline creating of new components with non-default colors
	 * @param c New color the component should have
	 * @return an instance of UIComponent with the new color constructed from the supplied RGB value.
	 */
	public UIComponent setColor(Color c) {
		color = c;//new Color(c.getRGB());
		return this;
	}
	
	public Vector2i getAbsolutePosition() {
		return new Vector2i(position).add(offset);
	}
	
	protected void setOffset(Vector2i offset) {
		this.offset = offset;
	}
	
	
}
