package com.gamebuster19901.excite.bot.game;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gamebuster19901.excite.bot.command.Commands;
import com.gamebuster19901.excite.exception.DieParseException;
import com.mojang.brigadier.StringReader;

public class Dice {

	public static final Pattern ROLLING_REGEX = Pattern.compile("(?<amount>[\\+\\-]?\\d*)(?<die>d\\d*){0,1}");
	
	int cursor;
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
			this.cursor = matcher.start("amount");
			
			String amt = matcher.group("amount");
			int amount = 1;
			if(amt.equals("-")) {
				amount = -1;
			}
			else if(amt.equals("+")) {
				//no-op
			}
			else if(amt != null && !amt.isEmpty()) {
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
			
			System.out.println("amount: " + this.amount);
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
	
	
	public Dice getChild() {
		return child;
	}
	
	public void roll() {
		if(roll.isEmpty()) {
			if(die > 0) {
				for(int i = 0; i < Math.abs(amount); i++) {
					if(amount > 0) {
						roll.add(new Die(die));
					}
					else {
						roll.add(new Die(-die));
					}
				}
			}
			else if(die == 0) {
				roll.add(new Value(amount));
			}
			if(child != null) {
				child.roll();
			}
		}
		else {
			throw new IllegalStateException("Dice already rolled!");
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
		for(Die die : getAllDice()) {
			ret += die.getValue();
		}
		return ret;
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		Dice dice = this;
		do {
			if(dice.die != 0) {
				ret.append(dice.amount + "d" + dice.die);
			}
			else {
				ret.append(dice.amount);
			}
			dice = dice.child;
			if(dice != null) {
				if(dice.amount > -1) {
					ret.append("+");
				}
			}
		}
		while(dice != null);
		return ret.toString();
	}
	
	public String toDebugString() {
		StringBuilder ret = new StringBuilder();
		ret.append('<');
		for(Die die : roll) {
			ret.append("[" + die + "] ");
		}
		ret.append('>');
		return ret.toString();
	}
	
}
