package com.gamebuster19901.excite.bot.graphics.dice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.gamebuster19901.excite.bot.game.Dice;
import com.gamebuster19901.excite.bot.game.Roll;
import com.gamebuster19901.excite.bot.graphics.Theme;
import com.gamebuster19901.excite.bot.graphics.Themed;

public abstract class DieGraphicBuilder {

	protected int rows;
	protected int columns;
	protected int width;
	protected int height;
	protected Roll roll;
	protected Dice dice;
	protected Theme theme;
	
	public DieGraphicBuilder(Themed theme, Roll roll) {
		this.roll = roll;
		this.dice = roll.getDice();
		rows = (int)Math.ceil(Math.abs((double)roll.getDieCount()) / 10);
		columns = roll.getDieCount();
		if(columns > 10) {
			columns = 10;
		}
		if(columns < 4) {
			columns = 4;
		}

		width = columns * 256;
		if(width > 2560) {
			width = 2560;
		}
		height = rows * 256;
		
		this.theme = theme.getTheme();
		
		System.out.println("Rows: " + rows);
		System.out.println("Columns: " + columns);
	}
	
	public abstract ByteArrayOutputStream buildImage() throws IOException;
	
}
