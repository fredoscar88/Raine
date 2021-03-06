package com.visellico.raine.entity.projectile;

import com.visellico.raine.entity.spawner.ParticleSpawner;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;

public class WizardProjectile extends Projectile {

	public static final int FIRERATE = 10;
	
	public WizardProjectile(double x, double y, double dir) {	//dir will probably be a double later on
		super(x, y, dir);
		angle = dir;
		speed = 1.5;
		range = random.nextInt(50) + 100;	//random vals
		damage = 20;		// ..
		sprite = Sprite.rotate(Sprite.projectileArrow, dir);	//projectileArrow;
		
		nx = speed * Math.cos(angle);	//Here's the nitty gritty, we're only travelling a length of "one" at a time. if our hypotenuse = 1, there is no need to multiply by speed.
		ny = speed * Math.sin(angle);	// Since speed would be 1, and 1 * Math.cos(angle) == Math.cos(angle). But speed is not 1! Trigonometry woo I know this stuff already.
									
	}

	public void render(Screen screen) {
		//We are casting to ints here, as opposed to keeping x, y as ints. we dont want to lose our precision early on.
		if (!isRemoved()) screen.renderProjectile((int) (x - sprite.SIZE / 2), (int) (y - sprite.SIZE / 2), this);
		//So. If we want to change where it shoots from, we need to change the x and y from which the angle is determined, as we want the
		//projectile to pass through the point that we click on (rendering wise and logic wise)
	}
	
	
	public void update() {
		
		if (level.tileCollision((int) (x + nx), (int) (y + ny), 6, 5, 5)) {
			explode();
		} 
		else {
			move();	//note how we still move even AFTER we check collision (TODO)
		}
		
	}
	
	
	

	protected void move() {
		x += nx;	//provided we still exist
		y += ny;
		if (distance() > range) remove();
		
		//System.out.println("Distance: " + distance());
		
	}
	
	private void explode() {
		//release particle effects and remove
		//Spawner s = new Spawner((int) x, (int) y, Spawner.Type.PARTICLE, 100, level);
		level.add(new ParticleSpawner((int) x, (int) y, 100, 50, level));
		remove();
	}


	private double distance() {
		double dist;
		dist = Math.abs(Math.sqrt((x-xOrigin)*(x-xOrigin) + (y-yOrigin)*(y-yOrigin)));	//no fear of the sqrt of a negative number, since any number multiplied by itself is positive (x-xOrigin)*(x-xOrigin)

		return dist;
	}
	
}
