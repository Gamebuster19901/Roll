package com.gamebuster19901.excite.bot.graphics.dice;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.gamebuster19901.excite.bot.game.Die;
import com.gamebuster19901.excite.bot.game.Roll;

public class DieTheme {
	
	public static final DieTheme DEFAULT_DIE_THEME = new DieTheme(new Font("Aakar", Font.BOLD, 200), Color.BLUE, Color.GREEN.darker(), Color.RED, Color.WHITE);
	public static final DieTheme DEBUG_DIE_THEME = new DieTheme(new Font("Aakar", Font.BOLD, 200), Color.BLUE, Color.GREEN.darker(), Color.RED, Color.WHITE, true);
	
	private final Font font;
	private final Color textColor;
	private final Color goodColor;
	private final Color badColor;
	private final Color dieColor;
	private final boolean debug;
	
	public DieTheme(Font font, Color textColor, Color goodColor, Color badColor, Color dieColor) {
		this(font, textColor, goodColor, badColor, dieColor, false);
	}
	
	public DieTheme(Font font, Color textColor, Color goodColor, Color badColor, Color dieColor, boolean debug) {
		this.font = font;
		this.textColor = textColor;
		this.goodColor = goodColor;
		this.badColor = badColor;
		this.dieColor = dieColor;
		this.debug = debug;
	}
	
	public BufferedImage renderDie(Roll roll, Entry<Die, Integer> dieResult) throws IOException {
		BufferedImage dieImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) dieImage.getGraphics();
		int maxSize = 256;
		Die die = dieResult.getKey();
		int value = dieResult.getValue();
		switch(die.getDieType()) {
			case d10:
			case d12:
			case d20:
			case d4:
			case d6:
			case d8:
				maxSize = die.getDieType().getTextSize();
				dieImage = ImageIO.read(getClass().getResourceAsStream("/com/gamebuster19901/roll/" + die.getDieType() + "/" + die.getSpin() + "/0000.png"));
				g = (Graphics2D) dieImage.getGraphics();
				
				g.setColor(getTextColor());
				
				if(die.getSides() > -1 && value == die.getMaxValue()) {
					g.setColor(getGoodColor());
				}
				else if(value == 1 || value < 0) {
					g.setColor(getBadColor());
				}
				
				renderText(g, value + "", die.getDieType().getOffsetX(), die.getDieType().getOffsetY(), maxSize, maxSize);
				break;
			case modifier:
				g.setColor(getTextColor());
				renderText(g, value > -1 ? "+" + value : value + "", 0, 0, maxSize, maxSize);
				break;
			case other:
			case d100:
			default:
				dieImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB); //blank image
				g = (Graphics2D) dieImage.getGraphics();
				g.setColor(getTextColor());
				if(Math.abs(die.getSides()) > 2) { //so coin tosses are not always red/green
					if(die.getSides() > -1 && die.getSides() == value) {
						g.setColor(getGoodColor());
					}
					else if(value == 1 || value < 0) {
						g.setColor(getBadColor());
					}
				}
				renderText(g, value > -1 ? "+" + value : value + "", 0, 0, 211, 211);
				g.setColor(Color.YELLOW);
				if(die.getSides() > 0) {
					renderText(g, "d" + die.getSides(), 0, 100, 256, 45);
				}
				else {
					renderText(g, "-d" + Math.abs(die.getSides()), 0, 100, 256, 45);
				}
				
				break;
		}
		return dieImage;
	}
	
	private void renderText(Graphics2D g, String text, int offsetX, int offsetY, int maxWidth, int maxHeight) {
		GlyphVector string;
		Rectangle2D bounds;
		float fontSize = 300f;
		do {
			g.setFont(font.deriveFont(fontSize--));
			string = getString(g, text);
			bounds = getZeroedRectangle(getString(g, text).getVisualBounds());
			//System.out.println(fontSize);
			if(fontSize < 1) {
				throw new AssertionError();
			}
		}
		while(bounds.getWidth() > maxWidth || bounds.getHeight() > maxHeight);
		

		Rectangle2D rect = getZeroedRectangle(string.getVisualBounds());
		int drawX = (int) ((int)Math.round(0 - string.getVisualBounds().getX()) + (128 + offsetX - rect.getWidth() / 2));
		int drawY = (int) ((int)Math.round(0 - string.getVisualBounds().getY()) + (128 + offsetY - rect.getHeight() / 2));
		
		Color color = g.getColor();
		
		if(debug) {
			g.setColor(getDebugOutlineTextSize());
			System.out.println(("x: ") + rect.getX());
			g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
			
			g.setColor(getDebugOutlineTextBounds());
			g.drawRect((int)(128 - (rect.getWidth() / 2) + offsetX), (int)(128 - rect.getHeight() / 2) + offsetY, (int)rect.getWidth(), (int)rect.getHeight());
		}
		
		g.setColor(color);
		g.drawGlyphVector(string, drawX, drawY);
	}
	
	private GlyphVector getString(Graphics2D graphics, String str) {
		return graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), str);
	}
	
	private Rectangle2D getZeroedRectangle(Rectangle2D rectangle) {
		return new Rectangle(0, 0, (int)rectangle.getWidth(), (int)rectangle.getHeight());
	}
	
	public Font getFont() {
		return font;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public Color getGoodColor() {
		return goodColor;
	}
	
	public Color getBadColor() {
		return badColor;
	}
	
	public Color getDieColor() {
		return dieColor;
	}

	public Color getDebugOutline() {
		return Color.YELLOW;
	}
	
	public Color getDebugOutlineTextSize() {
		return Color.RED;
	}
	
	public Color getDebugOutlineTextBounds() {
		return Color.GREEN;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
}
