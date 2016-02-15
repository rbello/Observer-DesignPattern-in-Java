package designpattern.observer.benchmark;

import designpattern.observer.basic.AObservable;
import designpattern.observer.basic.IObserver;
import designpattern.observer.benchmark.Benchmark.IBenchmarkImplementation;

public class ImplBasic extends AObservable<Event> implements IBenchmarkImplementation {
	
	@Override
	public void initListeners() {
		// # 1
		addListener(new IObserver<Event>() {
			public void onEvent(Event event) {
				Benchmark.count(ImplBasic.this);
			}
		});
		// # 2
		addListener(new IObserver<Event>() {
			public void onEvent(Event event) {
				Benchmark.count(ImplBasic.this);
			}
		});
		// # 3
		addListener(new IObserver<Event>() {
			public void onEvent(Event event) {
				Benchmark.count(ImplBasic.this);
			}
		});
		// # 4
		addListener(new IObserver<Event>() {
			public void onEvent(Event event) {
				Benchmark.count(ImplBasic.this);
			}
		});
	}
	
	@Override
	public void notifyListeners(double number, String varchar) {
		notifyListeners(new Event(number, varchar));
	}

	@Override
	public int getObserversCount() {
		return _listeners.size();
	}
	
}
