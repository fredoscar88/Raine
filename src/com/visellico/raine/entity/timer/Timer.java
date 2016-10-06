package com.visellico.raine.entity.timer;

import java.util.ArrayList;
import java.util.List;

import com.visellico.raine.entity.Entity;

public class Timer extends Entity {

	private int time;
	private int life;
	
	private TimerEvent t;
	private List<Entity> tEntities;
	
	public Timer(int life, TimerEvent t) {
		time = 0;	//I don't think I need to set this.
		this.life = life;
		tEntities = new ArrayList<Entity>();
		
		this.t = t;
	}
	
	public Timer(int life) {
		time = 0;
		this.life = life;
		tEntities = new ArrayList<Entity>();
	}
	
	public void addEntity(Entity e) {
		tEntities.add(e);
	}
	
	public void setTimerEvent(TimerEvent t) {
		this.t = t;
	}
	
	
	public void update() {
		if (time >= life) {
			remove();
			return;
		}
		for (Entity e : tEntities) {
			e.update();
		}
		t.action(time++);
	}
	
}
