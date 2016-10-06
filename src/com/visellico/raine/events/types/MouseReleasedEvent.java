package com.visellico.raine.events.types;

import com.visellico.raine.events.Event;

public class MouseReleasedEvent extends MouseButtonEvent {

	public MouseReleasedEvent(int button, int x, int y) {
		super(button, x, y, Event.Type.MOUSE_RELEASED);
		
	}

	
	
}
