package ru.bio4j.smp.common.types;


public interface DelegateCheck<T> {
	boolean callback(T object);
}
