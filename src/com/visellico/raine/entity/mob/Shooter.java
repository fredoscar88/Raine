package com.visellico.raine.entity.mob;

import java.util.List;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.entity.projectile.WizardProjectile;
import com.visellico.raine.graphics.AnimatedSprite;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.SpriteSheet;
import com.visellico.raine.util.Vector2i;

public class Shooter extends Mob {
	//--------------------- copied from dummy
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 4);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 4);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 4);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 4);
		
	AnimatedSprite curSprite = null;
	
	private int time;
	private int timesFired;
	
	private int xa = 0;
	private int ya = 0;
	//----------------------
	
	private int fireRate = 0;
	private Entity rand = null;
	
	public Shooter(double x, double y) {
		this.x = x * 16;	//seriously why did we bother with TileCoordinate if we ain't gunna use it.
		this.y = y * 16;
		curSprite = down;
		curSprite.setFrame(0);
		setAnimatedFrameRate(10, down, up, left, right);
		fireRate = WizardProjectile.FIRERATE;
	}
	
	public void update() {

		time++;
		
		if (time % (random.nextInt(30) + 30) == 0) {
			
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
			
			if (random.nextInt(4) == 0) {
				xa = ya = 0;
			}
			if (xa != 0 && ya != 0) ya = 0;
			
		}
		
		
		if (xa != 0 || ya !=0) {
			//move(xa, ya);
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
		
		if (fireRate > 0) {
			fireRate--;
		} else {
			updateShooting();
			
		}
		
	}
	
	//Potential problem TODO: If, for instance, firerate is 0 (so fire every update), Shooter may end up targeting the Particle Spawners :V so we might have to exclude those.
	public void updateShooting() {
		
//		shootClosest();
		shootRandom();
		
	}
	
	private void shootRandom() {
		timesFired = (timesFired + 1) % 3;
		if (timesFired == 0) {
			Player player = level.getClientPlayer();	//might need to change this
			List<Entity> entities = level.getMobs(this, 16*10);	//moves it into Pixel Precision
			if (player.getDistance(x, y) < 16*10) entities.add(level.getClientPlayer());	//it should only do this if it is in range tbh
			
			if (entities.size() > 0) {
				
				int index = random.nextInt(entities.size());
				//entity we'll shoot at
				rand = entities.get(index);
			}
			
		}
		
		if (rand != null) {
			//does the shooting
			double dx = rand.getX() - x;
			double dy = rand.getY() - y;
			double theta = Math.atan2(dy, dx);
			shoot(x, y, theta);
			fireRate = WizardProjectile.FIRERATE + 20;
		}
	}
	
	@SuppressWarnings("unused")
	private void shootClosest() {
		Player player = level.getClientPlayer();	//might need to change this
		List<Entity> entities = level.getMobs(this, 16*10);	//moves it into Pixel Precision
		if (player.getDistance(x, y) < 16 * 10) entities.add(level.getClientPlayer());	//it should only do this if it is in range tbh
		
		double min = 0;	//minimum distance
		Entity closest = null;	//entity we'll shoot at
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			double distance = Vector2i.getDistance(new Vector2i((int) x, (int) y), new Vector2i((int) e.getX(), (int) e.getY()));
			if (i == 0 || distance < min) {
				min = distance;	//min will end up as the distance of the closest entity
				closest = e;	//our aforementioned closest entity
			}
		}
		
		if (closest != null) {
			//does the shooting
			double dx = closest.getX() - x;
			double dy = closest.getY() - y;
			double theta = Math.atan2(dy, dx);
			shoot(x, y, theta);
			fireRate = WizardProjectile.FIRERATE + 20;
		}
	}

	public void render(Screen screen) {
		sprite = curSprite.getSprite();
		screen.renderMob((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2), this);
		
	}

}
//aaaaa