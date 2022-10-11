package com.gamebuster19901.roll.bot.graphics;

import java.awt.Color;

import com.gamebuster19901.roll.bot.graphics.dice.DieTheme;

public class Theme implements Themed {

	public static final Theme DEFAULT_THEME = new Theme(Color.GRAY, DieTheme.DEFAULT_DIE_THEME);
	public static final Theme DEBUG_THEME = new Theme(Color.MAGENTA, DieTheme.DEBUG_DIE_THEME);
	
	private final Color embedColor;
	
	private final DieTheme dieTheme;
	
	public Theme(Color embedColor, DieTheme dieTheme) {
		this.embedColor = embedColor;
		this.dieTheme = dieTheme;
	}

	public Color getEmbedColor() {
		return embedColor;
	}

	public DieTheme getDieTheme() {
		return dieTheme;
	}

	@Override
	public Theme getTheme() {
		return this;
	}
	
}
