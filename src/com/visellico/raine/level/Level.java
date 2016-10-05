package com.visellico.raine.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.visellico.raine.entity.Entity;
import com.visellico.raine.entity.mob.Mob;
import com.visellico.raine.entity.mob.Player;
import com.visellico.raine.entity.particle.Particle;
import com.visellico.raine.entity.projectile.Projectile;
import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.layers.Layer;
import com.visellico.raine.level.tile.Tile;
import com.visellico.raine.util.Vector2i;

//there will be two "types" here random gen and data loaded levels
//vaguely abstract. might make abstract.
public class Level extends Layer {	//So, MASSIVE ENGINE OVERHAUL well, chanes, but, Hey. TODO GIT UNDERSTOOD
	//Everything that ALL levels inherit goes here- a template
	//See SpawnLevel for an idea on how individual levels might be treated

	//Color constants;
	protected final int GREEN = 0xFF00FF00;	//grass
	protected final int PALE_YELLOW = 0xFFDDFF38;	//flower
	protected final int GRAY = 0xFF877E79;	//rock
	//-----
	
	protected int width, height;	//will be used with random levels- custom made ones will already have a width/height
	protected int[] tilesInt;
	protected int[] tiles;	//all the level tiles. acknowledging that only one level can be loaded at a time. Stores the colors.
	
	private List<Entity> entities = new ArrayList<Entity>();	//Side note: No need to instantiate an arraylist like ArrayList<Entity> since it's implied when we do List<entity>
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	private List<Particle> particles = new ArrayList<Particle>();
	private List<Player> players = new ArrayList<Player>();
	
