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
	

	//does not overrider mob since it doesn't accept the same parameters
	protected void move() {	//may need to also update dir. ATM it does, because it calls the move(xa,ya) which DOES update dir. in Mob.
		
		xa = 0;
		ya = 0;
		
		//might want to make px, py doubles TODO
		int px = (int) level.getPlayerAt(0).getX();	//pixel precision
		int py = (int) level.getPlayerAt(0).getY();
		Vector2i start = new Vector2i((int) getX() >> 4, (int) getY() >> 4);
		Vector2i destination = new Vector2i(px >> 4, py >> 4);
		if (time % 5 == 0) path = level.findPath(start, destination);	//For now- shouldnt run every single update
		if (path != null) {
			if (path.size() > 0) {
				//TODO lol this is just to get my attention
				//Real talk, this seems inefficient because once the mob has moved to size-1 node, it won't go any further on that calculated path- it will move again only after
				//	it has recalculated the path. I guess that makes sense, in a way- the parent node is path - 1 or something. But frankly if that's the case then we don't need to 
				//	recalculate the path each time, surely? we just need to look at the next node? How do we know when we don't need to find a new path?
				//	If I really think about it, I believe this is true- we're only moving to the first node in the path before we just recalculate it, but I think part of A* also requires
				//	knowing the full path. I might try implementing it to just get the next node, as opposed to calculating a whole path.
				//		Hey, fun fact- we run into a severe framerate drop when our Star can't find a valid path. Either when Im in a block (with collision disabled) or otherwise have stuch
				//		myself in a closed off region surrounded by solid tiles. I think this is because it ends up checking like... EVERY option, but idk.
				//		It needs some resolution.
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
