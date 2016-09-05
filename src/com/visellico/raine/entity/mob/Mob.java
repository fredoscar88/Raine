package com.visellico.raine.entity.mob;

import java.util.ArrayList;
import java.util.List;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.entity.projectile.Projectile;
import com.visellico.raine.entity.projectile.WizardProjectile;
import com.visellico.raine.graphics.Sprite;

public abstract class Mob extends Entity {
	
	protected Sprite sprite;	//probably want this to be public, unless mobs render themselves which I guess they will
	protected int dir = 2;	//direction 0123 NESW URBL. we also have to declare that initial direction else it won't find a sprite, as it will never be initialized
	protected boolean moving = false;
	//protected boolean walking = false;	//unused
	
	protected List<Projectile> projectiles = new ArrayList<Projectile>();
	//we can tell who the projectiles belong to
	
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
	
	public void move(int xa, int ya) {	//which way we're moving on the XAxis, YAxis. Personally I'd want to use xDelta and yDelta as names but WHATEVER. Three states, negative, 0, positive
		
		//This is the alternative to splitting up the collision- separating the movement. If we're moving on both axis, then handle movement one at a time.
		if (xa != 0 && ya !=0) {
			move(xa, 0);
			move(0, ya);
			return;	//else we'd move twice as fast going diagonally. Going to go with this method, splitting them up HERE, because not something I'd have thunk of. Bottom one either tho..
		}
		
		if (xa > 0) dir = 1;	//right
		if (xa < 0) dir = 3;	//left
		if (ya > 0) dir = 2;	//down
		if (ya < 0) dir = 0;	//up
		
		//xa and ya are delta variables, how much it is changing. so we can change twice as fast (Read: move twice as fast) by multing them by two
		//xa and ya also tell us WHICH direction we're moving in, +-x or +-y, to make 0123
		//if we are not colliding with anything, move. Dunno how this works because you can collide in any direction, so non-collided directions should be fine?
		if (!collision(xa, ya)) {	//if we're not colliding into the tiles we're MOVING to
			//x/y = {-1, 0, 1}
			x += xa;
			y += ya;
		}
		//to fix "sliding" against walls, we can split collision detection up and substitute 0 for xa and ya in two separate cases. That's one way. Or use magic if statements
		
		System.out.println(projectiles.size());
	}
	
	protected void shoot(int x, int y, double dir) {
//		dir *= 180 / Math.PI;	//convert to degrees
		Projectile p = new WizardProjectile(x,y, dir); //notsure how I feel about this cast
		projectiles.add(p);	//the projectile belongs to this mob, so we add it to this mob's collection of projectiles
		level.add(p);
	}
	
	public void update() {
		
	}
	
	public void render() {
	}
	
	private boolean collision(int xa, int ya) {	//really should call these delta..
		boolean solid = false;
		
		for (int c = 0; c < 4; c++) {
			int xt = ((x + xa) + (c % 2) * /*xtmp1 - xtmp2*/ 14 - 8) >> 4;	//umm
		 	int yt = ((y + ya) + (c / 2) * /*ytmp1 + ytmp2*/ 12 + 3) >> 4;	//umm
//		 	xxa = x + xa;
//		 	yya = y + ya;
		 	//this.xt = ((x + xa) + (c % 2) * 14 - 8 /*14 - 8*/);
		 	//this.yt = ((y + ya) + (c / 2) * 12 + 3 /*12 + 3*/);
		 	//int tmp = ((c % 2) * 14 - 8);
		 	//System.out.println("X: " + x + ", X + XA= " + (x + xa) + "; + the other stuff: " + (x + xa + tmp));
		 	
		 	if (level.getTile(xt, yt).solid()) solid = true;
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