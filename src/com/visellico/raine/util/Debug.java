package com.visellico.raine.util;

import com.visellico.raine.graphics.Font;
import com.visellico.raine.graphics.Screen;

public class Debug {
	
	//Static class- this class can never be instantiated
	private Debug() {
	}
	
	public static void drawRect(Screen screen, int x, int y, int width, int height, int col, boolean fixed) {
		screen.drawRect(x, y, width, height, col, fixed);
		
	}
	
	public static void drawRect(Screen screen, int x, int y, int width, int height, boolean fixed) {
		screen.drawRect(x, y, width, height, 0xff0000, fixed);
	}
	
	private static Font font;
	public static void drawText(String text, Screen screen) {
		drawText(10, 10, text, screen);
	}
	
	public static void drawText(int x, int y, String text, Screen screen) {
		
		drawText(x, y, 0x000, text, screen);
	}
	
	public static void drawText(int x, int y, int col, String text, Screen screen) {
		if (font == null) font = new Font();
		font.render(x, y, col, text, screen);
	}
	
}
