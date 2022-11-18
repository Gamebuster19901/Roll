package com.gamebuster19901.roll.bot.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import java.util.concurrent.ThreadLocalRandom;

public class Roll {
	
	private final Dice dice;
	private final LinkedHashMap<Die, Integer> result = new LinkedHashMap<Die, Integer>();
	private final List<Entry<Die, Integer>> result2 = new ArrayList<Entry<Die, Integer>>();
	
	public Roll(Dice dice) {
		this.dice = dice;
		roll();
	}
	
	public void roll() {
		if(result.isEmpty()) {
			if(dice.die > 0) {
				for(int i = 0; i < Math.abs(dice.amount); i++) {
					Die die;
					if(dice.amount > 0) {
						die = new Die(dice.die);
					}
					else {
						die = new Die(-dice.die);
					}
					result.put(die, roll(die));
				}
			}
			else if(dice.die == 0) {
				Die die = new Value(dice.amount);
				result.put(die, roll(die));
			}
			if(dice.hasChild()) {
				Roll childRoll = new Roll(dice.child);
				result.putAll(childRoll.result);
			}
			result2.addAll(result.entrySet());
		}
		else {
			throw new IllegalStateException("Dice already rolled!");
		}
	}
	
	public int getMinValue() {
		return dice.getMinValue();
	}

	public int getMaxValue() {
		return dice.getMaxValue();
	}
	
	public int getValue() {
		int ret = 0;
		for(int i : result.values()) {
			ret += i;
		}
		return ret;
	}
	
	public Entry<Die, Integer> getValue(int index) {
		return result2.get(index);
	}
	
	
	public List<Entry<Die, Integer>> getValues() {
		return List.copyOf(result.entrySet());
	}
	
	public List<Entry<Die, Integer>> getSortedValues() {
		ArrayList<Entry<Die, Integer>> set = new ArrayList<Entry<Die, Integer>>();
		set.addAll(result2);
		set.sort(new Comparator<Entry<Die, Integer>>() {
			@Override
			public int compare(Entry<Die, Integer> o1, Entry<Die, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		return set;
	}
	
	public int getDieCount() {
		return dice.getAllDice().size();
	}
	
	private int roll(Die die) {
		if(die instanceof Value) {
			return die.sides;
		}
		else {
			if(die.sides > 0) {
				return ThreadLocalRandom.current().nextInt(1, die.sides + 1);
			}
			else {
				return ThreadLocalRandom.current().nextInt(die.sides, 0);
			}
		}
	}
	
	public Dice getDice() {
		return dice;
	}
	
	public boolean isSortable() {
		return dice.getAllDice().size() > 1;
	}
	
}
