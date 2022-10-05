package com.gamebuster19901.excite.bot.game;

import com.gamebuster19901.excite.bot.graphics.Theme;
import com.gamebuster19901.excite.bot.graphics.Themed;

public class Roll {
	
	private final Theme theme;
	private final Dice dice;
	
	public Roll(Dice dice) {
		this(Theme.DEFAULT_THEME, dice);
	}

	public Roll(Themed theme, Dice dice) {
		this.theme = theme.getTheme();
		this.dice = dice;
		dice.roll();
	}

	public Dice getDice() {
		return dice;
	}

	public Theme getTheme() {
		return theme;
	}
	
	public boolean isSortable() {
		return dice.getAllDice().size() > 1;
	}
	
}
