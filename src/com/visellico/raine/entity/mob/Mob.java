package com.visellico.raine.entity.mob;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.entity.projectile.Projectile;
import com.visellico.raine.entity.projectile.WizardProjectile;
import com.visellico.raine.graphics.AnimatedSprite;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;

public abstract class Mob extends Entity {
	
	protected enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	protected Direction dir = Direction.DOWN;
	
//	protected Sprite sprite;	//probably want this to be public, unless mobs render themselves which I guess they will
	//protected int dir = 2;	//direction 0123 NESW URBL. we also have to declare that initial direction else it won't find a sprite, as it will never be initialized
	protected boolean moving = false;
	protected boolean walking = false;	//unused
	protected double speed = 1;	//default speed
	
	protected boolean ignoreCollision = false;
	
	//protected List<Projectile> projectiles = new ArrayList<Projectile>();
	//we can tell who the projectiles belong to
	//	but apparently we're just keeping this in Level now
	
	//---TEMPORARY FOR SHOWING WHERE WE CHECK COLLISION---
	//	Actually, I'm going to keep this, so that I can turn on bounding boxes whenever I want.... ah but.
	//	if we have more mobs..... and what we have here is player specific... hm
//	public int xt = x;
//	public int yt = y;
//	public final int xtmp1 = 14;
//	public final int xtmp2 = 8;
//	public final int ytmp1 = 12;
//	public final int ytmp2 = 3;
//	public int xxa = 0;
//	public int yya = 0;
	
	//------------------------------
	
	//(TODO) take time to understand..
	public void move(double xa, double ya) {	//which way we're moving on the XAxis, YAxis. Personally I'd want to use xDelta and yDelta as names but WHATEVER. Three states, negative, 0, positive
		
		//This is the alternative to splitting up the collision- separating the movement. If we're moving on both axis, then handle movement one at a time.
		if (xa != 0 && ya !=0) {
			move(xa, 0);
			move(0, ya);
			return;	//else we'd move twice as fast going diagonally. Going to go with this method, splitting them up HERE, because not something I'd have thunk of. Bottom one either tho..
		}
		
		if (xa > 0) dir = Direction.RIGHT;	//right
		if (xa < 0) dir = Direction.LEFT;	//left
		if (ya > 0) dir = Direction.DOWN;	//down
		if (ya < 0) dir = Direction.UP;		//up
		//dir is just for rendering, down and up override left/right so if we press both up+right it will show up
		
		//xa and ya are delta variables, how much it is changing. so we can change twice as fast (Read: move twice as fast) by multing them by two
		//xa and ya also tell us WHICH direction we're moving in, +-x or +-y, to make 0123
		//if we are not colliding with anything, move. Dunno how this works because you can collide in any direction, so non-collided directions should be fine?
		

		//We're checking every step along our move path- since we aren't moving "1" at a time, we need to simulate it. We can move fractionally too, which is accounted for
		while (xa != 0) {	//when xa=0 we aren't moving anymore
			if (Math.abs(xa) > 1) {	//basically, can we move xa 1 closer to 0? we can use (xa - abs(xa) > 0 OR math.abs(xa) > 1
				if (!collision(abs(xa), ya) || ignoreCollision) {	//if we're not colliding into the tiles we're MOVING to
					this.x += abs(xa);
				}
				xa -= abs(xa);	//xa can sometimes be negative- our job tho is to get it closer to 0.
								//if xa is positive sub 1. if negative, sub neg 1 (add 1), both bring it closer to zero.
			} else {
				
				if (!collision(abs(xa), ya) || ignoreCollision) {	//if we're not colliding into the tiles we're MOVING to
					this.x += xa;	//we're down to the nitty gritty, checking the last part of our fraction. we know that |xa| here is < 1
				}
				xa = 0;
			}
		}
		
		while (ya != 0) {	//when xa=0 we aren't moving anymore
			if (Math.abs(ya) > 1) {	//basically, can we move xa 1 closer to 0? we can use (xa - abs(xa) > 0 OR math.abs(xa) > 1
				if (!collision(xa, abs(ya)) || ignoreCollision) {	//if we're not colliding into the tiles we're MOVING to
					this.y += abs(ya);
				}
				ya -= abs(ya);	//xa can sometimes be negative- our job tho is to get it closer to 0.
								//if xa is positive sub 1. if negative, sub neg 1 (add 1), both bring it closer to zero.
			} else {
				
				if (!collision(xa, abs(ya)) || ignoreCollision) {	//if we're not colliding into the tiles we're MOVING to
					this.y += ya;	//we're down to the nitty gritty, checking the last part of our fraction. we know that |xa| here is < 1
				}
				ya = 0;
			}
		}
		
//		
//		if (!collision(xa, ya) || ignoreCollision) {	//if we're not colliding into the tiles we're MOVING to
//			//x/y = {-1, 0, 1}
//			x += xa;
//			y += ya;
//		}
		//to fix "sliding" against walls, we can split collision detection up and substitute 0 for xa and ya in two separate cases. That's one way. Or use magic if statements
		
	}
	
	//weird absolute thing. consolidates the whole numberline down to -1 and 1.
	private int abs(Double d) {
		if (d < 0) return -1;
		return 1;
	}
	
	protected void shoot(double x, double y, double dir) {
//		dir *= 180 / Math.PI;	//convert to degrees
		Projectile p = new WizardProjectile(x,y, dir); //notsure how I feel about this cast
//		level.getProjectiles().add(p);	//the projectile belongs to this mob, so we add it to this mob's collection of projectiles
		level.add(p);
//		p.init(level);	initiliazing in level, I guess it's more abstract that way
	}
	
	public abstract void update();
	
	public abstract void render(Screen screen);
	
	public void setAnimatedFrameRate(int framerate, AnimatedSprite... sprite) {
		for (int i = 0; i < sprite.length; i++) {
			sprite[i].setFrameRate(framerate);
		}
	}
	
	private boolean collision(double xa, double ya) {	//really should call these delta..
		boolean solid = false;
		
		for (int c = 0; c < 4; c++) {
			double xt = ((x + xa) - (c % 2) * /*xtmp1 - xtmp2*/ 15) / 16;	//(TODO) again, comprehension of corners. not TOO difficult I guess.
		 	double yt = ((y + ya) - (c / 2) * /*ytmp1 + ytmp2*/ 15) / 16;	//normally, it'd be *16, aka our tile size. Not sprite size in this instance. So we reduce collision area, 
		 																	//	here to let our A* mob get around corners. I find reducing the A* mob's speed also helps
		 	
		 	int ix = (int) Math.ceil(xt);
		 	int iy = (int) Math.ceil(yt);
		 	if (c % 2 == 0) ix = (int) Math.floor(xt);
		 	if (c / 2 == 0) iy = (int) Math.floor(yt);
		 	if (level.getTile(ix, iy).solid()) solid = true;
		}
		return solid;
		
		/*int xt = (x + xa) >> 4;	//x TILE
		int yt = (y + ya) >> 4;	//y TILE
		
		//if tile in space Im moving is solid- then solid is true, and collision is trues
		if (level.getTile(xt, yt).solid()) solid = true;	//Clever! Using getTile in different ways, not just for rendering! Huh! I need to git gud at programming as I would not have thought of that.
			//When using getTile, coordinates need to be in tile level precision >:V
		return solid;	//just to git rid of error
		*/
	}
	

}