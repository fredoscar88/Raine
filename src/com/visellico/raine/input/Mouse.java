package com.visellico.raine.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.visellico.raine.events.EventListener;
import com.visellico.raine.events.types.MouseMovedEvent;
import com.visellico.raine.events.types.MousePressedEvent;
import com.visellico.raine.events.types.MouseReleasedEvent;

public class Mouse implements MouseListener, MouseMotionListener {

	//all static here because there's one mouse.
	
	//negative 1 because it doesnt exist, it's an init value
	private static int mouseX = -1;
	private static int mouseY = -1;
	private static int mouseB = -1;	//button
	
	private EventListener eventListener;
	
	public Mouse(EventListener listener) {
		eventListener = listener;
	}
	
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
		
		MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(), true);
		eventListener.onEvent(event);
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(), false);
		eventListener.onEvent(event);
	}

	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
		
		MousePressedEvent event = new MousePressedEvent(mouseB, e.getX(), e.getY());
		eventListener.onEvent(event);
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = MouseEvent.NOBUTTON;
		
		MouseReleasedEvent event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
		eventListener.onEvent(event);
	}
	
	public void mouseClicked(MouseEvent e) {
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}

}
