package com.gamebuster19901.roll.bot.graphics;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import com.gamebuster19901.roll.gson.GSerializable;

public interface ImageResource extends GSerializable {
	
	public BufferedImage getImage();
	
	public Path getPath();
	
}
