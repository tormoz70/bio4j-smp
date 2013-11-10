package bio4j.common.types;


public interface DelegateCheck<T> {
	boolean callback(T object);
}
