package designpattern.observer.basic;

import java.util.ArrayList;
import java.util.List;

public abstract class AObservable<T> implements IObservable<T> {

	private List<IObserver<T>> listeners;

	public AObservable() {
		this.listeners = new ArrayList<IObserver<T>>();
	}
	
	@Override
	public void addListener(IObserver<T> observer) {
		if (observer == null)
			throw new NullPointerException();
		if (this.listeners.contains(observer))
			throw new IllegalArgumentException();
		this.listeners.add(observer);
	}

	@Override
	public void removeListener(IObserver<T> observer) {
		if (observer == null)
			throw new NullPointerException();
		this.listeners.remove(observer);
	}

	@Override
	public void notifyListeners(T event) {
		this.listeners.forEach((listener) -> listener.onEvent(event));
	}

}
