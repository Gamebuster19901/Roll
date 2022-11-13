package com.gamebuster19901.roll.bot.game.character;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.gamebuster19901.roll.bot.graphics.ImageResource;

public final class DefaultCharacterImageResource implements ImageResource {
	
	private static final String FILE = "/com/gamebuster19901/roll/character/default.png";
	public static final DefaultCharacterImageResource INSTANCE = new DefaultCharacterImageResource();
	
	private DefaultCharacterImageResource() {}
	
	@Override
	public BufferedImage getImage() {
		try {
			return ImageIO.read(ImageResource.class.getResourceAsStream(FILE));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public Path getPath() {
		return null;
	}

}
