package designpattern.observer.benchmark;

import designpattern.observer.benchmark.Benchmark.IBenchmarkImplementation;
import designpattern.observer.emitter.BindEvent;
import designpattern.observer.emitter.EventEmitter;
import designpattern.observer.emitter.Events;
import designpattern.observer.emitter.IEventEmitter;
import designpattern.observer.emitter.IListener;

public class ImplEmitter implements IBenchmarkImplementation {

	public final IEventEmitter<Event> OnChanged = new EventEmitter<Event>();
	
	@Override
	public void initListeners() {
		// # 1
		OnChanged.add(new IListener<Event>() {
			@Override
			public void notify(Event event) {
				Benchmark.count(ImplEmitter.this);
			}
		});
		// # 2
		IListener<Event> o = new IListener<Event>() {
			@Override
			public void notify(Event event) {
				Benchmark.count(ImplEmitter.this);
			}
		};
		OnChanged.add(o::notify);
		// # 3
		Events.bind(this, new ExpliciteObserver());
		// # 4
		Events.bind(this, new ImpliciteObserver());
	}

	@Override
	public void notifyListeners(double number, String varchar) {
		OnChanged.notify(new Event(number, varchar));
	}
	
	public class ExpliciteObserver {
		@BindEvent
		public void OnChanged(Event e) {
			Benchmark.count(ImplEmitter.this);
		}
	}
	
	public class ImpliciteObserver {
		@BindEvent({"OnChanged"})
		public void myMethod(Event e) {
			Benchmark.count(ImplEmitter.this);
		}
	}

	@Override
	public int getObserversCount() {
		return OnChanged.getListenersCount();
	}

}
