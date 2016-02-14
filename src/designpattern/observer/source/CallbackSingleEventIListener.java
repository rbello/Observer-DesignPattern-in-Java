package designpattern.observer.source;

import java.util.logging.Level;

import designpattern.observer.dispatcher.IListener;

public final class CallbackSingleEventIListener<L extends IEventListener> implements IEventCallback<L> {

	private String _eventName;
	private IListener<String> _listener;

	public CallbackSingleEventIListener(String eventName, IListener<String> listener) {
		_eventName = eventName;
		_listener = listener;
	}

	@Override
	public boolean notifyEvent(EventSource<L> source, String event, Object... args) {
		
		// Filtrage
		if (!_eventName.equals(event)) {
			return true;
		}
		
		// Log
		if (EventSource.LOGGER.isLoggable(Level.FINE)) {
			if (!toString().contains("DebugTreeViewController")) {
				EventSource.LOGGER.log(
						Level.FINE,
						"NOTIFY event " + event
						+ " --> TO " + toString());
			}
		}
		
		return _listener.notifyEvent(event, args);
		
	}
	
	@Override
	public String toString() {
		return "IListener '" + _listener.getClass() + "' for event '"+_eventName+"' only";
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
