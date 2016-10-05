package com.visellico.raine.entity.spawner;

import com.visellico.raine.entity.particle.Particle;
import com.visellico.raine.level.Level;

public class ParticleSpawner extends Spawner {

	private int life;
	
	
	public ParticleSpawner(int x, int y, int amount, int life, Level level) {
		super(x, y, Spawner.Type.PARTICLE, amount, level);
		this.life = life;
		
		for (int i = 0; i < amount; i++) {

			level.add(new Particle(x, y, this.life));
				
		}
		
		remove();
		
	}

	
	
}
