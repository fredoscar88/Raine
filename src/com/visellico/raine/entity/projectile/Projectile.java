package com.visellico.raine.entity.projectile;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.graphics.Sprite;

public abstract class Projectile extends Entity {
	
	protected Sprite sprite;
	protected final int xOrigin, yOrigin;
	protected double angle;
	protected double x, y;	//Overrides x and y from entity- since we need double floating point precision and not integers.
	protected double nx, ny; //new x, new y; for vectors
	protected double distance;
	protected double speed, rateOfFire, range, damage;	//rate of fire belongs to mob?? damage belongs to mob?? in that caase it can be put in the constructor for the subclass..
	
	
	public Projectile(int x, int y, double dir) {
		xOrigin = x;
		yOrigin = y;
		this.x = x;
		this.y = y;
		angle = dir;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public int getSpriteSize() {
		return sprite.SIZE;
	}
	
	protected void move() {
		
	}
}
