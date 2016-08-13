package com.visellico.raine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.swing.JFrame;

//wowzer
import com.visellico.raine.graphics.Screen;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static int width = 600;	//resolution
	public static int height = width / 16 * 9; //aspect ratio 16:9 ||| 168
	public static int scale = 3;	//How much the game will be scaled- multiply width/height
		//however we are only rendering for the 300 16:9, it's just scaled up
	
	private Thread thread;
	private JFrame frame;	//ugh
	public final static String title = "Raine";
	private boolean running = false;
	
	private Screen screen;
	
	//image to draw things, I guess this goes on the graphics, which goes on the buffer strategy which goes on the canvas
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //not scaling w/h, no alpha
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();/*raster- rectangular array of pixels*/
										//image -> raster (array of pixels) -> data buffer, which handles the raster
	
	public Game() {
		
		Dimension size = new Dimension(width*scale, height*scale);	//note that scaling by three makes our pixels effectively 3^2 larger. just like if it was dragged out by a click
		setPreferredSize(size);	//comes from Canvas; java.awt.component, canvas extends component
		
		screen = new Screen(width, height);	//not scaled either, I guess
		frame = new JFrame();
		
	}
	
	//synchronized - to avoid memory conflicts, or overlapping. dont want to screw up.
	public synchronized void start() {
		
		running = true;
		
		//new thread of THIS game class, named "Display"
		thread = new Thread(this, "Game");
		thread.start();	//automatically runs the run method as well
		
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
		long timer = System.currentTimeMillis();
		final double fpsRatio = 1000000000.0 / ups; //in 1 billion nano secs (1 sec) to x frames per sec
		double delta = 0;
		long now;
		int frames = 0;
		int updates = 0;
		
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / fpsRatio;	//compares elapsed time to a given division of a second
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
				
	}
	
	public void render() {
		//for buffering frames, so that we aren't drawing them life
		BufferStrategy bs = getBufferStrategy();	//gets the buffer strategy from this class, which extends canvas. we already have it.
		if (bs == null) {	//if our BS doesnt exist, then we create it.
			createBufferStrategy(3);	//creates the buffer strategy that bs is pointing to- i.e, "this" one
			//triple buffering. Double ain't gud nuff, we want to be able to draw ASAP
			return;
		}
		
		screen.clear();
		screen.render();
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();	//Graphics links to buffer, a context to draw to the buffer, the graphics of the bugger
		g.drawImage(image,0, 0, getWidth(), getHeight() , null);	
		
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
