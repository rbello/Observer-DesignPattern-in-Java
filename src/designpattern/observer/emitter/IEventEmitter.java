package designpattern.observer.emitter;

public interface IEventEmitter<E> {

	void notify(E event);

	void add(IListener<E> listener);
	
	void remove(IListener<E> listener);

}