	//welp I've never used one of these before.
	//Takes in two objects...  and in this case, compares their fCost to find the lower one, and switches their positions based on datshit I guess.
	private Comparator<Node> nodeSorter = new Comparator<Node>() {

		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) return +1;	//move it up in the array index
			if (n1.fCost > n0.fCost) return -1;	//move it down in the array index
			return 0;	//if they're the same
		}
		
	};	//using body brackets in this situation makes me uncomfortable because I dont understand it too well. Java nooooob TODO. Pretty sure it's just for overriding methods.
	
	//---------------Levels-----
	public static Level spawn = new SpawnLevel("/levels/spawn.png");
	
	//--------------------------
	
	//random level constructor
	public Level (int width, int height) {
		this.width = width;
		this.height = height;
		//Holds data on what type of tile is here
		tilesInt = new int[width*height];
		generateLevel();
	}
	
	//Load a level
	public Level (String path) {
		loadLevel(path);
		generateLevel();
		
//		this.add(new Spawner(16 * 16, 62 * 16, Spawner.Type.PARTICLE, 50, this));
		
	}
	
	protected void generateLevel() {
		
	}
	
	protected void loadLevel(String path) {
		
	}
	
	//happens sixty updates a second
	public void update() {
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
		}
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update();
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).update();
		}
		remove();	//removes dead entities that are flagged for removal		
	}
	
	private void remove() {
		for (int i = 0; i < entities.size(); i++) {	//do I need this?
			if (entities.get(i).isRemoved()) entities.remove(i);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).isRemoved()) projectiles.remove(i);
		}
		for (int i = 0; i < particles.size(); i++) {	//do I need this?
			if (particles.get(i).isRemoved()) particles.remove(i);
		}
		for (int i = 0; i < players.size(); i++) {	//do I need this?
			if (players.get(i).isRemoved()) players.remove(i);
		}
	}

	/**
	 * Calculates whether the entity will collide with a solid tile in it's next movement
	 * @param x X we are moving into
	 * @param y Y we are moving ito
	 * @param size Size of the sprite- for squares
	 * @param xOffset Offset sprite to upper left (where we check collision from)
	 * @param yOffset Offset sprite to upper left (where we check collision from)
	 * @return TRUE if the tile we are moving into is solid
	 */
	public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset) {
		boolean solid = false;
		
		for (int c = 0; c < 4; c++) {
			int xt = (x - (c % 2) * size + xOffset) >> 4;	//finding corners is alwatys fun! Frankly we should accept the "corner" params
		 	int yt = (y - (c / 2) * size + yOffset) >> 4; 	//	and I should set up rendering the corners that collision is even detected from
		 	if (getTile(xt, yt).solid()) solid = true;		//WE CHANGE THIS IN ep 81. I MAY HAVE TO COME BACK TO WORK OUT AS SELF (TODO) EP 81 might need to rewaaaaatch!
		 	//PLEASE NOTE THAT IF WE RUN AGAINST THE TOP OF A BLOCK THE PARTICLES COLLIDE INSTANTLY! other sides seem fine, but the top- noop! Lower spawn pos??? Update collision??
		 	//Possible reason- the top pixel isnt perfectly centered but frankly I think it is, else Im feeding it the wrong size. Or just summoning wrong. (TODO) (IMPORTANT)
		}
		return solid;
		
	}
	
	public List<Projectile> getProjectiles() {
		return projectiles;
	}
	
	//time of the level, manages time, like minecraft or something
	private void time() {
		
	}
	
	//xScrll and yScroll are the position of the player or somesuch, how much the screen is "scrolled". xScroll and yScroll are based on, I guess, the upper leftmost corner-
	//	or rather if that corner is 0,0 then x/y Scroll are the relative movements of ALL pixels but they also are the coords of the upper left pixel. hence why it works below when we do the
	//	bit-shift jazz. Kinda clever actually; the amount that EVERY pixel is offset by is also conveniently the coordinates of the upper leftmost pixel!
	//He names 'em xScroll, yScroll. Also all levels render the same, hence why we write this is in the parent class
	public void render(int xScroll, int yScroll, Screen screen) {
		
		//screen that we're drawing onto
		screen.setOffset(xScroll, yScroll);	//xScrp;; and yScroll are the position of the player, and the amount the map needs to "scroll" when the player moves- our offset
		
		//corner pins! Ways to tell the rendery bugger to stop. Because he's hit a corner. and also I guess where to start from. Only need two! Area of the level to RENDER!
		int x0 = xScroll >> 4;	//x0 is the left side of the screen- a vertical line (like x=0 in math)- where we start rendering x
								//divided by 16- to make it into tiles, as 16, 2^4, is the size of tiles :V, so pixels 0-15 belong to tile 0, as all of those >>4 == 0 (TODO) make sure to changeme if tiles change size
		int x1 = ((xScroll + screen.width) >> 4) + 1;	//TILE PRECISION >> 4 MASTERRACE
		int y0 = yScroll >> 4;	//four should be replaced by sqrt(tilesize) tbh
		int y1 = ((yScroll + screen.height) >> 4) + 1;	//x1 and y1 cornerpins are moved +1 tile out because when we try rendering the tiles in the screen, it gets cut off 
		
		//y and x run from, of course, y0 -> y1, x0 -> x1. We could have calculated them inside the for loop but, well, readability I guess.
		for (int y = y0; y < y1; y++) {	//the y1 and x1 render regions are bigger than the technical correct way.
			for (int x = x0; x < x1; x++) {
				//X and Y here cover all tiles that have parts visible on screen (including semi-hidden tiles)
				//Meaning they are in tile-level precision, not pixel. representing the position of the tile in the level.
				getTile(x, y).render(x, y, screen);	//GetTile accepts x and y and treats them as tile-level precision. <tile>.render however- also uses tile level precision?
				//each tile renders itself, since the tile knows what's diggity
				
				//For "drawn levels"
				
			}
			
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(screen);
		}
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(screen);
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).render(screen);
		}
		
	}
	
	//initialize an A* search. TODO TODO TODO git understood
	public List<Node> findPath(Vector2i start, Vector2i end) {
		
		List<Node> openList = new ArrayList<Node>();	//Every tile(node) that we can consider. All adjacent (and diagonal) tiles from current node. except parent. if we run out of these, there is no path to the finish
		List<Node> closedList = new ArrayList<Node>();	//Tiles (nodes) that we can't consider :c nodes are moved here when they are chucked out... or this is the list we return.
		Node current = new Node(start, null, 0, getDistance(start, end));
		openList.add(current);
		
		//while we are still checking out tiles
		while(openList.size() > 0) {
			
			Collections.sort(openList, nodeSorter);
			
			//openlist is the list of our adjacent tiles, which are sorted closest to furthest from end. So we consider the one closest to the finish (0) first.
			current = openList.get(0);
			//as soon as we find a path, we want to break out
			if (current.tile.equals(end)) {	//if we reached our end
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {	//starting tile has null. We are going from finish to start.
					path.add(current);
					current = current.parent;	//goes back one in the line
					//This whole "adding in reverse" reminds me of recursion
				}
				openList.clear();
				closedList.clear();
				return path;
				
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;	//we are here right now- 012 345 678
				int x = current.tile.getX();
				int y = current.tile.getY();
				int xi = (i % 3) -1;	//-1, 0, 1 on the x.	For targeting our adjacent tiles
				int yi = (i / 3) -1;	//-1, 0, 1 on the y.	Both of these are relative on each axis to a point in our 3x3 square, that is of size 9
				Tile at = getTile(x + xi, y + yi);	//yay getTile, our favorite
				if (at == null) continue;	//No tile? Move on!
				if (at.solid()) continue;	//Solid? Move on! Do not consider solid tiles since we can't travel thru them!
				Vector2i a = new Vector2i(x + xi, y + yi);	//"vector" for our "at" tile
				double gCost = current.gCost + getDistance(current.tile, a) == 1 ? 1 : 0.95; //Given that we;re comparing distances of adjacent tiles from our "current" node, itll always be +-1 or +-sqrt(2). This also ofc adds it onto the previous gCosts
				//gCost returns:
				//1 +
				//1.41 (sqrt(2)), can tweak this to be less so that it is preferred, like .95. we dont want it less than .5 cuz then 2 diagonals is better than 1 straight.
				//the ternary forces reults other than 1 to be favored. so diagonals really in this specific case.
				double hCost = getDistance(a, end);	//distance to the end. //IMPORTANT TODO we are tweaking the costs to let the guy favor diagonals first. Also travelling diagonals is actaully really quick since we dont do any trig, an fyi to TheCherno
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= node.gCost) continue;
				if (!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		//OpenList has size == 0, since that's the only way we're getting out of the while loop
		closedList.clear();
		return null;	//:c
	}
	
	private boolean vecInList(List<Node> list, Vector2i vector) {
		for (Node n : list) {
			if (n.tile.equals(vector)) return true;
			
		}
		return false;
		
	}
	
	//This should REALLY go in the Vector class. In fact it IS going in the vector class as of ep 104 >:VVVVVVVVVVVVVV UGH good programming, TheCherno lol. Of course he
	//	sometimes takes a few month's break in between episodes... I can see how it'd be easy to forget
	public double getDistance(Vector2i v1, Vector2i v2) {
		
		//Also- vectors traditionally don't use getters/setters, just have public member variables.
		double dx = v1.getX() - v2.getX();
		double dy = v1.getY() - v2.getY();
		double distance = Math.sqrt((dx * dx) + (dy * dy));
//		return distance == 1 ? 1 : 0.95;	//this is silly, but it lets him prefer diagonals (which normally are 1.41)
		return distance;
	}
	
	public void add(Entity e) {
		
		if (e instanceof Particle) {
			particles.add((Particle) e);
		} 
		else if (e instanceof Projectile) {
			projectiles.add((Projectile) e);
		}
		else if (e instanceof Player) {
			players.add((Player) e);
		}
		else {
			entities.add(e);
		}
		e.init(this);
	}

	public List<Player> getPlayers() {
		
		return players;
	}
	
	public List<Entity> getEntities(Entity e, int radius) {	//get all entities in the specified radius
		List<Entity> result = new ArrayList<Entity>();
		double ex = e.getX();
		double ey = e.getY();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.equals(e)) continue;
			double x = entity.getX();
			double y = entity.getY();
			
			double dx = x - ex;
			double dy = y - ey;
			Double distance = Math.sqrt((dx * dx) + (dy * dy));	//distance
			if (distance <= radius) {
				result.add(entity);
			}
		}
		
		return result;
	}
	
	public List<Entity> getMobs(Entity e, int radius) {	//get all entities in the specified radius
		List<Entity> result = new ArrayList<Entity>();
		double ex = e.getX();
		double ey = e.getY();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.equals(e) || !(e instanceof Mob)) continue;
			double x = entity.getX();
			double y = entity.getY();
			
			double dx = x - ex;
			double dy = y - ey;
			Double distance = Math.sqrt((dx * dx) + (dy * dy));	//distance
			if (distance <= radius) {
				result.add(entity);
			}
		}
		
		return result;
	}
	
	public List<Player> getPlayers(Entity e, int radius) {	//No items in entities is going to be a player! There is a seperate player list!
		List<Player> result = new ArrayList<>();
		double ex = e.getX();
		double ey = e.getY();
		for (int i = 0; i < players.size(); i++) {
			Entity entity = players.get(i);	//lol this can be Player player = players.get(i)
			if (players.get(i) instanceof Player) {	
				double x = entity.getX();
				double y = entity.getY();
				
				double dx = x - ex;
				double dy = y - ey;
				Double distance = Math.sqrt((dx * dx) + (dy * dy));	//distance
				if (distance <= radius) {
					result.add((Player) entity);
				}
			}
		}
		
		return result;
	}
	
	public Player getPlayerAt(int index) {
		return players.get(index);	//may need to handle exceptions
	}
	
	public Player getClientPlayer() {
		return players.get(0);	//Client player is first.... dunno how this meshes with networking
	}
	
	//We are now pulling color constants from Tile
	public Tile getTile(int x, int y) {	//x and y are at Tile-level position
		if (x < 0|| y < 0 || x >= width || y >= height ) return Tile.voidTile;	//If we go out of bounds in our map. VoidTile is what we render OOB
		switch (tiles[x + y * width])	{	//pulls the tile to render from this level's tile map. Get tile is ran for every tile in the level, me supposes, eventually
			case Tile.colSpawnGrass: return Tile.spawnGrass;	//A new grass tile tbh, but we've gone and just created a static version to be used wherever
			case Tile.colSpawnFloor: return Tile.spawnFloor;
			case Tile.colSpawnWall1: return Tile.spawnWall1;
			case Tile.colSpawnWall2: return Tile.spawnWall2;
			case Tile.colSpawnHedge: return Tile.spawnHedge;
			case Tile.colSpawnWater: return Tile.spawnWater;
			default: return Tile.voidTile;//return Tile.voidTile;
			//no need to break in the switch b/c return
		}
	}
	
}
