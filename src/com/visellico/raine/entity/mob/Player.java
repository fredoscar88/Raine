package com.visellico.raine.entity.mob;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;
import com.visellico.raine.input.Keyboard;

public class Player extends Mob {
	
	private Keyboard input;
//	private Sprite sprite;	//sprite of the player- switched around for when we animate. Funnily, we already have a sprite from mob. Let's comment this out for now..
	private int animate = 0;
	private boolean walking = false;
	
	//It may be that eventually we'll want sub-classes of player, but save that for another time, another game... (Important)
	//default spawn point
	public Player(Keyboard input) {	//will also use mouse in future
		this.input = input;	//the keyboard :I
		sprite = Sprite.player_back;
	}
	
	//in case players need to be instantiated at a specific coordinate
	public Player(int x, int y, Keyboard input) {
		this.x = x;
		this.y = y;
		this.input = input;
		sprite = Sprite.player_back;
	}
	
	public void update() {
		int xa = 0, ya = 0;
		animate = (animate + 1) % 40;
		
		if (input.up) ya--;
		if (input.down) ya++;
		if (input.left) xa--;
		if (input.right) xa++;
		//we used to just have x+- and y+- here, but this gives control to move.
		if (xa != 0 || ya !=0) {
			move(xa, ya);	//xa and ya are = to one of the following: {-1, 0, 1)
			walking = true;	//if we are moving, we are walking
		} else {
			walking = false;
		}
		//xa and ya are delta variables, how much it is changing
	}
	
	/**
	 * Renders the player by determining which sprite to draw and calling the playerRender method in the given screen object.
	 * Uses an "animate" variable cycling between 0 and 39 for walking. A 2d array stores all idle/walking animations for each direction
	 * @param screen Screen to draw the character on
	 */
	public void render(Screen screen) {
		
		int flip = 0;
		
		//animate cycles between 0 and 39. In order each quartile goes 0,1,0,2 for IDLE, WALK 1, IDLE, WALK 2
		if (walking) {
			switch (animate / 10) {
			case 0:	sprite = Sprite.playerSprites[dir][0]; break;	//0-9	idle
			case 1:	sprite = Sprite.playerSprites[dir][1]; break;	//10-19	walk 1
			case 2:	sprite = Sprite.playerSprites[dir][0]; break;	//20-29	idle
			case 3:	sprite = Sprite.playerSprites[dir][2]; break;	//30-39	walk 2
			}
		}
		else {
			sprite = Sprite.playerSprites[dir][0];	//When the player is not walking/moving, the idle sprite for a given direction should be displayed
		}
		
		//The walking left animation (dir 3) is the right animation flipped
		if (dir == 3) {
			flip = 1;
		}
		
		screen.renderPlayer(x - (sprite.SIZE >> 1), y - (sprite.SIZE >> 1), sprite, flip);
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
