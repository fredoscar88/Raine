package com.visellico.raine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.visellico.raine.entity.mob.Player;
import com.visellico.raine.events.Event;
import com.visellico.raine.events.EventListener;
//wowzer
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.layers.Layer;
import com.visellico.raine.graphics.ui.UIManager;
import com.visellico.raine.input.Keyboard;
import com.visellico.raine.input.Mouse;
import com.visellico.raine.level.Level;
import com.visellico.raine.level.TileCoordinate;
import com.visellico.raine.net.Client;
import com.visellico.raine.net.player.NetPlayer;
import com.visellico.rainecloud.serialization.RCDatabase;
import com.visellico.rainecloud.serialization.RCField;
import com.visellico.rainecloud.serialization.RCObject;

public class Game extends Canvas implements Runnable, EventListener {

	private static final long serialVersionUID = 1L;
	
	//width of our game viewing area.... (TODO) this really probably should just be a representation of thw window width, not our main viewing port.
	//Why? Because it makes more sense that way. we are accommodating for it when we set the Dimension.
	public static int width = 300 - 80;	//DEFAULT resolution
	public static int height = 168;//width / 16 * 9; //aspect ratio 16:9 ||| 168
	public static int scale = 3;	//How much the game will be scaled- multiply width/height
		//however we are only rendering for the 300 16:9, it's just scaled up
	
	//---------- game stuff
	//only one level should be loaded at a time, duh
	private Level level;
	private Player player;
	//----------
	
	private Thread thread;
	private JFrame frame;	//ugh
	public final static String title = "Raine";
	private boolean running = false;
	private boolean debug = true;
	
	private static UIManager uiManager;	
	private Screen screen;
	private Keyboard key;
	
	//image to draw things, I guess this goes on the graphics, which goes on the buffer strategy which goes on the canvas
	//here we could use a widthGame
	private BufferedImage image; //not scaling w/h, no alpha
	private int[] pixels;/*raster- rectangular array of pixels*/
										//image -> raster (array of pixels) -> data buffer, which handles the raster
	
	private List<Layer> layerStack = new ArrayList<Layer>();
	
	//TODO create a reload event or something that will reload all asset files, or maybe specific ones, that contain data on stuff like monster damage, etc., which can be edited runtime without our magic hotreplace
	public Game() {
		
		setSize();
		//widthGame (sense graphics g doesnt use the screen renderer
		screen = new Screen(width, height);	//not scaled either, I guess
		uiManager = new UIManager();  	//creating this BEFORE the player since player references this in it's constructor. Im fairly certain we can create it after if we wanted to, tho
		frame = new JFrame();
		key = new Keyboard();
		
		Client client = new Client("localhost", 8192);
		client.connect();
		
		RCDatabase db = RCDatabase.deserializeFromFile("res/data/screen.bin");
		client.send(db);		
		
		level = Level.spawn;	//new SpawnLevel("/levels/spawn.png");	//starts @spawn
//		level = new RandomLevel(64,64);
		addLayer(level);	//hooray
		//player = new Player(key);
		TileCoordinate playerSpawn = new TileCoordinate(20, 59);
		player = new Player("Fredo", playerSpawn.x(), playerSpawn.y(), key);	//adjusting player spawn. Tile sizes here are 16, multiplied by a coordinate in tile level precision, added by half a tile in pixel precision
															//we can edit this into the constructor in the player class as well, thanks youtube comments
															//however we just ended up using a tileCoordinate class that does 16x for us.

		level.addPlayer(player);
		level.addPlayer(new NetPlayer());
		
		//Must do this after key is initialized
		addKeyListener(key);	//adds this component to the canvas
		Mouse mouse = new Mouse(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		save();
		
	}
	
	private void setSize() {
		RCDatabase db = RCDatabase.deserializeFromFile("res/data/screen.bin");
		if (db != null) {
			RCObject obj = db.findObject("Resolution");
			width	= obj.findField("width").getInt();
			height	= obj.findField("height").getInt();
			scale	= obj.findField("scale").getInt();
		}
		
		Dimension size = new Dimension(width*scale + 80*scale, height*scale);	//note that scaling by three makes our pixels effectively 3^2 larger. just like if it was dragged out by a click
		setPreferredSize(size);	//comes from Canvas; java.awt.component, canvas extends component
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	private void save() {
		RCDatabase db = new RCDatabase("Screen");
		RCObject obj = new RCObject("Resolution");
		obj.addField(RCField.Int("width", width));
		obj.addField(RCField.Int("height", height));
		obj.addField(RCField.Int("scale", scale));
		
		db.addObject(obj);
		
		db.serializeToFile("res/data/screen.bin");
	}
	
	private void load() {
		
	}
	
	public static UIManager getUIMananger() {
		System.out.println(uiManager);
		return uiManager;
	}
	
	public void addLayer(Layer layer) {
		layerStack.add(layer);
	}
	
	//synchronized - to avoid memory conflicts, or overlapping. dont want to screw up.
	public synchronized void start() {
		
		running = true;
		
		//new thread of THIS game class, named "Display"
		thread = new Thread(this, "Game");
		thread.start();	//automatically runs the run() method as well
		
	}
	
	public synchronized void stop() {
		
		running = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
		
	//program running
	public void run() {
		final double ups = 60.0;	//updates per second
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();	//for displaying fps, ups data every second (1000 milliseconds)
		final double fpsRatio = 1000000000.0 / ups; //in 1 billion nano secs (1 sec) to x frames per sec
		double delta = 0;	//change in time
		long now;
		int frames = 0;	//for displaying update info
		int updates = 0;//for displaying update info
		
		requestFocus();
		
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / fpsRatio;	//compares elapsed time to a given division of a second (generally, 1/60 of a second, giving us 60 frames per sec
			lastTime = now;
			while (delta >= 1) {
				update(); //tick. Game logic. Limited speed. 60 times a second.
				updates++;
				
				//System.out.println(delta);
				delta--;	//NOT delta = 0. This is for the simple reason that delta has leftover, like 1.007 or something and that little bit counts! we lose that time if we set to 0
			}
			
			render();	//Draws to screen, unlimited speed
			frames++;
			
			//tells how many frames and updates we are getting in a second
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				//System.out.println("fps " + frames + ", updates " + updates);
				frame.setTitle(title + " | fps: " + frames + " ups: " + updates);
				frames = 0;
				updates = 0;
			}
		}
		stop();
	}
	
	public void onEvent(Event event) {
		//events go down the layer stack in reverse order
		for (int i = layerStack.size() - 1; i >= 0; i--) {
			layerStack.get(i).onEvent(event);
		}
	}
	
	public void update() {
		
		key.update();
//		player.update();	//screw it, player will be updated and rendered independently, at least THIS player will be.
		//level.update();
		//Update through the layerstack instead. Update layers here
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).update();
			//We render in first to last order, unlike how we process events
		}
		uiManager.update();
		
		
		
	}
