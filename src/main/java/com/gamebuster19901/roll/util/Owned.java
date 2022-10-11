package com.gamebuster19901.roll.util;

public interface Owned<O extends Named> extends Named {

	public O getOwner();
	
	public default String getOwnershipString() {
		return getName() + "(" + getOwner().getName() + ")";
	}
	
}
