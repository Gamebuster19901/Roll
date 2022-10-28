package com.gamebuster19901.roll.bot.game.stat;

public enum GameLayer {

	/**
	 * Something defined in the rules, for example:
	 * 
	 * EX: "your base Armor Class is 10 + Dexterity modifier"
	 */
	GAME_RULE(0),
	
	/**
	 * Something that is chosen or rolled for at character creation, 
	 * 
	 * EX: the player's class, rolled/point buy stats, etc
	 */
	CHOSEN(100),
	
	/**
	 * Something defined or altered by racial features or innate traits.
	 * 
	 * EX: Tortles have a base AC of 17 and gain no benefits to AC from dexterity or Armor
	 */
	INNATE_FEATURE(200),
	
	/**
	 * Something defined or altered by class or background features.
	 * 
	 * EX: Monks can have an AC of 10 + dexterity modifier + wisdom modifier
	 */
	CLASS_OR_BACKGROUND_FEATURE(300),
	
	/**
	 * Something defined or altered by a feat
	 * 
	 * EX: Characters with the dragon hide feat can calculate their AC as 13 + Dexterity modifier"
	 */
	FEAT(400),
	
	/**
	 * Something defined or altered by equipment a creature is wearing
	 * 
	 * EX: When you wear padded armor, your base AC becomes "11"
	 * EX: Disadvantage on stealth checks while wearing Plate armor.
	 */
	EQUIPMENT(500),
	
	/**
	 * Something defined or altered by a condition or disease the creature has
	 * 
	 * EX: Movement speed is 0 while the creature is restrained
	 */
	CONDITION(600),
	
	/**
	 *  Something defined or altered by an effect a creature is under
	 *  
	 *  EX: Longstrider increases movement speed by 10
	 */
	EFFECT(700),
	
	/**
	 * Special layer for damage resistances and immunities
	 */
	RESISTANCE(800), //also immunity
	
	/**
	 * Special layer for damage vulnerabilities
	 */
	VULNERABILITY(900),
	
	/**
	 * Stat change which is the result of the DM
	 * 
	 * EX: DM can change the controller or owner of a character
	 */
	DM_RULING(Integer.MAX_VALUE / 2),
	
	/**
	 * Values set by the database
	 * 
	 * Usually just IDs
	 */
	DATABASE(Integer.MAX_VALUE - 1),
	
	/**
	 * Stat change which is the result of an office action
	 * 
	 * Intended to be used for moderation
	 * 
	 * Can never be overridden except by other office actions
	 * 
	 * EX: setting the name of an inappropriate character
	 */
	OFFICE_ACTION(Integer.MAX_VALUE);
	;
	
	final int layer;

	GameLayer(int layer) {
		this.layer = layer;
	}

	public int getLayer() {
		return layer;
	}
	
}
