package com.visellico.raine.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import static com.visellico.raine.util.MathUtils.*;

public class ImageUtils {

	private ImageUtils(){
	}
	
	/**
	 * Changes brightness by adding a given value to each of the color channels for every pixel in a given image.
	 * Takes input image as TYPE_4BYTE_ABGR, but output image is TYPE_INT_ARGB.
	 * @param original A BufferedImage of type TYPE_4BYTE_ABGR (4x longer than TYPE_INT_ARGB)
	 * @param amount Amount to add to each color channel, negative or positive
	 * @return An image that is brighter or darker than the original
	 */
	public static BufferedImage changeBrightness(BufferedImage original, int amount) {

		BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		System.out.println(original.getType());
		//NOTE BIEN the stuff beneath returns the images of a pixel array using BufferedImage.TYPE_BYTE_ABGR (or something like that)
		byte[] originalPixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();	//class casting error if we cast to DataBufferInt
		int[] resultPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();
		
		int offset = 0;
		
		for (int yy = 0; yy < original.getHeight(); yy++) {
			for (int xx = 0; xx < original.getWidth(); xx++) {

				//This is a mad house.
				int a = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//whew this all has to do with how the different color channels are used with DataBufferByte
				int b = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//	So, the TYPE of the BufferedImage original == BufferedImage.TYPE_BYTE_ABGR
				int g = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//I frankly don't have a good idea of why each pixel in this array stores it's channels, well, collated
				int r = (int) Byte.toUnsignedInt(originalPixels[offset++]);	//	like this- It's not << 24 for alpha, it is in fact every four bits is one of the alpha bits. I think... (TODO)

				r = clamp(r + amount, 0, 0xFF);
				g = clamp(g + amount, 0, 0xFF);
				b = clamp(b + amount, 0, 0xFF);
				
				resultPixels[xx + yy * original.getWidth()] = (a << 24 | (r << 16) | (g << 8) | b);
				
			}
		}
		return result;
		
		/*
		int i = 0;
		for (int color : pixels) {

			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0xFF00) >> 8;
			int b = (color & 0xFF);

			r += amount;	//this needs to not be > 255, for all three of these
			g += amount;
			b += amount;
			
			newPixels[i] = ((color & 0xff000000) | (r << 16) | (g << 8) | b);
			i++;
		}*/		
	}
	
}
