package com.visellico.raine.graphics.ui;

//YAY INTERFACES
//YAY LISTENERS
public class UIButtonListener {

	//class because predefined behaviour
	
	public void entered(UIButton button) {
		button.setColor(0xCDCDCD);
	}
	
	public void exited(UIButton button) {
		button.setColor(0xAAAAAA);
//		button.performAction();
	}
	
	public void pressed(UIButton button) {
		button.setColor(0xAAFFAA);
	}
	
	public void released(UIButton button) {
		button.setColor(0xCDCDCD);
		button.performAction();
	}
	
}
