package com.visellico.raine.net.player;

import com.visellico.raine.entity.mob.Mob;
import com.visellico.raine.graphics.Screen;

public class NetPlayer extends Mob {

	public NetPlayer() {
		x = 20 * 16;
		y = 55 * 16;
	}
	
	public void update() {
		
	}

	public void render(Screen screen) {
		screen.fillRect((int) x, (int) y, 32, 32, 0x2030CC, true);
		
	}

}