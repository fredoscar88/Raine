package com.visellico.raine.entity.mob;

import java.util.List;

import com.visellico.raine.graphics.AnimatedSprite;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.SpriteSheet;

public class Chaser extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 4);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 4);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 4);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 4);
		
	private AnimatedSprite curSprite = null;
	
	private double xa = 0;
	private double ya = 0;
	
	public Chaser(int x, int y) {
		this.x = x << 4;	//again, should be using TileCoords..
		this.y = y << 4;
		setAnimatedFrameRate(10, up, down, left, right);
		
		curSprite = down;
		curSprite.setFrame(0);
//		sprite = sprite.voidsprite;
		speed = .8;
	}
	
	/*
	private double calculateDistance(int x1, int y1, int x2, int y2) {
		double xd1 = (double) x1;
		double yd1 = (double) y1;
		double xd2 = (double) x2;
		double yd2 = (double) y2;
		
		return Math.sqrt(((xd1 - xd2) * (xd1 - xd2)) + ((yd1 - yd2) * (yd1 - yd2)));
	}*/

	protected void move() {	//may need to also update dir. ATM it does, because it calls the move(xa,ya) which DOES update dir. in Mob.
		
		xa = 0;
		ya = 0;
		
		List<Player> players = level.getPlayers(this, 3*16);
		if (players.size() > 0) {
			Player player = players.get(0);	//This is being got every frame
			
	//		if (calculateDistance(x,y,player.getX(),player.getY()) > (double)(16*2)) {	//this works but it's a lil shite.
			if ((int) x < (int) player.getX()) xa+=speed;	//Mathematical sidenote: xa = (x - player.getX()) / x
			if ((int) x > (int) player.getX()) xa-=speed;	//Visual side note- the chaser would "jitter" as the chaser's pos wouldnt land on the player's pos on account of not being
			if ((int) y < (int) player.getY()) ya+=speed;	//	a multiple of the player's pos (or really, movement rate). To ammend, we cast as an int to add tolerance
			if ((int) y > (int) player.getY()) ya-=speed;
		}
			
		if (xa != 0 || ya !=0) {
			walking = true;
			move(xa, ya);
			curSprite.update();
		} else {
			
			walking = false;
			curSprite.setFrame(0);
		}
		
	}
	
	public void update() {
		
		move();
		
		switch (dir) {
		case UP: curSprite = up;		break;
		case RIGHT: curSprite = right;	break;
		case DOWN: curSprite = down;	break;
		case LEFT: curSprite = left;	break;
		}
		
//		if (xa != 0 || ya !=0) {
//			walking = true;
//			move(xa, ya);
//			curSprite.update();
//			
//			switch (dir) {
//			case UP: curSprite = up;		break;
//			case RIGHT: curSprite = right;	break;
//			case DOWN: curSprite = down;	break;
//			case LEFT: curSprite = left;	break;
//			}	
//		} else {
//			
//			walking = false;
//			curSprite.setFrame(0);
//		}

	}
	
	@Override
	public void render(Screen screen) {
		sprite = curSprite.getSprite();
		
		screen.renderMob((int) (x - (sprite.getWidth() >> 1)), (int) (y - (sprite.getHeight() >> 1)), this);
	}

}
