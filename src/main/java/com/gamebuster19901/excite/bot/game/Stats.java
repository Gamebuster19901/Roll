package com.gamebuster19901.excite.bot.game;

public interface Stats extends Stat {
	
	//Abilities
	public static final Stat Strength = Ability.Strength;
	public static final Stat Dexterity = Ability.Dexterity;
	public static final Stat Constitution = Ability.Constitution;
	public static final Stat Intelligence = Ability.Intelligence;
	public static final Stat Wisdom = Ability.Wisdom;
	public static final Stat Charisma = Ability.Charisma;
	
	//Stats
	public static final Stat HP = genStat("Hit Points", "HP");
	public static final Stat Armor_Class = genStat("Armor Class", "AC");
		
	 private static Stat genStat(String name, String shortName) {
		return new Stat() {
			@Override
			public String getName() {
				return name;
			}

			@Override
			public String shortName() {
				return shortName;
			}};
	}

}
