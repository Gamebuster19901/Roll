package com.gamebuster19901.excite.bot.graphics.dice.animation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.gamebuster19901.excite.bot.game.Die;
import com.gamebuster19901.excite.bot.game.Roll;
import com.gamebuster19901.excite.bot.graphics.Themed;
import com.gamebuster19901.excite.bot.graphics.dice.DieGraphicBuilder;

@Deprecated
public class DieAnimationBuilder extends DieGraphicBuilder{
	
	public DieAnimationBuilder(Themed theme, Roll roll) {
		super(theme, roll);
	}
	
	@Override
	public ByteArrayOutputStream buildImage() throws IOException {
		ByteArrayOutputStream ret = new ByteArrayOutputStream(4194304);//4MiB
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		GifWriter writer = new GifWriter(ret, BufferedImage.TYPE_INT_RGB, 1, true);
		Die die;
		BufferedImage dieImage;
		for(int i = 0; i < 72; i++) { //24fps for 3 seconds = 72 frames
			BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for(int d = 0; d < allDice.size(); d++) {
				int x = d % 10;
				int y = d / 10;
				die = allDice.get(d);
				if(getClass().getResourceAsStream("/com/gamebuster19901/roll/" + die.getDieType() + "/" + die.getSpin() + "/" + fileNo(i) + ".png") == null) {
					throw new AssertionError("Could not find " + "/com/gamebuster19901/roll/" + die.getDieType() + "/" + die.getSpin() + "/" + fileNo(i) + ".png");
				}
				dieImage = ImageIO.read(getClass().getResourceAsStream("/com/gamebuster19901/roll/" + die.getDieType() + "/" + die.getSpin() + "/" + fileNo(i) + ".png"));
				frame.getGraphics().drawImage(dieImage, x * 256, y * 256, null);
				System.out.println("Width" + width);
				System.out.println("Height" + height);
				System.out.println("Drew die " + (x+y) + "(" + die.getDieType() + ") at " + x + ", " + y);
			}
			writer.write(frame);
		}
		System.out.println("rows: " + rows);
		System.out.println("cols: " + columns);
		writer.close();
		return ret;
	}
	
	private String fileNo(int i) {
		String s1;
		String s2 = i + "";
		for(s1 = s2; s1.length() < 4;) {
			s1 = "0" + s1;
		}
		return s1;
	}
	
}
