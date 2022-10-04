package com.gamebuster19901.excite.bot.graphics;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.gamebuster19901.excite.bot.game.Dice;
import com.gamebuster19901.excite.bot.game.Die;

public abstract class DieGraphicBuilder {

	int rows;
	int columns;
	int width;
	int height;
	Dice dice;
	List<Die> allDice;
	int dieCount;
	HashMap<Dice, Color> dieColors = new HashMap<>();
	
	public DieGraphicBuilder(Dice dice) {

		
		this.dice = dice;
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
		setDieColors(dice);
		width = columns * 256;
		if(width > 2560) {
			width = 2560;
		}
		height = rows * 256;
		
		System.out.println("Rows: " + rows);
		System.out.println("Columns: " + columns);
	}
	
	public abstract ByteArrayOutputStream buildImage() throws IOException;
	
	protected void setDieColors(Dice dice) {
		dieColors.put(dice, Color.WHITE);
		if(dice.hasChild()) {
			setDieColors(dice.getChild());
		}
	}
	
}
