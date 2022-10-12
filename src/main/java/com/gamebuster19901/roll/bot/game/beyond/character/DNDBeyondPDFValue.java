package com.gamebuster19901.roll.bot.game.beyond.character;

import static com.gamebuster19901.roll.bot.game.SaveType.*;
import static com.gamebuster19901.roll.bot.game.beyond.character.DNDBeyondPDFValue.FunctionType.*;
import static com.gamebuster19901.roll.bot.game.stat.Ability.*;
import static com.gamebuster19901.roll.bot.game.stat.ProficiencyLevel.*;
import static com.gamebuster19901.roll.bot.game.stat.Skill.*;

import java.util.function.Function;

import com.gamebuster19901.roll.bot.game.Dice;
import com.gamebuster19901.roll.bot.game.character.Stat;
import com.gamebuster19901.roll.util.pdf.PDFText;

public enum DNDBeyondPDFValue {

	CHARACTER_NAME("CharacterName", null, STRING),
	CLASS_LEVEL("CLASS  LEVEL", null, STRING),
	PLAYER_NAME("PLAYER NAME", null, STRING),
	RACE("RACE", null, STRING),
	BACKGROUND("BACKGROUND", null, STRING),
	EXP("EXPERIENCE POINTS", Stat.EXP, (text) -> {
		return text.toString().contains("Milestone") 
				? text.toString() : 
					INTEGER.getFunction().apply(text);}),
	
	/**
	 * Yes, the D&D Beyond people named the fields in the PDF incorrectly,
	 * this is a D&D beyond issue, not an issue with Roll.
	 */
	STR("STRmod", Strength.getStat(), INTEGER),
	DEX("DEXmod ", Dexterity.getStat(), INTEGER), //D&D beyond PDF has extraneous space character
	CON("CONmod", Constitution.getStat(), INTEGER),
	INT("INTmod", Intelligence.getStat(), INTEGER),
	WIS("WISmod", Wisdom.getStat(), INTEGER),
	CHA("CHamod", Charisma.getStat(), INTEGER), //D&D beyond PDF not capitalized correctly
	

	STR_MOD("STR", Strength.getModStat(), INTEGER),
	DEX_MOD("DEX", Dexterity.getModStat(), INTEGER),
	CON_MOD("CON", Constitution.getModStat(), INTEGER),
	INT_MOD("INT", Intelligence.getModStat(), INTEGER),
	WIS_MOD("WIS", Wisdom.getModStat(), INTEGER),
	CHA_MOD("CHA", Charisma.getModStat(), INTEGER),
	
	STR_PROF("StrProf", Strength.getProficiencyStat(), PROFICIENCY_BOOL),
	DEX_PROF("DexProf", Dexterity.getProficiencyStat(), PROFICIENCY_BOOL),
	CON_PROF("ConProf", Constitution.getProficiencyStat(), PROFICIENCY_BOOL),
	INT_PROF("IntProf", Intelligence.getProficiencyStat(), PROFICIENCY_BOOL),
	WIS_PROF("WisProf", Wisdom.getProficiencyStat(), PROFICIENCY_BOOL),
	CHA_PROF("ChaProf", Charisma.getProficiencyStat(), PROFICIENCY_BOOL),
	
	SAVING_STR("ST Strength", CHARISMA_SAVE.getStat(), INTEGER),
	SAVING_DEX("ST Dexterity", DEXTERITY_SAVE.getStat(), INTEGER),
	SAVING_CON("ST Constitution", CONSTITUTION_SAVE.getStat(), INTEGER),
	SAVING_INT("ST Intelligence", INTELLIGENCE_SAVE.getStat(), INTEGER),
	SAVING_WIS("ST Wisdom", WISDOM_SAVE.getStat(), INTEGER),
	SAVING_CHA("ST Charisma", CHARISMA_SAVE.getStat(), INTEGER),
	
	DEFENSES("DEFENSES", null, STRING),
	
	SAVE_MODS("SaveModifiers", null, STRING),
	
