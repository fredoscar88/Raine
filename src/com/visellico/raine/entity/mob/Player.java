package com.visellico.raine.entity.mob;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.visellico.raine.Game;
import com.visellico.raine.entity.projectile.Projectile;
import com.visellico.raine.entity.projectile.WizardProjectile;
import com.visellico.raine.events.Event;
import com.visellico.raine.events.EventDispatcher;
import com.visellico.raine.events.EventListener;
import com.visellico.raine.events.types.MousePressedEvent;
import com.visellico.raine.events.types.MouseReleasedEvent;
import com.visellico.raine.graphics.AnimatedSprite;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;
import com.visellico.raine.graphics.SpriteSheet;
import com.visellico.raine.graphics.ui.UIActionListener;
import com.visellico.raine.graphics.ui.UIButton;
import com.visellico.raine.graphics.ui.UIButtonListener;
import com.visellico.raine.graphics.ui.UILabel;
import com.visellico.raine.graphics.ui.UIManager;
import com.visellico.raine.graphics.ui.UIPanel;
import com.visellico.raine.graphics.ui.UIProgressBar;
import com.visellico.raine.input.Keyboard;
import com.visellico.raine.input.Mouse;
import com.visellico.raine.util.ImageUtils;
import com.visellico.raine.util.Vector2i;

public class Player extends Mob implements EventListener {
	
	private String name;
	private int mana = 80;
	
	private Keyboard input;
//	private Sprite sprite;	//sprite of the player- switched around for when we animate. Funnily, we already have a sprite from mob. Let's comment this out for now..
//	private int animate = 0;
	//private boolean walking = false;
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 4);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 4);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 4);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 4);
		
	AnimatedSprite curSprite = null;
	private final int FRAME_RATE = 10;
	
	//speed should belong to all mobs (TODO)
	private int fireRate = 0;
	private boolean shooting = false;
	
	private UIManager ui;
	private UIProgressBar uiBarHealth;
	private UIButton randomButton;
	private UIButton exitButton;
	
	private BufferedImage biExitButton = null;
	private BufferedImage biExitButtonHover = null;
	private BufferedImage biHome, biHomeBright, biHomeDark;
