package com.gamebuster19901.roll.bot.graphics.dice;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import com.gamebuster19901.roll.bot.game.Die;
import com.gamebuster19901.roll.bot.game.Roll;
import com.gamebuster19901.roll.bot.graphics.Themed;

public class RollResultBuilder extends DieGraphicBuilder {
	
	private boolean sort = false;
	
	public RollResultBuilder(Themed theme, Roll roll) {
		super(theme, roll);
	}
	
	public void setSorted(boolean sort) {
		this.sort = sort;
	}

	@Override
	public ByteArrayOutputStream buildImage() throws IOException {
		ByteArrayOutputStream ret = new ByteArrayOutputStream(4194304);//4MiB
		Entry<Die, Integer> die;
		BufferedImage dieImage = null;
		if(roll.getDieCount() <= 0) {
			throw new AssertionError("Zero dice?!");
		}
		if(roll.getDieCount() > 1000) {
			throw new IllegalArgumentException("Too many dice");
		}
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		DieTheme dieTheme = theme.getDieTheme();
		Entry<Die, Integer>[] values = sort ? roll.getSortedValues().toArray(new Entry[0]) : roll.getValues().toArray(new Entry[0]);
		for(int d = 0; d < roll.getDieCount(); d++) {
			int x = d % 10;
			int y = d / 10;
			die = values[d];
			dieImage = dieTheme.renderDie(roll, die);


			if(dieTheme.isDebug()) {
				Graphics2D g = (Graphics2D) out.getGraphics();
				g.setColor(dieTheme.getDebugOutline());
				g.drawRect(0, 0, 255, 255);
			}
			
			out.getGraphics().drawImage(dieImage, x * 256, y * 256, null);
			
			//System.out.println("Width" + width);
			//System.out.println("Height" + height);
			//System.out.println("Drew die " + (x+y) + "(" + die.getDieType() + ") at " + x + ", " + y);
		}
		
		//System.out.println("rows: " + rows);
		//System.out.println("cols: " + columns);
		ImageIO.write(out, "png", ret);
		return ret;
	}
	

	

	
	
}
