package com.visellico.raine.graphics.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

	/*
	 * PANELS and COMPONENTS
	 * 	Panels are groups that hold components. Drawn relative to the screen- but could be themselves a component, I guess, that didnt have a parent panel or something
	 * 	Components do things. Drawn relative to the panel.
	 */

	//ALL OF OUR GRAPHICS USING JAVAS GRAPHICS WILL NEED 3 SCALING, that is, if we aren't trying to take advantage of our hi res option (like text doesnt need it).
	//	Rectangles however, particularly screen filling ones, will do. and vector2i stuff.
	
	private List<UIPanel> panels = new ArrayList<UIPanel>();
	
	
	public UIManager() {

	}
	
	public void add(UIPanel p) {	//TheCherno called it addPanel();, I might change it to that...
		panels.add(p);
	}
	
	public void update() {
		for(UIPanel panel : panels) {
			panel.update();
		}
	}
	
	public void render(Graphics g) {
//		screen.drawRect(50, 50, 100, 100, 0xff00ff, false);
		for(UIPanel panel : panels) {
			panel.render(g);
		}
	}
	
}
