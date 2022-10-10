package com.gamebuster19901.excite.bot.game.beyond.character;

public enum DNDBeyondPDFValue {

	CHARACTER_NAME("CharacterName"),
	CLASS_LEVEL("CLASS  LEVEL"),
	PLAYER_NAME("PLAYER NAME"),
	RACE("RACE"),
	BACKGROUND("BACKGROUND"),
	EXP("EXPERIENCE POINTS"),
	
	/**
	 * Yes, the D&D Beyond people named the fields in the PDF incorrectly,
	 * this is a D&D beyond issue, not an issue with Roll.
	 */
	STR("STRmod"),
	DEX("DEXmod"),
	CON("CONmod"),
	INT("INTmod"),
	WIS("WISmod"),
	CHA("CHAmod"),
	
	CON_MOD("CON"),
	STR_MOD("STR"),
	DEX_MOD("DEX"),
	INT_MOD("INT"),
	WIS_MOD("WIS"),
	CHA_MOD("CHA"),
	
	STR_PROF("StrProf"),
	DEX_PROF("DexProf"),
	CON_PROF("ConProf"),
	INT_PROF("IntProf"),
	WIS_PROF("WisProf"),
	CHA_PROF("ChaProf"),
	
	SAVING_STR("ST Strength"),
	SAVING_DEX("ST Dexterity"),
	SAVING_CON("ST Constitution"),
	SAVING_INT("ST Intelligence"),
	SAVING_WIS("ST Wisdom"),
	SAVING_CHA("ST Charisma"),
	
	DEFENSES("DEFENSES"),
	
	SAVE_MODS("SaveModifiers"),
	
	ACROBATICS("Acrobatics"),
	ANIMAL_HANDLING("Animal"), //Just called 'Animal' in PDF
	ARCANA("Arcana"),
	ATHLETICS("Athletics"),
	DECEPTION("Deception"),
	HISTORY("History"),
	INSIGHT("Insight"),
	INTIMIDATION("Intimidation"),
	INVESTIGATION("Investigation"),
	MEDICINE("Medicine"),
	NATURE("Nature"),
	PERCEPTION("Perception"),
	PERFORMANCE("Performance"),
	PERSUASION("Persuasion"),
	RELIGION("Religion"),
	SLEIGHT_OF_HAND("SleightofHand"), //yes, the 'of' isn't properly capitalized in the PDF either...
	STEALTH("Stealth"),
	SURVIVAL("Survival"),
	
	ACROBATICS_PROF("AcrobaticsProf"),
	ANIMAL_HANDLING_PROF("AnimalHandlingProf"),
	ARCANA_PROF("ArcanaProf"),
	ATHLETICS_PROF("AthleticsProf"),
	DECEPTION_PROF("DeceptionProf"),
	HISTORY_PROF("HistoryProf"),
	INSIGHT_PROF("InsightProf"),
	INTIMIDATION_PROF("IntimidationProf"),
	INVESTIGATION_PROF("InvestigationProf"),
	MEDICINE_PROF("MedicineProf"),
	NATURE_PROF("NatureProf"),
	PERCEPTION_PROF("PerceptionProf"),
	PERFORMANCE_PROF("PerformanceProf"),
	PERSUASION_PROF("PersuasionProf"),
	RELIGION_PROF("ReligionProf"),
	SLEIGHT_OF_HAND_PROF("SleightOfHandProf"),
	STEALTH_PROF("StealthProf"),
	SURVIVAL_PROF("SurvivalProf"),
	
	PASSIVE_PERCEPTION("Passive1"),
	PASSIVE_WISDOM("Passive2"),
	PASSIVE_INTELLIGENCE("Passive3"),
	
	SENSES("AdditionalSenses"),
	
	INITIATIVE("Init"),
	
	ARMOR_CLASS("AC"),
	
	PROFICIENCY_BONUS("ProfBonus"),
	
	//AbilitySaveDC
	
	MOVEMENT("Speed"),
	
	MAX_HP("MaxHP"),
	CURRENT_HP("CurrentHP"),
	TEMPORARY_HP("TempHP"),
	HIT_DICE("Total"),
	HIT_DICE_REMAINING("HD"),
	PROFICIENCIES_AND_LANGUAGES("ProficienciesLang"),
	
	COPPER("CP"),
	SILVER("SP"),
	ELECTRUM("EP"),
	GOLD("GP"),
	PLATINUM("PP"),
	
	SEX("GENDER"),
	SIZE("SIZE"),
	HEIGHT("HEIGHT"),
	WEIGHT("WEIGHT"),
	FAITH("FAITH"),
	SKIN("SKIN"),
	EYES("EYES"),
	HAIR("HAIR"),
	
	SPELLCASTING_ABILITY("spellCastingAbility", true),
	SPELL_SAVE_DC("spellSaveDC", true),
	SPELL_ATK_BONUS("spellAtkBonus", true),
	SPELL_CASTING_CLASS("spellCastingClass", true),
	
	//TODO: rest of spell shit
	
	;

	final String key;
	final boolean hasMultiple;
	
	DNDBeyondPDFValue(String string) {
		this(string, false);
	}
	
	DNDBeyondPDFValue(String string, boolean multiple) {
		this.key = string;
		hasMultiple = true;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getKey(int amount) {
		if(hasMultiple) {
			return key + amount;
		}
		return key;
	}
	
	public boolean hasMultiple() {
		return hasMultiple;
	}
	
}
