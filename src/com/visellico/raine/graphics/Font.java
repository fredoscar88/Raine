package com.visellico.raine.graphics;

public class Font {

//	private static SpriteSheet font = new SpriteSheet("/fonts/arial.png", width, height);

	public static final int charSize = 16;
	public static SpriteSheet font = new SpriteSheet("/fonts/arial.png", 208,96);	//oh, TheCherno. This is a spritesheet not a single font.
	//This DOES extract sprites! TheCherno is a bit of a nimrod lol, can't fault him tho. He drops this project for months at a time. But next time git comments pls
//	private static SpriteSheet font_characters = new SpriteSheet(font, 0, 0, 13, 6, 16);	//oh, TheCherno. This is a spritesheet not a single font.
	
	private static Sprite[] characters = Sprite.split(font);
	
	//only one font, won't change
	private static String charIndex = 	"ABCDEFGHIJKLM" +	//this is a "lookup" table heh
								  		"NOPQRSTUVWXYZ" + 
								  		"abcdefghijklm" + 
								  		"nopqrstuvwxyz" + 
								  		"0123456789.,'" + 	//can copy opening, closing single and double quotes from a text editor
								  		"'\"\";:!@$%()-+";
	
	public Font() {
	}
	
	public  void render(int x, int y, int kerning, int color, String text, Screen screen) {
//		screen.renderFont();
//		screen.renderSprite(50, 50, font_characters.getSprites()[0], false);
//		text = "Hello! This is a test.";
		
//		int spotInLine = -1;
		x -= charSize;	//offsets by one character, as we add the offset in our loop
		
		int line = 0;
		int xOffset = 0;	//Why does this subtract the size? well you see, it adds 16 plus some magic spacing each time anyway.
		
		for (int i = 0; i < text.length(); i++) {
			int yOffset = 0 + (line * 20);
			xOffset += 16 + kerning;
//			spotInLine++;	//instead of using i to render the x offset. This is because new lines need to render on the far left.
			char currentChar = text.charAt(i);
			if (currentChar == ' ') continue;	//skips spaces for 
			//THIS IS AWFUL
			if (currentChar == 'g' || currentChar == 'y' || currentChar == 'q' || currentChar == 'p' || currentChar == 'j' || currentChar == ',') yOffset += 4;
			if (currentChar == '\n') {
				line++;	//This will need to increase 2*20 when we use two \n. Also this if statement is awful. but ohwell.
				xOffset = 0;
			}
			int index = charIndex.indexOf(currentChar);
//			System.out.print(index + " ");
			if (index == -1) continue;
			
			screen.renderTextCharacter(x + xOffset, y + yOffset, characters[index], color, false);	//index is from the lookup table, so no need to adjust stuff. Really convenient actually. We choose values for the chars
			
		}
//		System.out.println();
		
	}
	
	//Specify text and position, with default color and spacing
	public  void render(int x, int y, String text, Screen screen) {
		render(x,y,0, 0,text,screen);	//kek. This is to keep this function, but that way if we want to change both of them we only need to modify one.
		
	}
	
	//Specify color but no spacing
	public  void render(int x, int y, int color, String text, Screen screen) {
		render(x,y,0, color,text,screen);
	}
	
}
