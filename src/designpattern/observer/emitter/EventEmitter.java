package designpattern.observer.emitter;

import java.util.ArrayList;
import java.util.Collection;
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

	@Override
	public Collection<IListener<E>> getListeners() {
		return _listeners;
	}

	@Override
	public boolean hasListeners() {
		return _listeners.size() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IListener<E>> Collection<T> getListeners(Class<T> t) {
		List<T> list = new ArrayList<>();
		for (IListener<E> listener : _listeners) {
			if (t.isInstance(listener))
				list.add((T) listener);
		}
		return list;
	}

	@Override
	public int getListenersCount() {
		return _listeners.size();
	}

}
