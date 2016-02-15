package designpattern.observer.benchmark;

import designpattern.observer.benchmark.Benchmark.IBenchmarkImplementation;
import designpattern.observer.emitter.BindEvent;
import designpattern.observer.emitter.EventEmitter;
import designpattern.observer.emitter.Events;
import designpattern.observer.emitter.IEventEmitter;
import designpattern.observer.emitter.IListener;

public class ImplEmitter2 implements IBenchmarkImplementation {

	public final IEventEmitter<Double> OnNumberChanged = new EventEmitter<Double>();
	public final IEventEmitter<String> OnVarcharChanged = new EventEmitter<String>();
	
	@Override
	public void initListeners() {
		// # 1
		OnNumberChanged.add(new IListener<Double>() {
			@Override
			public void notify(Double event) {
				Benchmark.count(ImplEmitter2.this);
			}
		});
		// # 2
		IListener<Double> o = new IListener<Double>() {
			@Override
			public void notify(Double event) {
				Benchmark.count(ImplEmitter2.this);
			}
		};
		OnNumberChanged.add(o::notify);
		// # 3
		Events.bind(this, new ExpliciteObserver());
		// # 4
		Events.bind(this, new ImpliciteObserver());
	}

	@Override
	public void notifyListeners(double number, String varchar) {
		OnNumberChanged.notify(number);
		OnVarcharChanged.notify(varchar);
	}
	
	public class ExpliciteObserver {
		@BindEvent
		public void OnVarcharChanged(String e) {
			Benchmark.count(ImplEmitter2.this);
		}
	}
	
	public class ImpliciteObserver {
		@BindEvent({"OnVarcharChanged"})
		public void myMethod(String e) {
			Benchmark.count(ImplEmitter2.this);
		}
	}

	@Override
	public int getObserversCount() {
		return OnVarcharChanged.getListenersCount() + OnNumberChanged.getListenersCount();
	}

}
