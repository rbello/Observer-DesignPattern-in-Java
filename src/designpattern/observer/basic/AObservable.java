package designpattern.observer.basic;

import java.util.ArrayList;
import java.util.List;

public abstract class AObservable<T> implements IObservable<T> {

	private List<IObserver<T>> _listeners;

	public AObservable() {
		this._listeners = new ArrayList<IObserver<T>>();
	}
	
	@Override
	public void addListener(IObserver<T> observer) {
		if (observer == null)
			throw new NullPointerException();
		if (this._listeners.contains(observer))
			throw new IllegalArgumentException();
		this._listeners.add(observer);
	}

	@Override
	public void removeListener(IObserver<T> observer) {
		if (observer == null)
			throw new NullPointerException();
		this._listeners.remove(observer);
	}

	@Override
	public void notifyListeners(T event) {
		this._listeners.forEach((listener) -> listener.onEvent(event));
	}

}
