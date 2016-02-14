package designpattern.observer.basic;

public interface IObservable<T> {

	public void addListener(IObserver<T> observer);
	
	public void removeListener(IObserver<T> observer);
	
	public void notifyListeners(T event);
	
}
