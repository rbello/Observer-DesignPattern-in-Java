package designpattern.observer.emitter;

import java.util.Collection;

public interface IEventEmitter<E> {

	void notify(E event);

	void add(IListener<E> listener);
	
	void remove(IListener<E> listener);

	Collection<IListener<E>> getListeners();
	
	<T extends IListener<E>> Collection<T> getListeners(Class<T> t);

	boolean hasListeners();

}
