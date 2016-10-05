package com.visellico.raine.graphics.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.visellico.raine.input.Mouse;
import com.visellico.raine.util.Vector2i;

public class UIButton extends UIComponent {
	
	private UIButtonListener buttonListener;
	private UIActionListener actionListener;
	
	private boolean inside = false;
	private boolean pressed = false;
	private boolean ignorePressed = false;
	
	public UILabel label;
	private Image image;	//make this a component?
	
	public UIButton(Vector2i position, Vector2i size) {
		this(position,size, new UIActionListener() {
			public void perform() {
				//default blank, the assumption is that the button will overrider this during creation
			}
		}, "");

	}
	
	public UIButton(Vector2i position, Vector2i size, UIActionListener actionListener) {
		this(position,size, actionListener, "");

	}
	
	public UIButton(Vector2i position, Vector2i size, UIActionListener actionListener, String text) {
		super(position, size);
		this.actionListener = actionListener;
		Vector2i lp = new Vector2i(position);
		lp.x += 4;
		lp.y += 25;
		
		//TODO center label on button
		label = (UILabel) new UILabel(new Vector2i(lp), text);//.setColor(0);
		label.setColor(0x444444);
		label.active = false;

		init();

	}
	
	public UIButton(Vector2i position, BufferedImage image, UIActionListener actionListener) {
		super(position, new Vector2i(image.getWidth(), image.getHeight()));
		this.actionListener = actionListener;
		
		setImage(image);
		init();
		
	}
	
	
	
	private void init() {
		setColor(0xAAAAAA);
		
		buttonListener = new UIButtonListener();
	}
	//I think I prefer my setOffset method, if only because I like the notion of SOME components containing others in unique ways, but I guess this is fine.
	//	and in Button we still have access to the label after we add it, so that's cool I guess. yeah TheCherno's is probably bet.rar
	public void init(UIPanel p) {
		super.init(p);
		if (label != null) {
			panel.add(label);
		}

	}
		
	public void setButtonListener(UIButtonListener buttonListener) {
		this.buttonListener = buttonListener;	//we can override default behavior this way
	}
	
	public void setActionListener(UIActionListener actionListener) {
		this.actionListener = actionListener;	//we give it a new actionListener (and yeah, this is not possible if we implemented ActionListener because we would have
												//	had to create a whole new button just to get a new ActionListener!
	}
	//This should not be implemented through the UIActionListener interface! It's only to access perform without making actionListener public
	//	If it were to overrider perform() in UIActionListener, we'd need to implement it and refactor this to be called perform()
	public void performAction() {	
		actionListener.perform();
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void setText(String text) {
		if (text == "") {
			label.active = false;
		}
		else {
			label.setText(text);
//			label.setOffset(offset); see if we don't initialize the label upon button creation, we never get another chance to set it's offset
			label.active = true;
		}
	}

	
	public void update() {
		Rectangle rect = new Rectangle(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y);
		boolean leftMouseButtonDown = Mouse.getButton() == MouseEvent.BUTTON1;
		
		if (rect.contains(new Point(Mouse.getX(), Mouse.getY()))) {
			if (!inside) {
				if (leftMouseButtonDown) 
					ignorePressed = true;	//if we clicked before entering the button, then we don't want to register the press
				else {
					ignorePressed = false;
				}
				buttonListener.entered(this);//bad
				
			}
			inside = true;
			
			if (!pressed && !ignorePressed && leftMouseButtonDown) {
				buttonListener.pressed(this);
				pressed = true;
			} else if (Mouse.getButton() == MouseEvent.NOBUTTON) {	//this will trigger when ANY button is let up. This is pretty bad, we should be using release events. (TODO)
				if (pressed) {
					buttonListener.released(this);
					pressed = false;
					//actionListener.perform();	this is handled through the UIButtonListener class				
				}
				ignorePressed = false;
			}
			
		}
		else {
			if (inside){
				buttonListener.exited(this);	
				//also call released? depends if we want it to function that way- but it should set pressed false TODO
				pressed = false;
			}
			inside = false;
		}
	}
	
//	public void setOffset(Vector2i offset) {
//		super.setOffset(offset);
//		if (label != null) {
//			label.setOffset(offset);
//		}
//		
//	}

	public void render(Graphics g) {
		
		int x = position.x + offset.x;
		int y = position.y + offset.y;
		
		if (image != null) {
			
			g.drawImage(image, x, y, null);
		} else {
			
			//Im thinking color will be set in update(), like when it's pressed
			g.setColor(color);
			g.fillRect(x, y, size.x, size.y);
			if (label != null) label.render(g);			
		}
		
	}

}
