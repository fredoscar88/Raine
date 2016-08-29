package com.visellico.raine.entity.mob;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.graphics.Sprite;

public abstract class Mob extends Entity {
	
	protected Sprite sprite;	//probably want this to be public, unless mobs render themselves which I guess they will
	protected int dir = 2;	//direction 0123 NESW URBL. we also have to declare that initial direction else it won't find a sprite, as it will never be initialized
	protected boolean moving = false;
	
	public void move(int xa, int ya) {	//which way we're moving on the XAxis, YAxis. Personally I'd want to use xDelta and yDelta as names but WHATEVER. Three states, negative, 0, positive
		
		if (xa > 0) dir = 1;	//right
		if (xa < 0) dir = 3;	//left
		if (ya > 0) dir = 2;	//down
		if (ya < 0) dir = 0;	//up
		
		//xa and ya are delta variables, how much it is changing. so we can change twice as fast (Read: move twice as fast) by multing them by two
		//xa and ya also tell us WHICH direction we're moving in, +-x or +-y, to make 0123
		//if we are not colliding with anything, move. Dunno how this works because you can collide in any direction, so non-collided directions should be fine?
		if (!collision()) {
			//x/y = {-1, 0, 1}
			x += xa;	//apparently this doesn't really work 
			y += ya;
		}
	}
	
	public void update() {
	}
	
	public void render() {
	}
	
	private boolean collision() {
		return false;	//just to git rid of error
	}
	
	
}
