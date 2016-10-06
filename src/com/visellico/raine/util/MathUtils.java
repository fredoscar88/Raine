package com.visellico.raine.util;

public class MathUtils {
	
	private MathUtils() {
	}
	
	public static int smaller(int value, int min) {
		return value < min ? value : min;
	}
	
	public static int greater(int value, int max) {
		return value > max ? value : max;
	}
	
	/**
	 * Returns the closest int from the given value inside of the range
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static int clamp(int value, int min, int max) {
		if (value < min) return min;
		if (value > max) return max;
		return value;
	}
	
}
