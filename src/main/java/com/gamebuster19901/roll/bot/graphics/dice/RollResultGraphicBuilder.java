package com.gamebuster19901.roll.bot.graphics.dice;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import com.gamebuster19901.roll.bot.game.Roll;
import com.gamebuster19901.roll.bot.graphics.Themed;

public class RollResultGraphicBuilder extends DieGraphicBuilder {

	public RollResultGraphicBuilder(Themed theme, Roll roll) {
		super(theme, roll);
	}

	@Override
	@Nullable
	public ByteArrayOutputStream buildImage() throws IOException {
		BufferedImage image = theme.getDieTheme().renderResult(roll);
		if(image != null) {
			ByteArrayOutputStream ret = new ByteArrayOutputStream(4194304);//4MiB
			ImageIO.write(theme.getDieTheme().renderResult(roll), "png", ret);
			return ret;
		}
		return null;
	}

}
