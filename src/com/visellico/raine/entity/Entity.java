package com.visellico.raine.entity;

import java.util.Random;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.level.Level;
								//cant be instantiated which is A-OKAY
public abstract class Entity {	//abstract because this is a template, a framework- there will never be just an "entity" created, it will always be a type of entity
	
						
	public int x, y;	//location- probably not based on pixels but just the x,y for all tiles/pixels in the map
						//x,y redundant if we dont have a sprite
	
	//NB: (IMPORTANT) Instance variables, stuff defined OUTSIDE of methods, start with default values filled with 0s. Implicitly initialized with the default value of its type. 
	//Booleans are false, primitive data types are 0, etc. //This applies to static as well. Local variables inside of methods do not come with default values.
	//Objects are probably null.
	
	private boolean removed = false;
	protected Level level;	//The level that the entity is summoned in
	protected final Random random = new Random();
	
	public void update() {
	}
	
	public void render(Screen screen) {
	}
	
	/**
	 * Removes entity from game and sets its remove flag
	 */
	public void remove() {
		//Remove from level
		removed = true;
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void init(Level level) {
		this.level = level;
		//level.add(this);	//(IMPORTANT) TheCherno has NOT done this (yet)
		//(TODO) do level.add(this) here?
	}
	
}
