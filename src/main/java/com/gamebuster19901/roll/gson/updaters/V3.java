package com.gamebuster19901.roll.gson.updaters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class V3 implements Updaters {

	private static final int VERSION = 3;
	
	private static final String CLASS_TO_UPDATE = "com.gamebuster19901.roll.bot.game.character.Stat$SkillStat";
	private static final String PROF_TO_UPDATE  = "com.gamebuster19901.roll.bot.game.character.Proficiency";
	private static final String INCORRECT_SKILL = "Slight of Hand";
	private static final String   CORRECT_SKILL = "Sleight of Hand";
	private static final String INCORRECT_PROF  = INCORRECT_SKILL + " proficiency";
	private static final String   CORRECT_PROF  = CORRECT_SKILL + " proficiency";
	
	static class UpdaterV3SleightOfHandFixer extends GUpdater {
		public UpdaterV3SleightOfHandFixer() {
			super(VERSION, CLASS_TO_UPDATE);
		}

		@Override
		protected void updateImpl(JsonElement json) {
			if(json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();
				if(obj.get("class") != null) {
					String clazz = obj.get("class").getAsString();
					if(clazz.equals(CLASS_TO_UPDATE)) {
						if(obj.get("name").getAsString().equals(INCORRECT_SKILL)) {
							obj.addProperty("name", CORRECT_SKILL);
						}
					}
				}
			}
		}
	}
	
	static class UpdaterV3SleightOfHandProficiencyFixer extends GUpdater {
		public UpdaterV3SleightOfHandProficiencyFixer() {
			super(VERSION, PROF_TO_UPDATE);
		}

		@Override
		protected void updateImpl(JsonElement json) {
			if(json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();
				if(obj.get("class") != null) {
					String clazz = obj.get("class").getAsString();
					if(clazz.equals(PROF_TO_UPDATE)) {
						if(obj.get("name").getAsString().equals(INCORRECT_PROF)) {
							obj.addProperty("name", CORRECT_PROF);
						}
					}
				}
			}
		}
	}
	
	@Override
	public GUpdater[] getUpdaters() {
		return new GUpdater[] {new UpdaterV3SleightOfHandFixer(), new UpdaterV3SleightOfHandProficiencyFixer()};
	}
	
	@Override
	public int version() {
		return VERSION;
	}

}
