package com.visellico.raine.entity.mob;

import java.util.List;

import com.visellico.raine.Game;
import com.visellico.raine.entity.Entity;
import com.visellico.raine.entity.projectile.Projectile;
import com.visellico.raine.entity.projectile.WizardProjectile;
import com.visellico.raine.graphics.AnimatedSprite;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;
import com.visellico.raine.graphics.SpriteSheet;
import com.visellico.raine.input.Keyboard;
import com.visellico.raine.input.Mouse;

public class Player extends Mob {
	
	private Keyboard input;
//	private Sprite sprite;	//sprite of the player- switched around for when we animate. Funnily, we already have a sprite from mob. Let's comment this out for now..
	private int animate = 0;
	//private boolean walking = false;
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 4);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 4);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 4);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 4);
		
	AnimatedSprite curSprite = null;
	private final int FRAME_RATE = 10;
	
	//speed should belong to all mobs (TODO)
	private int fireRate = 0;
	
	//It may be that eventually we'll want sub-classes of player, but save that for another time, another game... (Important)
	//default spawn point
	public Player(Keyboard input) {	//will also use mouse in future
		this.input = input;	//the keyboard :I
		sprite = Sprite.player_back;
		curSprite = down;
	}
	
	//in case players need to be instantiated at a specific coordinate
	public Player(int x, int y, Keyboard input) {
		this.x = x;
		this.y = y;
		this.input = input;
		sprite = Sprite.player_back;
		curSprite = down;
		fireRate = WizardProjectile.FIRERATE;
		speed = 1;
		setAnimatedFrameRate(10, down, up, left, right);
		
//		ignoreCollision = true;
	}
	
	public void update() {
		//test.update();
		/*if (walking) {
			curSprite.update();	//Not the hugest fan of how we're implementing animated sprites tbh, previously we rendered 0,1,0,2 for idle, walk1, idle, walk2 which was nice/
								//but we don't have such an elegant animation with our animated sprite.			
		} else {
			curSprite.setFrame(0);	//previously, if we weren't walking, we would change to the idle-player sprite in the render method but now it makes sense to put in the update
									//	since this is where we update what we're trying to show
		}*/
		
		double xa = 0, ya = 0;
		//animate = (animate + 1) % 40;	//cycles between 0 and 39
		
		if (input.up) ya-=speed;
		if (input.down) ya+=speed;
		if (input.left) xa-=speed;
		if (input.right) xa+=speed;
		if (xa != 0 || ya !=0) {
			move(xa, ya);	//xa and ya are = to one of the following: {-1, 0, 1)
			walking = true;	//if we are moving, we are walking
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
		//xa and ya are delta variables, how much it is changing
		
		clear();	//Gets rid of projectiles that have over travelled, travelled beyond their range.
		if (fireRate > 0) {
			fireRate--;
		} else {
			updateShooting();
		}
		
	}
	
	//Why is this method in player. (TODO). I think it used to mean, basically, that all mobs could manage their own projectiles, but it's a little silly now, since mobs don't keep track
	private void clear() {
		for (int i = 0; i < level.getProjectiles().size(); i++) {
			Projectile p = level.getProjectiles().get(i);
			if (p.isRemoved()) level.getProjectiles().remove(i);
		}
		
	}

	private void updateShooting() {
		
		if (Mouse.getButton() == 1 && fireRate == 0) {
			//System.out.println(Mouse.getX() + " " + Game.getWindowWidth() / 2 + "\n" + Mouse.getY() + " " + Game.getWindowHeight() / 2);
			double dx = Mouse.getX() - (Game.getWindowWidth()/2);	//should be pulling this from screen, but game width/height is static
			double dy = Mouse.getY() - (Game.getWindowHeight()/2);
			double theta = Math.atan2(dy, dx);	//Basically, if dx= 0 tan is approaching infinite. But we all know that it should be 0 so this makes it 0.
			shoot(x, y, theta);	//Note we still use the players x y as the place to fire from!
				//all mobs can shoot, players shoot differently which is why we're calling the super class method but doing math here
			fireRate = WizardProjectile.FIRERATE;
		}
	}

	/**
	 * Renders the player by determining which sprite to draw and calling the playerRender method in the given screen object.
	 * Uses an "animate" variable cycling between 0 and 39 for walking. A 2d array stores all idle/walking animations for each direction
	 * @param screen Screen to draw the character on
	 */
	public void render(Screen screen) {
		//it's like a battle field in here TODO remove all these comments
		int flip = 0;
		
		//animate cycles between 0 and 39. In order each quartile goes 0,1,0,2 for IDLE, WALK 1, IDLE, WALK 2
		/*if (walking) {
			switch (dir) {
			case UP: curSprite = up;		break;
			case RIGHT: curSprite = right;	break;
			case DOWN: curSprite = down;	break;
			case LEFT: curSprite = left;	break;
			}							//CUR SPRITE NOT REDUNDANT
//			curSprite.setFrameRate(10);	//curSprite is redundant here, since we could just plunk sprite in the switch statement. I might re-do how animated sprites work, or add a
										//	new constructor, or something where I can put together which sprites I want instead of it pulling from a sheet.
										//	Alternatively, I can create a new sheet constructor which can add sprites to a sheet, because all we need is all of it animated.
										//		So what I can do is in paint.net increase the canvas size and add a new row.
			sprite = curSprite.getSprite();
//			switch (animate / 10) {
//			case 0:	sprite = Sprite.playerSprites[dir][0]; break;	//0-9	idle
//			case 1:	sprite = Sprite.playerSprites[dir][1]; break;	//10-19	walk 1
//			case 2:	sprite = Sprite.playerSprites[dir][0]; break;	//20-29	idle
//			case 3:	sprite = Sprite.playerSprites[dir][2]; break;	//30-39	walk 2
//			}
		}
		else {
			//Still need this here. If we aren't walking, frame/sprite set to 0 but we don't update the player's sprite
			sprite = curSprite.getSprite();
			//sprite = Sprite.playerSprites[dir][0];	//When the player is not walking/moving, the idle sprite for a given direction should be displayed
		}*/
		
		//The walking left animation (dir 3) is the right animation flipped
//		if (dir == 3) {
//			flip = 1;
//		}
		
		sprite = curSprite.getSprite();
//		System.out.println(sprite);
		screen.renderMob((int) (x - (sprite.SIZE >> 1)), (int) (y - (sprite.SIZE >> 1)), sprite, flip);	//Moves the player half way along each direction so it's centered
//		int flip = 0;
//		
//		//Thought suggested by a youtube commenter: for rendering these sprites, put em in a 2d array. access direction with dir and cycle the animation with 0,1,0,2 using animate
//		if (dir == 0) {
//			sprite = Sprite.player_forward;		
//			if (walking) {
////				sprite = sprite.player_forward_1; this is stupid funny
////				if (animate % 20 > 10) {
////					flip = 1;
////				}
//				if (walking) {
//					if (animate % 40 > 30) {	//animate 31-39
//						sprite = Sprite.player_forward_1;
//					} else if (animate % 40 > 20) {	//animate 21-30
//						//sprite = sprite.player_back;
//					} else if (animate % 40 > 10) {	//animate 11-20
//						sprite = Sprite.player_forward_2;
//					}
//				}
//			}
//		}
//		if (dir == 1) {
//			sprite = Sprite.player_side;	//animate 0-10
//			if (walking) {
//				if (animate % 40 > 30) {	//animate 31-39
//					sprite = Sprite.player_side_1;
//				} else if (animate % 40 > 20) {	//animate 21-30
//					//sprite = sprite.player_side;
//				} else if (animate % 40 > 10) {	//animate 11-20
//					sprite = Sprite.player_side_2;
//				}
//			}
//		
//		}
//		if (dir == 2) {
//			sprite = Sprite.player_back;
//			if (walking) {
//				if (animate % 40 > 30) {	//animate 31-39
//					sprite = Sprite.player_back_1;
//				} else if (animate % 40 > 20) {	//animate 21-30
//					//sprite = sprite.player_back;
//				} else if (animate % 40 > 10) {	//animate 11-20
//					sprite = Sprite.player_back_2;
//				}
//			}
////			if (walking) {
////				if (animate % 20 > 10) {
////					sprite = sprite.player_back_1;
////				} else {
////					sprite = sprite.player_back_2;
////				}
////			}
//		}
//		if (dir == 3) {
//			sprite = Sprite.player_side;
//			if (walking) {
//				if (animate % 40 > 30) {
//					sprite = Sprite.player_side_1;
//				} else if (animate % 40 > 20) {
//					//sprite = sprite.player_side;
//				} else if (animate % 40 > 10) {
//					sprite = Sprite.player_side_2;
//				}
//			}
//			flip = 1;	//flip x of player_side sprite 
//		}
//			//To think, instead of the obvious variable solution, made another method call here of renderPlayer just with the different parameter :I (IMPORTANT)
//		
//		//minus 16 to account for the fact that we draw from the top left to the bottom right, to center it we needs must put the center at the center.
//		//The center is size/2 (or 16).
//		screen.renderPlayer(x - (sprite.SIZE >> 1), y - (sprite.SIZE >> 1), sprite, flip);
		
	}
	
}
