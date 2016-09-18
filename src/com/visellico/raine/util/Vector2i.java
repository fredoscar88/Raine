package com.visellico.raine.util;

//2dimensional vector using "i"- integers
public class Vector2i {
	
	//All the seemingly useless stuff in here is meant to be good practice for "util" classes, so it's chill
	private int x, y;
	
	public Vector2i() {
		set(0,0);
	}
	
	//Make a vector cloned off another one, for getting a pointer that doesnt point to the original.
	//	And since our setx, sety return a vector, well, guess what- we can discard our vector clone in preference of a clone with different x,y.
	//	ex new Vector2i(player_position).setX(50) where player_position is another vector. yay one line of code.
	public Vector2i (Vector2i vector) {
		set(vector.x, vector.y);
	}
	
	public Vector2i (int x, int y) {
		set(x,y);
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	//add a vector to this vector
	public Vector2i add(Vector2i vector) {
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}
	//subtract a vector from this vector
	public Vector2i subtract(Vector2i vector) {
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//returns this vector, so you can make changes and get it back. Not TOO applicable but, apparently, useful.
	public Vector2i setX(int x) {
		this.x = x;
		return this;
	}
	public Vector2i setY(int y) {
		this.y = y;
		return this;
	}
	
	public boolean equals(Object object) {
		
		if (!(object instanceof Vector2i)) return false;
		Vector2i vec = (Vector2i) object;
		if (this.x == vec.getX() && this.y == vec.getY()) return true;
		return false;
	}
	
}
