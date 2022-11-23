package com.gamebuster19901.roll.bot.game;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.EnumUtils;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.exception.DieParseException;
import com.gamebuster19901.roll.util.regex.RegexUtil;
import com.mojang.brigadier.StringReader;

public class Dice {

	public static final Pattern ROLLING_REGEX = Pattern.compile("(?<amount>[\\+\\-]?\\d*)(?<die>d\\d+){0,1}(?<type>(?:(?!d\\d+)[\\w\\s])*)");
	
	transient boolean badDie = false;
	
	final int amount;
	final int die;
	final String type;
	final DamageType damageType;
	Dice child;
	List<Die> dice = new ArrayList<>();
	
	public Dice(String s) {
		this(s, 0);
	}
	
	public Dice(String s, int cursor) {
		this(ROLLING_REGEX.matcher(s), cursor);
	}
	
	public Dice(StringReader s) {
		this(Commands.readUntilEnd(s), s.getCursor());
	}
	
	private Dice(Matcher matcher, int cursor) {
		matcher.find();
		//System.out.println("Cursor:" + cursor);
		//System.out.println("End:" + matcher.end());
		try {
			cursor = matcher.start("amount");
			
			
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
			
			if(RegexUtil.groupExists("die", matcher)) {
				
				matcher.groupCount();
				String die = matcher.group("die");
				cursor = matcher.start("die");
				if(die != null && !die.isEmpty()) {
					this.die = Integer.parseInt(die.substring(1));
				}
				else {
					this.die = 0;
				}
			}
			else {
				this.die = 0;
			}
			
			String type = matcher.group("type");
			if(type == null || type.isBlank()) {
				this.type = null;
				this.damageType = null;
			}
			else {
				this.type = type;
				if (EnumUtils.isValidEnum(DamageType.class, type)) {
					this.damageType = DamageType.valueOf(type);
				}
				else {
					this.damageType = null;
				}
			}
			/*
			System.out.println("amount: " + this.amount);
			System.out.println("die:" + this.die);
			System.out.println("type:" + type);
			System.out.println("damageType:" + damageType);
			
			System.out.println(matcher.requireEnd());
			System.out.println(matcher.start() + ", " + matcher.end());
			*/
			if(matcher.start() != matcher.end()) {
				Dice chld = new Dice(matcher, cursor + matcher.end());
				//For some reason java regex is being buggy, and matcher.hitEnd() is returning true prematurely
				//So the only way to tell if we have actually hit the end of the string is to check if matcher.start and matcher.end
				//are both the same value. If they are then we know the generated die is bad and we should not add it as a child.
				if(!chld.badDie) { 
					child = chld;
				}
			}
			else {
				badDie = true;
			}

			//System.out.println(matcher.lookingAt());
			group();
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
	
	public void group() {
		if(dice.isEmpty()) {
			if(die > 0) {
				for(int i = 0; i < Math.abs(amount); i++) {
					if(amount > 0) {
						dice.add(new Die(die));
					}
					else {
						dice.add(new Die(-die));
					}
				}
			}
			else if(die == 0) {
				if(type == null) {
					dice.add(new Value(amount));
				}
				else {
					if(amount < 0) {
						dice.add(new RollValue(type, true));
					}
					else {
						dice.add(new RollValue(type));
					}
				}
			}
		}
		else {
			throw new IllegalStateException("Dice already grouped!");
		}
	}
	
	@Deprecated
	public List<Die> getDice() {
		return dice;
	}
	
	public List<Die> getAllDice() {
		ArrayList<Die> dice = new ArrayList<>();
		dice.addAll(this.dice);
		if(hasChild()) {
			dice.addAll(child.getAllDice());
		}
		return dice;
	}
	
	public int getMaxValue() {
		return getMaxValue(null);
	}
	
	public int getMinValue() {
		return getMinValue(null);
	}
	
	public int getMaxValue(Statted statted) {
		int ret = 0;
		for(Die die : getAllDice()) {
			ret = ret + die.getMaxValue(statted);
		}
		return ret;
	}
	
	public int getMinValue(Statted statted) {
		int ret = 0;
		for(Die die : getAllDice()) {
			ret = ret + die.getMinValue(statted);
		}
		return ret;
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		Dice dice = this;
		do {
			if(dice.die != 0) {
				if(dice.type != null) {
					ret.append(dice.amount + "d" + dice.die + type);
				}
				else {
					ret.append(dice.amount + "d" + dice.die);
				}
			}
			else {
				if(dice.type == null) {
					ret.append(dice.amount);
				}
				else {
					if(dice.amount < 0) {
						ret.append("-" + dice.type);
					}
					else {
						ret.append(dice.type);
					}
				}
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
		for(Die die : dice) {
			ret.append("[" + die + "] ");
		}
		ret.append('>');
		return ret.toString();
	}
	
}
