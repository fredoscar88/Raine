package com.visellico.raine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private boolean[] keys = new boolean[120];
	public boolean up, down, left, right;	//Did we press a key corresponding to one of these movements?
	public boolean mmUp, mmDown;
	
	public void update() {
		
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		mmUp = keys[KeyEvent.VK_R];
		mmDown = keys[KeyEvent.VK_F];
		
//		for (int i = 0; i < keys.length; i++) {
//			
//			if (keys[i] == true) {
//				System.out.println("KEY: " + i);
//			}
//				
//			
//		}
		
	}
	
	public void keyPressed(KeyEvent ke) {
		keys[ke.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent ke) {
		keys[ke.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent ke) {
		
	}

	
	
}
