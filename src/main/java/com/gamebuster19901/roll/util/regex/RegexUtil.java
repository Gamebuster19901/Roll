package com.gamebuster19901.roll.util.regex;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	private static final Field GROUP_FIELD;
	static {
		try {
			GROUP_FIELD = Pattern.class.getDeclaredField("namedGroups");
			GROUP_FIELD.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			throw new AssertionError(e);
		}
	}
	
	public static final boolean groupExists(String group, Matcher matcher) {
		return groupExists(group, matcher.pattern());
	}
	
	public static final boolean groupExists(String group, Pattern pattern) {
		try {
			return ((Map<String, Integer>)GROUP_FIELD.get(pattern)).containsKey(group);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new AssertionError(e);
		}
	}
	
}
