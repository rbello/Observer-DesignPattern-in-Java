package designpattern.observer.emitter;

import java.util.ArrayList;
import java.util.List;

public class EventEmitter<E> implements IEventEmitter<E> {
	
	private List<IListener<E>> _listeners = new ArrayList<>();

	@Override
	public void notify(E event) {
		_listeners.forEach((listener) -> listener.notify(event));
	}

	@Override
	public void add(IListener<E> listener) {
		_listeners.add(listener);
	}

	@Override
	public void remove(IListener<E> listener) {
		_listeners.remove(listener);
	}
	
	public void dispose() {
		_listeners = null;
	}

}
