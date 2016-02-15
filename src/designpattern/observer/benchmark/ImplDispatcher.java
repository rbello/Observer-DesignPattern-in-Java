package designpattern.observer.benchmark;

import designpattern.observer.benchmark.Benchmark.IBenchmarkImplementation;
import designpattern.observer.dispatcher.EventDispatcher;
import designpattern.observer.dispatcher.IEventDispatcher;
import designpattern.observer.dispatcher.IListener;
import designpattern.observer.dispatcher.IObservable;

public class ImplDispatcher implements IBenchmarkImplementation, IObservable<String> {

	private IEventDispatcher<String> _dispatcher = new EventDispatcher<String>();
	
	@Override
	public void initListeners() {
		// # 1
		events().bind("DataRead", new IListener<String>() {
			public boolean notifyEvent(String event, Object... args) {
				Benchmark.count(ImplDispatcher.this);
				return true;
			}
		});
		// # 2
		events().bind("DataRead", new Handler(), "notified");
		// # 3
		events().bind("DataRead", new IListener<String>() {
			public boolean notifyEvent(String event, Object... args) {
				Benchmark.count(ImplDispatcher.this);
				return true;
			}
		});
		// # 4
		events().bind("DataRead", new Handler(), "notified");
	}

	@Override
	public void notifyListeners(double number, String varchar) {
		_dispatcher.trigger("DataRead", number, varchar);
	}

	@Override
	public IEventDispatcher<String> events() {
		return _dispatcher;
	}
	
	public class Handler {
		public void notified(Double number, String varchar) {
			Benchmark.count(ImplDispatcher.this);
		}
	}

	@Override
	public int getObserversCount() {
		return _dispatcher.getObserversCount();
	}

}