//	int colorChange = 0;
	public void render() {
		//for buffering frames, so that we aren't drawing them life
		BufferStrategy bs = getBufferStrategy();	//gets the buffer strategy from this class, which extends canvas. we already have it.
		if (bs == null) {	//if our BS doesnt exist, then we create it.
			createBufferStrategy(3);	//creates the buffer strategy that bs is pointing to- i.e, "this" one. This if should not run again for the duration of runtime
			//triple buffering. Double ain't gud nuff, we want to be able to draw ASAP
			return;
		}
		
		//screen.clear();
		double xScroll = player.getX() - (screen.width / 2);	//Offsets level rendering to center the player
		double yScroll = player.getY() - (screen.height / 2);	//this is because the player's position would other wise be at the top left of the screen.
		level.setScroll((int) xScroll, (int) yScroll);
		//render layers here
		for (int i = 0; i < layerStack.size(); i++) {
			layerStack.get(i).render(screen);
			//We render in first to last order, unlike how we process events
		}
		
		// RIP level.render TODO so, we remove this b/c of the event system. level.render(screen);	//So render at location of player, minus half the screen in either direction
//		player.render(screen);	//Player is rendered after other mobs because the player should render above everything else. Which means it's being rendered twice- 
								//	I dont have a fix for this now since it should still render players from level.render (multiplayer), but I want player to belong to level 
								//	and not be updated on it's own.
		//screen.renderMovementPix(player.x, player.y, player.xxa, player.yya, player);
		
		
//		font.render(10, 10, -3, 0x00ffff, Integer.toString(Screen.ROTATE), screen);
//		Debug.drawText(Integer.toString(Screen.ROTATE), screen);
//		colorChange+=1000;
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i]; //NO NO NO seizure warning - colorChange ;//-100000000;//- 10050;	WOO COLOR CHANGING
		}
		//using our own custom rendering lets us control the screen better! But we are gunna use java's 2d rendering for our UI (TODO)
		//this overlays
		
		Graphics g = bs.getDrawGraphics();	//Graphics links to buffer, a context to draw to the buffer, the graphics of the bugger
		g.setColor(new Color(0xFF00FF));
		g.fillRect(0, 0, (width + 80)*3, (height)*3); //debug, lets us now when stuff isnt being rendered.
		//widthGame
		g.drawImage(image, 0, 0, width * scale, height * scale , null);	//this is like a strobe light if you reduce the getwidth, getheight :I
		uiManager.render(g);	//to reiterate, we're using Java's own graphics object, instead of our engine, to render UI elements.
			//note if we were using java's graphics object for stuff other than just UIManager, then we'd really be better suited putting all of our graphics related stuff into a
			//	seperate class file for some good ol' OOP
		
//		g.setFont(new java.awt.Font("Helvetica", 0, 15));
//		g.setColor(Color.WHITE);
//		g.drawString("Hello", 50, 50);
//		g.setColor(Color.WHITE);
//		g.setFont(new Font("Verdana", 0, 50));
		//g.fillRect(Mouse.getX() - 32, Mouse.getY() - 32, 64, 64);
		//g.drawString("Button: " + Mouse.getButton(), 50, 50);
		g.dispose();	//free the resources that we arent using- they arent even being displayed, would crash
		bs.show();	//makes next buffer visible		
	}
	
	static public int getWindowHeight() {
		return height * scale;
	}
	
	static public int getWindowWidth() {
		return width * scale;
	}
	
	//hurr, entry point
	public static void main(String[] args) {
		
		Game game = new Game();
		
		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);	//adds "this" canvas component, our game, onto the frame
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
		
	}
	
}