//	UIProgressBar barMana;
	
	//It may be that eventually we'll want sub-classes of player, but save that for another time, another game... (Important)
	//default spawn point
	public Player(String name, Keyboard input) {	//will also use mouse in future
//		this.input = input;	//the keyboard :I
//		sprite = Sprite.player_back;
//		curSprite = down;
		this(name, 0, 0, input);	//note that this.x and this.y, what we find in Entity, are initialized @0 implicitly, hence why in the commented out code above we never set these things
	}
	
	//in case players need to be instantiated at a specific coordinate
	public Player(String name, int xspawn, int yspawn, Keyboard input) {
		this.name = name;
		this.x = xspawn;
		this.y = yspawn;
		this.input = input;
		sprite = Sprite.player_back;
		curSprite = down;
		fireRate = WizardProjectile.FIRERATE;
		speed = 1;
		health = maxHealth;
		setAnimatedFrameRate(FRAME_RATE, down, up, left, right);
		
		ui = Game.getUIMananger();
		
		
		try {
			biExitButton = ImageIO.read(new File("res/textures/button_exit.png"));
			biExitButtonHover = ImageIO.read(new File("res/textures/button_exitHover.png"));
			biHome = ImageIO.read(new File("res/textures/home.png"));
			biHomeBright = ImageUtils.changeBrightness(biHome, 30);	//It may be that we don't want to maintain references to these guys after we've set them in ButtonListener,
			biHomeDark = ImageUtils.changeBrightness(biHome, -30);	//	because while we could CHANGE them after the fact, there's no point, and now we have useless stuff on the heap
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Player's action/side panel UI---------------------------------------------------------
		UIPanel p = (UIPanel) new UIPanel(new Vector2i((300 - 80) * 3,0), new Vector2i(80*3, 168*3)).setColor(new Color(0x555555, false));
		
		UILabel labelName = (UILabel) new UILabel(new Vector2i(35, 200), this.name).setColor(0xBBBBBB);
		labelName.setFont(new Font("Verdana", Font.PLAIN, 24));
		labelName.dropShadow = true;

		uiBarHealth = (UIProgressBar) new UIProgressBar(new Vector2i(35, 210), new Vector2i(80*3 - 20*3, 20), .5).setColor(0xFF7F7F);
		
		UILabel labelHP = new UILabel(new Vector2i(uiBarHealth.position).add(new Vector2i(2, 16)), "HP".concat(": " + health));
		labelHP.setColor(0xFFFFFF);
		labelHP.dropShadow = true;
		labelHP.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		randomButton = new UIButton(new Vector2i(35, 235), new Vector2i(120, 30), new UIActionListener() {
			public void perform() {
				System.out.println("Button Pressed! and released!");
				//and we have access to Player variables!
			}	//anonymous inner type! And we can replace this UIActionListener!
		}, "Hi");
		randomButton.setText("Hello");
		randomButton.setActionListener(new UIActionListener() {
			public void perform() {
				health = health - 10 < 0 ? 0 : health - 10;
				labelHP.setText("HP: " + health);	//this label should update every "tick" but it doesnt since the label is a local variable in this constructor
//				health = maxHealth;	so this is something I want to do- a health regen when you're disengaged from battle
//				System.exit(0); lol
			}
		});
		randomButton.setButtonListener(new UIButtonListener() {
			public void released(UIButton button) {	//default behaviour is button performs when released, changing that to when it is pressed
//				button.setColor(0xAAAAFF);
				button.setColor(0xCDCDCD);
			}	//sniff it's so beautiful ;D	TODO
			public void pressed(UIButton button) {
				button.setColor(0xAAFFAA);
				button.performAction();
			}
		});
		//Problems with the method below: We would have a lot harder of a time changing it. FYI to do what the below method does,
		//	UIButton needds must implement UIActionListener. Good lesson on inheritance tho
		/*randomButton = new UIButton(new Vector2i(35, 235), new Vector2i(120, 30), "hi") {
			public void perform() {
				System.out.println("Boo");
			}
		};*/
		
		UIButton imageButton = new UIButton(new Vector2i(35, 270), biHome, new UIActionListener() {
			public void perform() {
				health = maxHealth;
				labelHP.setText("HP: " + health);
//				System.out.println("HEALTH: " + health);
				Teleport(20*16,59*16,1);
//				x = 20*16;	//something I've just learned, it has a hard time when you use arguments from parameters that also are the same as super class values.
//				y = 59*16;
			}
		});
		imageButton.setButtonListener(new UIButtonListener() {
			public void entered(UIButton button) {
				button.setImage(biHomeBright);
			}
			public void exited(UIButton button) {
				button.setImage(biHome);
			}
			public void pressed(UIButton button) {
				button.setImage(biHomeDark);
			}
			public void released(UIButton button) {
				button.setImage(biHomeBright);
				button.performAction();
			}
		});
		
		exitButton = new UIButton(new Vector2i(80*3 - 32, 168*3 - 32), biExitButton, new UIActionListener() {
			public void perform() {
				System.exit(0);
			}
		});
		exitButton.setButtonListener(new UIButtonListener() {
			public void entered(UIButton button) {
				button.setImage(biExitButtonHover);	//we could have read the pixels from the buffered image and done a replace like we do in Game, but nah. Two separate images is easier.
			}										//	if we look at arrays we have to be careful not to modify the pixels of the original image. ACTUALLY IMMA DO DIS
			public void exited(UIButton button) {
				button.setImage(biExitButton);
			}
		});
		
		p.add(new UIButton(new Vector2i(35, 330), new Vector2i(120, 30), new UIActionListener() {
			
			@Override
			public void perform() {
				Teleport((int)(x + 16*5), (int)(y), 60);
			}
		}, "TP"));
		
		p.add(labelName);
		p.add(uiBarHealth);
		p.add(labelHP);
		p.add(randomButton);
		p.add(imageButton);
		p.add(exitButton);
		
		
		ui.add(p);	//this doesnt have to happen after everything, but I like it this way, anyways.
		
		/*UIPanel death = new UIPanel(new Vector2i((300-300)*3, 0), new Vector2i((300-80)*3, (168)*3)).setColor(new Color(0xD0303030, true));
		UILabel d0 = (UILabel) new UILabel(new Vector2i(3*300/2 - 45*3, 3*168/2 - 5*3), "Died!").setColor(0xFF0000);
		death.add(d0);
		ui.add(death);*/
//		ignoreCollision = true;
	}
	
//	int time = 0;
	public void update() {
		//test.update();
		/*if (walking) {
			curSprite.update();	//Not the hugest fan of how we're implementing animated sprites tbh, previously we rendered 0,1,0,2 for idle, walk1, idle, walk2 which was nice/
								//but we don't have such an elegant animation with our animated sprite.			
		} else {
			curSprite.setFrame(0);	//previously, if we weren't walking, we would change to the idle-player sprite in the render method but now it makes sense to put in the update
									//	since this is where we update what we're trying to show
		}*/
//		ignoreCollision = true;	//this is here so I can edit it runtime. TODO remove
		double xa = 0, ya = 0;
		//animate = (animate + 1) % 40;	//cycles between 0 and 39
//		time++;
//		if (time % 10 == 0) {
//			if (input.manUp && mana < 100) mana++; 
//			if (input.manDown && mana > 0) mana--;
//			health++;
//		}
		
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
		} /*else {
			updateShooting();
		}*/
		updateShooting();
		
		uiBarHealth.setProgress((double) health / (double) maxHealth);
	}
	
	//Why is this method in player. (TODO). I think it used to mean, basically, that all mobs could manage their own projectiles, but it's a little silly now, since mobs don't keep track
	private void clear() {
		for (int i = 0; i < level.getProjectiles().size(); i++) {
			Projectile p = level.getProjectiles().get(i);
			if (p.isRemoved()) level.getProjectiles().remove(i);
		}
		
	}

	//LOL WELCOME BACK UPDATE SHOOTING
	//RIP updateShooting, the new Event System won't miss you but I will
	/*private void updateShooting() {
		
		if ((Mouse.getButton() == 1 && fireRate == 0) ) {//&& Mouse.getX() < Game.width * Game.scale) {
			//System.out.println(Mouse.getX() + " " + Game.getWindowWidth() / 2 + "\n" + Mouse.getY() + " " + Game.getWindowHeight() / 2);
			double dx = Mouse.getX() - (Game.getWindowWidth()/2);	//should be pulling this from screen, but game width/height is static
			double dy = Mouse.getY() - (Game.getWindowHeight()/2);
			double theta = Math.atan2(dy, dx);	//Basically, if dx= 0 tan is approaching infinite. But we all know that it should be 0 so this makes it 0.
			shoot(x, y, theta);	//Note we still use the players x y as the place to fire from!
				//all mobs can shoot, players shoot differently which is why we're calling the super class method but doing math here
			fireRate = WizardProjectile.FIRERATE;	//resets firerate. There's multiple ways to slow down how a mob fires, but this works, set it to an arbitrary limit, decrement each update
		}
	}*/
	private void updateShooting() {
		if (!shooting || fireRate > 0) 
			return;
		//well it's broken anyway. The layerstack is being handled all wrong I think... (TODO) the layers for the UI are not being used
		//but I might just have to actually add them to the Game.layerstack
		if (Mouse.getX() > 660) return;
		double dx = Mouse.getX() - (Game.getWindowWidth()/2);
		double dy = Mouse.getY() - (Game.getWindowHeight()/2);
		double theta = Math.atan2(dy, dx);
		shoot(x, y, theta);	//Note we still use the players x y as the place to fire from!
			//all mobs can shoot, players shoot differently which is why we're calling the super class method but doing math here
		fireRate = WizardProjectile.FIRERATE;	//resets firerate. There's multiple ways to slow down how a mob fires, but this works, set it to an arbitrary limit, decrement each update
		
	
	}
	
	public boolean onMousePressed(MousePressedEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {//&& Mouse.getX() < Game.width * Game.scale) {
			shooting = true;
			return true;	//Blocks the event because we handled it now
		}
		return false;	//whether or not we block the rest of the stuff receiving this event. False says we haven't handled it and other things requesting it can use it.
	}
	
	public boolean onMouseReleased(MouseReleasedEvent e) {	//YEAH THIS SHOULD WORK WITH MOUSEEVENT.BUTTON1
		if (e.getButton() == MouseEvent.BUTTON1) {
		//if (e.getButton() == MouseEvent.BUTTON1) {	//uhhh I dont think this ever runs if this is MouseEvent.BUTTON1. Yeah this... it should only go when we release Button1 but that doesnt work TODO
//			System.out.println("Mouse released");
			shooting = false;
			return true;
		}
		return false;//whether or not we block the rest of the stuff receiving this event
	}
	
	//Player actualy does something with events. But really TODO TODO TODO git understood lambdas. Shorter ways of writing an inner class, like when we put body braces in a parameter
	public void onEvent(Event event) {
//		System.out.println(event);	//.....yay
		EventDispatcher dispatcher = new EventDispatcher(event);
//		System.out.println(event.getType() + " hey you, the mouse released thingy, always gives 0 for the mouses button. Should be BUTTON1\nelse shooting" 
//											+ "never turns off. I hope this is noticeable enough");
		dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> (onMousePressed((MousePressedEvent) e)));
		dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> (onMouseReleased((MouseReleasedEvent) e)));
	}
	
	public String getName() {
		return this.name;
	}

	/**
	 * Renders the player by determining which sprite to draw and calling the playerRender method in the given screen object.
	 * Uses an "animate" variable cycling between 0 and 39 for walking. A 2d array stores all idle/walking animations for each direction
	 * @param screen Screen to draw the character on
	 */
	public void render(Screen screen) {
		
//		Debug.drawRect(screen, 50, 50, 16, 16, false);
		
		
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
