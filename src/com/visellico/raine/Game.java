package com.visellico.raine;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.visellico.raine.entity.mob.Player;
//wowzer
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.input.Keyboard;
import com.visellico.raine.level.Level;
import com.visellico.raine.level.SpawnLevel;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static int width = 300;	//resolution
	public static int height = width / 16 * 9; //aspect ratio 16:9 ||| 168
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
	
	private Screen screen;
	private Keyboard key;
	
	//image to draw things, I guess this goes on the graphics, which goes on the buffer strategy which goes on the canvas
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //not scaling w/h, no alpha
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();/*raster- rectangular array of pixels*/
										//image -> raster (array of pixels) -> data buffer, which handles the raster
	
	public Game() {
		
		Dimension size = new Dimension(width*scale, height*scale);	//note that scaling by three makes our pixels effectively 3^2 larger. just like if it was dragged out by a click
		setPreferredSize(size);	//comes from Canvas; java.awt.component, canvas extends component
		
		screen = new Screen(width, height);	//not scaled either, I guess
		frame = new JFrame();
		key = new Keyboard();
		level = new SpawnLevel("/levels/spawn.png");
//		level = new RandomLevel(64,64);
		player = new Player(key);
		
		//Must do this after key is initialized
		addKeyListener(key);	//adds this component to the canvas
		
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
	
	public void update() {
		
		key.update();
		player.update();
	}
	
	public void render() {
		//for buffering frames, so that we aren't drawing them life
		BufferStrategy bs = getBufferStrategy();	//gets the buffer strategy from this class, which extends canvas. we already have it.
		if (bs == null) {	//if our BS doesnt exist, then we create it.
			createBufferStrategy(3);	//creates the buffer strategy that bs is pointing to- i.e, "this" one. This if should not run again for the duration of runtime
			//triple buffering. Double ain't gud nuff, we want to be able to draw ASAP
			return;
		}
		
		screen.clear();
		int xScroll = player.x - (screen.width / 2);	//Offsets level rendering to center the player
		int yScroll = player.y - (screen.height / 2);	//this is because the player's position would other wise be at the top left of the screen.
		level.render(xScroll, yScroll, screen);	//So render at location of player, minus half the screen in either direction
		player.render(screen);
//		screen.render(xMoveView, yMoveView);	//determines what pixels should be what
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();	//Graphics links to buffer, a context to draw to the buffer, the graphics of the bugger
		g.drawImage(image, 0, 0, getWidth(), getHeight() , null);
		//g.drawLine(3*150, 0, 3*150, 3*533);
		//g.drawLine(0, scale *screen.height/2, 3*300, scale * screen.height/2);
		g.dispose();	//free the resources that we arent using- they arent even being displayed, would crash
		bs.show();	//makes next buffer visible		
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
