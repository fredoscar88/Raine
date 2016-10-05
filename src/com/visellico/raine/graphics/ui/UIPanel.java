package com.visellico.raine.graphics.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.visellico.raine.util.Vector2i;

public class UIPanel extends UIComponent {
	
	private List<UIComponent> components = new ArrayList<UIComponent>();
	
	//private Color color;
	
	public UIPanel(Vector2i position) {
		super(position);
		this.position = position;
	}
	
	public UIPanel(Vector2i position, Vector2i size) {
		super(position);
		this.position = position;	//it may be that we'll want to automatically scale these vectors by 3. Or not, actually no, lets not because we might want precision
		this.size = size;
		color = new Color(0xAACACACA, true);	//surely we can make this a parameter, somewhere. Also this is a default param.
	}
	
	public void add(UIComponent c) {
		components.add(c);
		c.init(this);
		c.setOffset(position);	//Should only need to do this once, or whenever we update position. So I'm going to go ahead and add in a setPosition, that incorporates this, so I don't forget
	}
	
	public void setPosition(Vector2i position) {
		this.position = position;
		for (UIComponent component : components) {	//Hey now we're thinking like programmers
			component.setOffset(this.position);
		}
	}
	
	/*public UIPanel setColor(Color c) {
		color = c;
		return this;
	}*/
	
	public void update() {
		for(UIComponent component : components) {
//			component.setOffset(position);	//This only needs to happen once? Or, if more than once, only ever infrequently. Maybe not tho
			component.update();
		}
	}
	
	public void render(Graphics g) {
		g.setColor(color);	//right? right! confurmed by TerCherners
		g.fillRect(position.x, position.y, size.x, size.y);
		
		for(UIComponent component : components) {
			component.render(g);
		}
	}
	
}
