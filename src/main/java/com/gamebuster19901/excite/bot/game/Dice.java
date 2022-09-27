package com.gamebuster19901.excite.bot.game;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.excite.bot.command.Commands;
import com.gamebuster19901.excite.exception.DieParseException;
import com.mojang.brigadier.StringReader;

public class Dice {

	public static final Pattern ROLLING_REGEX = Pattern.compile("(?<modifier>[\\+\\-])?(?<amount>\\d*)(?<die>d\\d*){0,1}");
	
	int cursor;
	final boolean positive;
	final int amount;
	final int die;
	Dice child;
	List<Die> roll = new ArrayList<>();
	
	public Dice(String s) {
		this(s, 0);
	}
	
	public Dice(String s, int cursor) {
		this(ROLLING_REGEX.matcher(s), cursor);

	}
	
	public Dice(StringReader s) {
		this(Commands.readString(s), s.getCursor());
	}
	
	private Dice(Matcher matcher, int cursor) {
		matcher.find();
		this.cursor = cursor;
		try {
			positive = !"-".equals(matcher.group("modifier"));
			this.cursor = matcher.start("modifier");
			
			String amt = matcher.group("amount");
			this.cursor = matcher.start("amount");
			int amount = 1;
			if(amt != null && !amt.isEmpty()) {
				amount = Integer.parseInt(amt);
			}

			this.amount = amount;
			
			String die = matcher.group("die");
			this.cursor = matcher.start("die");
			if(die != null && !die.isEmpty()) {
				this.die = Integer.parseInt(die.substring(1));
			}
			else {
				this.die = 0;
			}
			
			System.out.println("amount: " + amt);
			System.out.println("die:" + this.die);
			
			if(!matcher.hitEnd()) {
				child = new Dice(matcher, cursor + matcher.end());
			}
		}
		catch(Throwable t) {
			if(t instanceof DieParseException) {
				throw t;
			}
			throw new DieParseException(cursor, t);
		}
	}
	
	public boolean hasChild() {
		return child != null;
	}
	
	public void roll() {
		if(die > 0) {
			if(amount > 0) {
				for(int i = 0; i < amount; i++) {
					roll.add(new Die(die));
				}
			}
		}
		else if(die == 0) {
			roll.add(new Value(amount));
		}
	}
	
	public List<Die> getDice() {
		return roll;
	}
	
	public List<Die> getAllDice() {
		ArrayList<Die> dice = new ArrayList<>();
		dice.addAll(roll);
		if(hasChild()) {
			dice.addAll(child.getAllDice());
		}
		return dice;
	}
	
	public int getValue() {
		int ret = 0;
		for(Die die : roll) {
			ret += die.getValue();
		}
		if(!positive) {
			ret = ret * -1;
		}
		if(hasChild()) {
			child.roll();
			ret = ret + child.getValue();
		}
		return ret;
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append('<');
		if(!positive) {
			ret.append('-');
		}
		for(Die die : roll) {
			ret.append("[" + die + "] ");
		}
		ret.append('>');
		return ret.toString();
	}
	
}
