package com.gamebuster19901.roll.gson;

import java.lang.annotation.Annotation;

/**
 * 
 * Same as {@link Metamorphic}
 * 
 * @
 * 
 * Used so that implementing classes to not have to
 * declare an {@link #annotationType()} implementation
 *
 */

public interface MetamorphicType extends Metamorphic {

	@Override
	public default Class<? extends Annotation> annotationType() {
		return Metamorphic.class;
	}
	
}
