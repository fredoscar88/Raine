package com.visellico.raine.entity.particle;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;

//(TODO) (IMPORTANT) I REALLY need to take some time to understand the collision-nu, with these particles, and the shit in Level. Reference eps 81,82, maybe 80

public class Particle extends Entity {

	private Sprite sprite;	//entities, the super class, should really have these...
	
	private int life;
	private int time;	//default instantiation to 0, but I will actually put it 0. just wanna test. remove this comment, add = 0 (todo)
	
	protected double xa, ya, za;	//amount of pixels it moves in x, and y. For animating
	protected double xx, yy, zz;	//'actual' x and y, but doubles. Because there's a double x. xx. same for y. yy.
									//Cast to int only when we render! We also have a Z now to influence the others
	
	//for one particle. But we're calling this constructor- from the other constructor!
	public Particle(int x, int y, int life) {
		this.x = x;
		this.y = y;
		this.xx = x;
		this.yy = y;
		this.life = life + random.nextInt(100); //yeah I like 100 //random.nextInt(10) - 10;	//varies from -10 to 10, a range of 20 centered around life. Formerly just nextInt(100) without and subtraction
		sprite = Sprite.particle_normal;	//if we want a different particle, just make sub classes. Still not fond of this approach. Put sprite in the constructor if you want
											//	a specific one.
//		sprite = Sprite.projectileWizard;
		
		
		this.xa = random.nextGaussian() /* + 2 /*can limit size/speed / 2*/;	//normal distribution random numbers between -1 and 1.
		this.ya = random.nextGaussian() /*can limit size/speed / 2*/;	//Normalized, so we'll get more stuff closer to 0 than -1 and 1.
		this.zz = random.nextFloat() + 2.0;	//less bounce, since they're hitting "stone" and stone particles wouldn't bounce so much would they.
		
//		particles.add(this);	//move this??? Frankly this whole constructor doesn't need to exist, but I suppose it makes sense. this is for each individual particle
								//	in the whole effect, while the other one is for coming up with these individuals, including itself. A little confusing and I think, frankly,
								//	that we don't need particles.add(this) in this constructor. Unless we are creating a single particle, in which case sure, it needs to update itself
								//... I guess.
								//I WAS RIGHT but I don't think we can creatre single particles, unless we go amount 1. lol.
	}
	
	//for more than one particle
/*	public Particle(int x, int y, int life, int amount) {
		//do everything before adding to array list- we don't have pointers to modify it afterward (unlike c++)
		this(x, y, life + 200);	//calling another constructor.... This does NOT create another instance of Particle! It's like calling a method.
		
		for (int i = 0; i < amount - 1; i++) {	//amount - 1 because we added one when calling the other constructor
			particles.add(new Particle(x, y, life));
		}
		particles.add(this);
		
		//So I need to do some explanation for myself, When using this constructor we are creating a particle instance, that can be used to reference all of 
		//	particles in the particles list. Note that we add "this" into the list as well, at the first position even
		//	The second constructor then with fewer args is called to add more, single particles into the list.
	} Leaving this here for posterity, since. Well. Using an Emitter Object now, but hey.. I learned shit*/
	
	public void render(Screen screen) {
		//for (Particle p : particles) {
		if (!isRemoved()) {
			screen.renderSprite((int) xx, (int) yy - (int) zz - 1, sprite, true);	//slight y rendering offset, as they are inside of pixels a lil bit
		}
	}
	
	public void update() {

		time++;
		if (time >= Integer.MAX_VALUE - 1) time = 0;
		//for (Particle p : particles) {
		if (time >= life) remove(); 
		za -= 0.1;
		
				//"bouncing" - also, decreases speed each "bounce" so it looks cool beans.
		if (zz < 0) {	//The stuff going on here could do with some more thought, so marking with (TODO)
			zz = 0;
			za *= -0.6; //invert direction and slows- This Z is almost a "depth" variable, at least emulating one.
			xa *= 0.5;
			ya *= 0.5;
		}
		
		move(xx + xa, (yy + ya) + (zz + za));	//zz is added to ya, and when we use THIS move method in particuler we are sending the position we want to move TO
//		this.xx += xa;	//coordinates update very tick by ?a much
//		this.yy += ya;	//they need to change direction on collision- duh
//		this.zz += za;	MOVED TO MOVE METHOD
	
			
//		this.xx += xa;	//'explodes' outwards, I guess
//		this.yy += ya;
	}

	private void move(double x, double y) {	//the x and y of where we want to moooove
		
		if (collision(x, y)) {
			this.xa *= -0.5;
			this.ya *= -0.5;
			this.za *= -0.5;
		}
		
		this.xx += xa;	//coordinates update very tick by ?a much
		this.yy += ya;	//they need to change direction on collision- duh
		this.zz += za;
		
	}
	
	public boolean collision(double x, double y) {	//copied from Level.tileCollision- no offset, since the size of the sprite is the size of what is actually displayed
		boolean solid = false;					//size 16, since that is the size of tiles? (IMPORTANT) (TODO)
												//I think we use size 16 as our bounding box since we end up rounding our "check" coordinates down anyway. so @* size we use 16 as a constant
		
		for (int c = 0; c < 4; c++) {
			double xt = (x - (c % 2) * 16) / 16;	//finding corners is alwatys fun! Frankly we should accept the "corner" params
		 	double yt = (y - (c / 2) * 16) / 16; 	//	and I should set up rendering the corners that collision is even detected from
		 	int ix = (int) Math.ceil(xt);	//ceiling/floor function- so round up/down.
		 	int iy = (int) Math.ceil(yt);	//Either one breaks collision on negXY (floor) or posXY (ceil). whew. So we need to use these in different circumstances, using if statements yay!
		 	if (c % 2 == 0) ix = (int) Math.floor(xt);	//when (c % 2 == 0), x is in the left most position
		 	if (c / 2 == 0) iy = (int) Math.floor(yt);	//when (c / 2 == 0), y is in the up most position WE'D CHANGE THESE TO ONE IF WE USED FLOOR INSTEAD OF CEIL ABOVE
		 	if (level.getTile(ix, iy).solid()) solid = true;		//WE CHANGE THIS IN ep 81. I MAY HAVE TO COME BACK TO WORK OUT AS SELF (TODO) EP 81 might need to rewaaaaatch!
		 	//PLEASE NOTE THAT IF WE RUN AGAINST THE TOP OF A BLOCK THE PARTICLES COLLIDE INSTANTLY! other sides seem fine, but the top- noop! Lower spawn pos??? Update collision??
		 	//Possible reason- the top pixel isnt perfectly centered but frankly I think it is, else Im feeding it the wrong size. Or just summoning wrong. (TODO) (IMPORTANT)
		}
		return solid;
		
	}
	
}
