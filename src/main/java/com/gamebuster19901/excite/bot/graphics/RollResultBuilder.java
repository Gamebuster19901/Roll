package com.gamebuster19901.excite.bot.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gamebuster19901.excite.bot.game.Dice;
import com.gamebuster19901.excite.bot.game.Die;

public class RollResultBuilder extends DieGraphicBuilder {
	
	public RollResultBuilder(Dice dice) {
		super(dice);
	}

	@Override
	public ByteArrayOutputStream buildImage() throws IOException {
		ByteArrayOutputStream ret = new ByteArrayOutputStream(4194304);//4MiB
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Die die;
		BufferedImage dieImage = null;
		if(allDice.size() > 1000) {
			throw new IllegalArgumentException("Too many dice");
		}
		for(int d = 0; d < allDice.size(); d++) {
			int x = d % 10;
			int y = d / 10;
			die = allDice.get(d);
			int maxSize = die.getDieType().getTextSize();
			dieImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) dieImage.getGraphics();
			switch(die.getDieType()) {
				case d10:
				case d12:
				case d20:
				case d4:
				case d6:
				case d8:
					dieImage = ImageIO.read(getClass().getResourceAsStream("/com/gamebuster19901/roll/" + die.getDieType() + "/" + die.getSpin() + "/0000.png"));
					g = (Graphics2D) dieImage.getGraphics();
					renderText(g, die.getValue() + "", die.getDieType().getOffsetX(), die.getDieType().getOffsetY(), maxSize, maxSize);
					break;
				case modifier:
					Font font = new Font("aakar", Font.BOLD, 100);
					renderText(g, die.getValue() > -1 ? "+" + die.getValue() : die.getValue() + "", 0, 0, maxSize, maxSize);
					break;
				case other:
				case d100:
				default:
					dieImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB); //blank image
					g = (Graphics2D) dieImage.getGraphics();
					break;

			}
			g.setColor(Color.yellow);
			g.drawRect(0, 0, 255, 255);
			out.getGraphics().drawImage(dieImage, x * 256, y * 256, null);

			
			System.out.println("Width" + width);
			System.out.println("Height" + height);
			System.out.println("Drew die " + (x+y) + "(" + die.getDieType() + ") at " + x + ", " + y);
		}
		
		System.out.println("rows: " + rows);
		System.out.println("cols: " + columns);
		ImageIO.write(out, "png", ret);
		return ret;
	}
	
	private void renderText(Graphics2D g, String text, int offsetX, int offsetY, int maxWidth, int maxHeight) {
		GlyphVector string;
		Rectangle2D bounds;
		float fontSize = 300f;
		do {
			g.setFont(g.getFont().deriveFont(fontSize--));
			string = getString(g, text);
			bounds = getZeroedRectangle(getString(g, text).getVisualBounds());
			System.out.println(fontSize);
			if(fontSize < 1) {
				throw new AssertionError();
			}
		}
		while(bounds.getWidth() > maxWidth || bounds.getHeight() > maxHeight);
		
		g.setColor(Color.RED);
		Rectangle2D rect = getZeroedRectangle(string.getVisualBounds());
		int drawX = (int) ((int)Math.round(0 - string.getVisualBounds().getX()) + (128 + offsetX - rect.getWidth() / 2));
		int drawY = (int) ((int)Math.round(0 - string.getVisualBounds().getY()) + (128 + offsetY - rect.getHeight() / 2));
		System.out.println(("x: ") + rect.getX());
		g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		g.setColor(Color.GREEN);
		g.drawRect((int)(128 - (rect.getWidth() / 2)), (int)(128 - rect.getHeight() / 2), (int)rect.getWidth(), (int)rect.getHeight());
		
		g.setColor(Color.BLUE);
		g.drawGlyphVector(string, drawX, drawY);
	}
	
	private GlyphVector getString(Graphics2D graphics, String str) {
		return graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), str);
	}
	
	private Rectangle2D getZeroedRectangle(Rectangle2D rectangle) {
		return new Rectangle(0, 0, (int)rectangle.getWidth(), (int)rectangle.getHeight());
	}
	
	
}
