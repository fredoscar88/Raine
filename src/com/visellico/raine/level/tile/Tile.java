package com.visellico.raine.level.tile;

import com.visellico.raine.graphics.Screen;
import com.visellico.raine.graphics.Sprite;
import com.visellico.raine.level.tile.spawnLevel.*;

//vaguely abstract (this is a template- it won't be ran itself)
//this is written to be overwritten
//Also I guess we're creating all tiles here? (TODO)
//(TODO) (IMPORTANT) hah, trickt you with my todo.
//	What I need your attention for is the excessive abstraction. Every time we have a new tile type, TheCherno creates a new class- a bit much, frankly, when tiles share so
//	many attributes, they should either be divided into i.e SolidTile or BreakAbleTile with constructors that assign sprites, or have all of the attributes assigned in the
//	constructor. Special tiles might get their own classes but for generic, world-building tiles it is a little ridiculous.
public class Tile {

	public int x, y;
	public Sprite sprite;

	public String name;
	
	//(IMPORTANT) see GrassTile for general notes on a tile. Particularly, rendering. you bastard, Cherno >:V
	
	//GrassTile is a Tile, and has specific properties- particularly rendering. This is one grass tile, certainly, not sure why he did it like this, using static,
	//when surely just creating a new one each time? bleh idk
	//A general purpose grass tile, I guess :I but everytime we use this singular one, it's going to be carbon copied. To be sure, all grass are the same atm anyway, unless in tjhe
	//future we might want to load random sprites or something, or other shit like decorations
	public static Tile grass = new GrassTile(Sprite.grass);
	public static Tile flower = new FlowerTile(Sprite.flower);
	public static Tile rock = new RockTile(Sprite.rock);
	public static Tile voidTile = new VoidTile(Sprite.voidsprite);
	//Please note that if a parent class of something is returned, and a child of that parent is returned, 
	//then calling a method calls the child's overriding method not the one found in the parent (unless of course the child just inherits the parents method)
	//If the method exists in the child but not the parent, we can't call it (!) since we're looking at the PARENT not the CHILD. very strange but also makes sense to me, finally :I
	
	//--------------Spawn level tiles---------------------//
	public static Tile spawnGrass = new SpawnGrassTile(Sprite.spawn_grass);
	public static Tile spawnHedge = new SpawnHedgeTile(Sprite.spawn_hedge);
	public static Tile spawnWater = new SpawnWaterTile(Sprite.spawn_water);
	public static Tile spawnWall1 = new SpawnWallTile(Sprite.spawn_wall1);
	public static Tile spawnWall2 = new SpawnWallTile(Sprite.spawn_wall2);
	public static Tile spawnFloor = new SpawnFloorTile(Sprite.spawn_floor);
	
	//Color constants which we're doing here, I guess
	public static final int colSpawnGrass = 0xFF00FF00;
	public static final int colSpawnHedge = 0; //unused
	public static final int colSpawnWater = -1; //unused
	public static final int colSpawnFloor = 0xFF93541D;
	public static final int colSpawnWall1 = 0xFF877E79;
	public static final int colSpawnWall2 = 0xFF938E89;
	
	
	public Tile(Sprite sprite) {
		
		this.sprite = sprite;
				
	}
	
	//Tiles render themselves! They are called from elsewhere to render themselves! This method will give the command to render!
	public void render(int x, int y, Screen screen) {
	}
	
	//by default, no collision
	public boolean solid() {
		return false;
	}
	
}
