package com.gamebuster19901.excite.bot.graphics.dice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.gamebuster19901.excite.bot.game.Dice;
import com.gamebuster19901.excite.bot.game.Die;
import com.gamebuster19901.excite.bot.game.Roll;

public abstract class DieGraphicBuilder {

	protected int rows;
	protected int columns;
	protected int width;
	protected int height;
	protected Roll roll;
	protected Dice dice;
	protected List<Die> allDice;
	protected int dieCount;
	
	public DieGraphicBuilder(Roll roll) {
		this.roll = roll;
		this.dice = roll.getDice();
		allDice = dice.getAllDice();
		dieCount = allDice.size();
		rows = (int)Math.ceil(Math.abs((double)dieCount) / 10);
		columns = dieCount;
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
		
		//System.out.println("Rows: " + rows);
		//System.out.println("Columns: " + columns);
	}
	
	public abstract ByteArrayOutputStream buildImage() throws IOException;
	
}
