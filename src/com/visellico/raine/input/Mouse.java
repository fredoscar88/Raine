package com.visellico.raine.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

	//all static here because there's one mouse.
	
	//negative 1 because it doesnt exist, it's an init value
	private static int mouseX = -1;
	private static int mouseY = -1;
	private static int mouseB = -1;	//button
	
	public static int getX() {
		return mouseX;
	}
	
	public static int getY() {
		return mouseY;
	}
	
	public static int getButton() {
		return mouseB;
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();	//So......... if we move the mouse all is beuno, but clicking and moving is considered drag. What a drama queen.
		mouseY = e.getY();	//TL;DR we need set this method to do the same stuff as mouse moved since mouse moved won't be called when we are clicking and dragging.
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = -1;
	}

}
