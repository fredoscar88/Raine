package com.visellico.raine.events;

public interface EventHandler {
	
	public boolean onEvent(Event event);	//All subscribers to an event must implement this
	//true or false based on whether or not further event subscribers can use the event
}
