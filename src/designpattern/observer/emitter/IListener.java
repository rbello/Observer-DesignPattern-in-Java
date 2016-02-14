package designpattern.observer.emitter;

@FunctionalInterface
public interface IListener<E> {

	public void notify(E event);
	
}
