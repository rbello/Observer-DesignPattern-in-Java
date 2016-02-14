package designpattern.observer.source;

import java.util.logging.Level;

import designpattern.observer.dispatcher.IListener;

public final class CallbackMultipleEventIListener<L extends IEventListener> implements IEventCallback<L> {

	private IListener<String> _listener;

	public CallbackMultipleEventIListener(IListener<String> listener) {
		_listener = listener;
	}

	@Override
	public boolean notifyEvent(EventSource<L> source, String event, Object... args) {
		
		// Log
		if (EventSource.LOGGER.isLoggable(Level.FINE)) {
			if (!toString().contains("DebugTreeViewController")) {
				EventSource.LOGGER.log(
						Level.FINE,
						"NOTIFY event " + event
						+ " --> TO " + toString());
			}
		}
		
		// Propagation au listener
		return _listener.notifyEvent(event, args);
		
	}
	
	@Override
	public String toString() {
		return "(IListener)" + _listener.getClass();
	}

	public IListener<String> getListener() {
		return _listener;
	}

	@Override
	public String getMethodName(String eventName) {
		return "notifyEvent";
	}

	@Override
	public Object getTargetObject() {
		return _listener;
	}

}
