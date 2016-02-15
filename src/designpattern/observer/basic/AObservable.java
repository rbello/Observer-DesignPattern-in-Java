package designpattern.observer.basic;

import java.util.ArrayList;
import java.util.List;

public abstract class AObservable<T> implements IObservable<T> {

	protected List<IObserver<T>> _listeners;

	public AObservable() {
		_listeners = new ArrayList<IObserver<T>>();
	}
	
	@Override
	public void addListener(IObserver<T> observer) {
		if (observer == null)
			throw new NullPointerException();
		if (_listeners.contains(observer))
			throw new IllegalArgumentException();
		_listeners.add(observer);
	}

	@Override
	public void removeListener(IObserver<T> observer) {
		if (observer == null)
			throw new NullPointerException();
		_listeners.remove(observer);
	}

	@Override
	public void notifyListeners(T event) {
		_listeners.forEach((listener) -> listener.onEvent(event));
	}

}
