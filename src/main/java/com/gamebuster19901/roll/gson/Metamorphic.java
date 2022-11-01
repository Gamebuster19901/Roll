package com.gamebuster19901.roll.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Represents that the annotated object or implementing class
 * at compile time is not always represented by the class at runtime.
 * <p>
 * This must be serialized with its class name
 * in order to be deserialized into the correct class.
 * <p>
 * If annotating a field, then this field may be one of many types
 * at runtime.
 * <p>
 * If annotating (or being implemented by) a class, then
 * any field at compile time which has its type derived from
 * the annotated class may be of many types at runtime.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Metamorphic {}
