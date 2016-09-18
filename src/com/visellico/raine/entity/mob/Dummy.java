package com.visellico.raine.entity.mob;

import com.visellico.raine.graphics.AnimatedSprite;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.SpriteSheet;

public class Dummy extends Mob {

	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 4);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 4);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 4);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 4);
		
	AnimatedSprite curSprite = null;
	
	private int time;
	
	private int xa = 0;
	private int ya = 0;
	
	public Dummy(int x, int y) {	//coordinates to spawn at in pixel precision
		this.x = x << 4;	//Multiplied by sixteen. TODO in future, USE THE TILECOORDINATE CLASS that we made specifically for this reason!
		this.y = y << 4;
		curSprite = down;
		curSprite.setFrame(0);
		setAnimatedFrameRate(10, down, up, left, right);
	}
	
	@Override
	public void update() {
		
		time++;	//sixty is our UPS. Might wanna grab it from game. Anyway, equals 1 second.
		
		if (time % (random.nextInt(30) + 30) == 0) {
			
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
			
			if (random.nextInt(4) == 0) {
				xa = ya = 0;
			}
			if (xa != 0 && ya != 0) ya = 0;
			
		}
		
		
		if (xa != 0 || ya !=0) {
			move(xa, ya);
			walking = true;
			curSprite.update();
			
			switch (dir) {
			case UP: curSprite = up;		break;
			case RIGHT: curSprite = right;	break;
			case DOWN: curSprite = down;	break;
			case LEFT: curSprite = left;	break;
			}	
		} else {
			
			walking = false;
			curSprite.setFrame(0);
		}

	}

	@Override
	public void render(Screen screen) {
		sprite = curSprite.getSprite();
		screen.renderMob((int) (x - (sprite.SIZE >> 1)), (int) ((y - (sprite.SIZE >> 1))), sprite, 0);
	}

}
