package com.gamebuster19901.roll.bot.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class Roll {
	
	private final String name;
	private final Dice dice;
	private final LinkedHashMap<Die, Integer> result = new LinkedHashMap<Die, Integer>();
	private final List<Entry<Die, Integer>> result2 = new ArrayList<Entry<Die, Integer>>();
	private final Statted statted;
	
	public Roll(Dice dice) {
		this(dice, null);
	}
	
	public Roll(Dice dice, Statted statted) {
		this("", dice, statted);
	}
	
	public Roll(String name, Dice dice, Statted statted) {
		this.name = name;
		this.dice = dice;
		this.statted = statted;
		roll(statted);
	}
	
	public void roll() {
		roll(statted);
	}
	
	private void roll(Statted statted) {
		if(result.isEmpty()) {
			for(Die die : dice.dice) {
				result.put(die, die.roll(statted));
			}

			if(dice.hasChild()) {
				Roll childRoll = new Roll(dice.child, statted);
				result.putAll(childRoll.result);
			}
			result2.addAll(result.entrySet());
		}
		else {
			throw new IllegalStateException("Dice already rolled!");
		}
	}
	
	public int getMinValue() {
		return dice.getMinValue(statted);
	}

	public int getMaxValue() {
		return dice.getMaxValue(statted);
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
	
	public Dice getDice() {
		return dice;
	}
	
	public boolean isSortable() {
		return dice.getAllDice().size() > 1;
	}
	
	public Statted getStatted() {
		return statted;
	}
	
	public String getName() {
		return name;
	}
	
}