	ACROBATICS("Acrobatics", Acrobatics.getStat(), INTEGER),
	ANIMAL_HANDLING("Animal", Animal_Handling.getStat(),  INTEGER), //Just called 'Animal' in PDF
	ARCANA("Arcana", Arcana.getStat(), INTEGER),
	ATHLETICS("Athletics", Athletics.getStat(), INTEGER),
	DECEPTION("Deception", Deception.getStat(), INTEGER),
	HISTORY("History", History.getStat(), INTEGER),
	INSIGHT("Insight", Insight.getStat(), INTEGER),
	INTIMIDATION("Intimidation", Intimidation.getStat(), INTEGER),
	INVESTIGATION("Investigation", Investigation.getStat(), INTEGER),
	MEDICINE("Medicine", Medicine.getStat(), INTEGER),
	NATURE("Nature", Nature.getStat(), INTEGER),
	PERCEPTION("Perception", Perception.getStat(), INTEGER),
	PERFORMANCE("Performance", Performance.getStat(), INTEGER),
	PERSUASION("Persuasion", Persuasion.getStat(), INTEGER),
	RELIGION("Religion", Religion.getStat(), INTEGER),
	SLEIGHT_OF_HAND("SleightofHand", Sleight_of_Hand.getStat(), INTEGER), //yes, the 'of' isn't properly capitalized in the PDF either...
	STEALTH("Stealth", Stealth.getStat(), INTEGER),
	SURVIVAL("Survival", Survival.getStat(), INTEGER),
	
	ACROBATICS_PROF("AcrobaticsProf", Acrobatics.getProficiencyStat(), PROFICIENCY_CHAR),
	ANIMAL_HANDLING_PROF("AnimalHandlingProf", Animal_Handling.getProficiencyStat(), PROFICIENCY_CHAR),
	ARCANA_PROF("ArcanaProf", Arcana.getProficiencyStat(), PROFICIENCY_CHAR),
	ATHLETICS_PROF("AthleticsProf", Athletics.getProficiencyStat(), PROFICIENCY_CHAR),
	DECEPTION_PROF("DeceptionProf", Deception.getProficiencyStat(), PROFICIENCY_CHAR),
	HISTORY_PROF("HistoryProf", History.getProficiencyStat(), PROFICIENCY_CHAR),
	INSIGHT_PROF("InsightProf", Insight.getProficiencyStat(), PROFICIENCY_CHAR),
	INTIMIDATION_PROF("IntimidationProf", Intimidation.getProficiencyStat(), PROFICIENCY_CHAR),
	INVESTIGATION_PROF("InvestigationProf", Investigation.getProficiencyStat(), PROFICIENCY_CHAR),
	MEDICINE_PROF("MedicineProf", Medicine.getProficiencyStat(), PROFICIENCY_CHAR),
	NATURE_PROF("NatureProf", Nature.getProficiencyStat(), PROFICIENCY_CHAR),
	PERCEPTION_PROF("PerceptionProf", Perception.getProficiencyStat(), PROFICIENCY_CHAR),
	PERFORMANCE_PROF("PerformanceProf", Performance.getProficiencyStat(), PROFICIENCY_CHAR),
	PERSUASION_PROF("PersuasionProf", Persuasion.getProficiencyStat(), PROFICIENCY_CHAR),
	RELIGION_PROF("ReligionProf", Religion.getProficiencyStat(), PROFICIENCY_CHAR),
	SLEIGHT_OF_HAND_PROF("SleightOfHandProf", Sleight_of_Hand.getProficiencyStat(), PROFICIENCY_CHAR),
	STEALTH_PROF("StealthProf", Stealth.getProficiencyStat(), PROFICIENCY_CHAR),
	SURVIVAL_PROF("SurvivalProf", Survival.getProficiencyStat(), PROFICIENCY_CHAR),
	
	PASSIVE_PERCEPTION("Passive1", null, INTEGER),
	PASSIVE_INSIGHT("Passive2", null, INTEGER),
	PASSIVE_INVESTIGATION("Passive3", null, INTEGER),
	
	SENSES("AdditionalSenses", null, STRING),
	
	INITIATIVE("Init", Stat.Initiative, INTEGER),
	
	ARMOR_CLASS("AC", Stat.AC, INTEGER),
	
