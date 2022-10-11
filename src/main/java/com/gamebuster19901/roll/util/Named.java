package com.gamebuster19901.roll.util;

public interface Named extends Identified {

	public String getName();
	
	public default String getIdentifierName() {
		return getName() + "(" + getID() + ")";
	}
	
	public default String getLookingForMatch() {
		return getName() + " " + getID();
	}
	
	public default boolean matches(String lookingFor) {
		return getName().equalsIgnoreCase(lookingFor) || lookingFor.equals(((Long)getID()).toString()) || getIdentifierName().equalsIgnoreCase(lookingFor) 
				|| getLookingForMatch().equalsIgnoreCase(lookingFor);
	}
	
}
