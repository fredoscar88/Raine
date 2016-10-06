package com.visellico.raine.events;

public class Event {

	public enum Type {
		MOUSE_PRESSED,
		MOUSE_RELEASED,
		MOUSE_MOVED
	}
	
	private Type type;
	boolean handled;	//visibility limited to the package
	
	//only subclasses can create an event
	protected Event(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
}