	PROFICIENCY_BONUS("ProfBonus", Stat.Proficiency_Bonus, INTEGER),
	
	//AbilitySaveDC
	
	MOVEMENT("Speed", null, STRING),
	
	MAX_HP("MaxHP", Stat.Max_HP, INTEGER),
	CURRENT_HP("CurrentHP", Stat.HP, INTEGER),
	TEMPORARY_HP("TempHP", Stat.Temp_HP, INTEGER),
	HIT_DICE("Total", Stat.Hit_Dice, (text) -> {return new Dice(text.toString().replace(" ", ""));}),
	HIT_DICE_REMAINING("HD", null, STRING),
	PROFICIENCIES_AND_LANGUAGES("ProficienciesLang", null, STRING),
	
	COPPER("CP", Stat.Copper, INTEGER),
	SILVER("SP", Stat.Silver, INTEGER),
	ELECTRUM("EP", Stat.Electrum, INTEGER),
	GOLD("GP", Stat.Gold, INTEGER),
	PLATINUM("PP", Stat.Platinum, INTEGER),
	
	SEX("GENDER", null, STRING),
	SIZE("SIZE", null, STRING),
	HEIGHT("HEIGHT", null, STRING),
	WEIGHT("WEIGHT", null, STRING),
	FAITH("FAITH", null, STRING),
	SKIN("SKIN", null, STRING),
	EYES("EYES", null, STRING),
	HAIR("HAIR", null, STRING),
	
	/*
	
	SPELLCASTING_ABILITY("spellCastingAbility", true),
	SPELL_SAVE_DC("spellSaveDC", true),
	SPELL_ATK_BONUS("spellAtkBonus", true),
	SPELL_CASTING_CLASS("spellCastingClass", true),
	 
	 */
	
	//TODO: rest of spell shit
	
	;

	final String key;
	final Stat stat;
	final boolean hasMultiple;
	final Function<PDFText, Object> function;
	
	static enum FunctionType {
			STRING((text) -> {return text.toString();}),
			INTEGER((text) -> {return text.toString().replace("-", "").isEmpty() ? 0 : Integer.parseInt(text.toString().replace(",", ""));}),
			BOOL_NONEMPTY((text) -> {return !text.toString().isBlank();}),
			PROFICIENCY_BOOL((text) -> {return text.toString().isBlank() ? NOT_PROFICIENT : PROFICIENT;}),
			PROFICIENCY_CHAR((text) -> {
				switch(text.toString()) {
					case "":
						return NOT_PROFICIENT;
					case "H":
						return HALF_PROFICIENT;
					case "P":
						return PROFICIENT;
					case "E":
						return EXPERTISE;
					default:
						throw new IllegalArgumentException("Unknown proficiency " + text);
				}
			})
		;
		
		Function<PDFText,Object> function;
		
		FunctionType(Function<PDFText, Object> function) {
			this.function = function;
		}
		
		Function<PDFText,Object> getFunction() {
			return function;
		}
		
	}
	
	DNDBeyondPDFValue(String string, Stat stat, Function<PDFText, Object> parseFunction) {
		this(string, stat, false, parseFunction);
	}
	
	DNDBeyondPDFValue(String string, Stat stat, boolean multiple, Function<PDFText, Object> parseFunction) {
		this.key = string;
		this.stat = stat;
		this.hasMultiple = multiple;
		this.function = parseFunction;
	}
	
	DNDBeyondPDFValue(String string, Stat stat, FunctionType functionType) {
		this(string, stat, functionType.getFunction());
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
	
	public Stat getStat() {
		return stat;
	}
	
	public Object parse(PDFText text) {
		return function.apply(text);
	}
	
	public static DNDBeyondPDFValue getValueType(PDFText text) {
		for(DNDBeyondPDFValue val : values()) {
			if(val.key.equals(text.getName())) {
				return val;
			}
			if(val.hasMultiple) {
				if((text.getName().matches(val.key + "\\d)*"))) {
					return val;
				}
			}
		}
		return null;
	}
	
	public boolean hasMultiple() {
		return hasMultiple;
	}
	
}
