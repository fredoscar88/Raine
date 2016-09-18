package com.visellico.raine.entity.mob;

import java.util.List;

import com.visellico.raine.graphics.AnimatedSprite;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.SpriteSheet;
import com.visellico.raine.level.Node;
import com.visellico.raine.util.Vector2i;

public class Star extends Mob {
	
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 4);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 4);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 4);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 4);
		
	private AnimatedSprite curSprite = null;
	
	private double xa = 0;
	private double ya = 0;
	private List<Node> path = null;
	private int time = 0;
	
	public Star(int x, int y) {
		this.x = x << 4;	//again, should be using TileCoords..
		this.y = y << 4;
		setAnimatedFrameRate(10, up, down, left, right);
		
		curSprite = down;
		curSprite.setFrame(0);
		speed = .8;
	}
	
	private double calculateDistance(int x1, int y1, int x2, int y2) {
		double xd1 = (double) x1;
		double yd1 = (double) y1;
		double xd2 = (double) x2;
		double yd2 = (double) y2;
		
		return Math.sqrt(((xd1 - xd2) * (xd1 - xd2)) + ((yd1 - yd2) * (yd1 - yd2)));
	}

	//does not overrider mob since it doesn't accept the same parameters
	protected void move() {	//may need to also update dir. ATM it does, because it calls the move(xa,ya) which DOES update dir. in Mob.
		
		xa = 0;
		ya = 0;
		
		//might want to make px, py doubles TODO
		int px = (int) level.getPlayerAt(0).getX();	//pixel precision
		int py = (int) level.getPlayerAt(0).getY();
		Vector2i start = new Vector2i((int) getX() >> 4, (int) getY() >> 4);
		Vector2i destination = new Vector2i(px >> 4, py >> 4);
		if (time % 3 == 0) path = level.findPath(start, destination);	//For now- shouldnt run every single update
		if (path != null) {
			if (path.size() > 0) {
				Vector2i vec = path.get(path.size() - 1).tile;	//A* search algorithm adds the nodes in reverse order, so the last one in the list is closest to us
				if (		(int) x < vec.getX() << 4) xa+= speed;		//vector in tile precision
				else if (	(int) x > vec.getX() << 4) xa-= speed;
				if (		(int) y < vec.getY() << 4) ya+= speed;
				else if (	(int) y > vec.getY() << 4) ya-= speed;
			}
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
		
		time++;
		move();
		
		
		switch (dir) {
		case UP: curSprite = up;		break;
		case RIGHT: curSprite = right;	break;
		case DOWN: curSprite = down;	break;
		case LEFT: curSprite = left;	break;
		}

	}
	
	@Override
	public void render(Screen screen) {
		sprite = curSprite.getSprite();
		
		screen.renderMob((int) (x - (sprite.getWidth() >> 1)), (int) (y - (sprite.getHeight() >> 1)), this);
	}

}
