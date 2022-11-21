package com.gamebuster19901.roll.gson.updaters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class V2 implements Updaters {

	private static final int VERSION = 2;
	
	private static final String OLD_ABILITY_STAT_CLASS = "com.gamebuster19901.roll.bot.game.character.Stat$AbilityStat";
	private static final String NEW_ABILITY_STAT_CLASS = "com.gamebuster19901.roll.bot.game.character.Stat$AbilityScoreStat";
	
	static class UpdaterV2ClassRename extends GUpdater {
		
		public UpdaterV2ClassRename() {
			super(VERSION, OLD_ABILITY_STAT_CLASS);
		}

		@Override
		protected void updateImpl(JsonElement json) {
			if(json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();
				if(obj.get("class") != null) {
					String oldClass = obj.get("class").getAsString();
					if (oldClass.equals(OLD_ABILITY_STAT_CLASS)) {
						obj.addProperty("class", NEW_ABILITY_STAT_CLASS);
						System.out.println("com.gamebuster19901.roll.bot.game.character.Stat$AbilityStat -> com.gamebuster19901.roll.bot.game.character.Stat$AbilityScoreStat");
					}
				}
			}
		}

	}
	
	static class UpdaterV2AbilityModSwap extends GUpdater {

		public UpdaterV2AbilityModSwap() {
			super(
				VERSION, 
				OLD_ABILITY_STAT_CLASS //Should have already been converted to new type
			);
		}

		@Override
		protected void updateImpl(JsonElement json) {
			if(json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();
				if(obj.get("class") != null) {
					String oldClass = obj.get("class").getAsString();
					if (oldClass.equals(OLD_ABILITY_STAT_CLASS)) {
						String statName = obj.get("name").getAsString();
						obj.addProperty("name", statName + " Score");
						System.out.println(statName + " -> " + statName + " Score");
					}
				}
			}
		}
		
		public boolean accepts(String clazz) {
			return clazzes.contains(clazz);
		}
		
	}

	@Override
	public GUpdater[] getUpdaters() {
		return new GUpdater[] {new UpdaterV2ClassRename(), new UpdaterV2AbilityModSwap()};
	}

	@Override
	public int version() {
		return VERSION;
	}

	
}
