package com.visellico.raine.entity.spawner;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.level.Level;

public class Spawner extends Entity {
	
	public enum Type {
		MOB, PARTICLE
	}
	
	private Type type;
	
	/**
	 * Creates a new spawner
	 * @param x X coordinate of spawner
	 * @param y Y coordinate of spawner
	 * @param type Type of entity to spawn
	 * @param amount Amount to spawn
	 * @param level 
	 */
	public Spawner(int x, int y, Type type, int amount, Level level) {
		init(level);
		this.x = x;
		this.y = y;
		this.type = type;	//may not need this
		
	}
	
	//Spawners should not be rendering/updating what they spawn. If the spawner gets removed, say goodbye to whatever they rendered/updated, if they did render what they made.
	
}
