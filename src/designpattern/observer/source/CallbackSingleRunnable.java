package designpattern.observer.source;

import java.util.logging.Level;

public final class CallbackSingleRunnable<L extends IEventListener> implements IEventCallback<L> {

	private String _eventName;
	private Runnable _runnable;
	
	public CallbackSingleRunnable(String eventName, Runnable runnable) {
		_eventName = eventName;
		_runnable = runnable;
	}
	
	@Override
	public String getMethodName(String eventName) {
		return "run";
	}

	@Override
	public boolean notifyEvent(EventSource<L> source, String eventName, Object... args) {
		if (!_eventName.equals(eventName)) {
			return true;
		}
		// Log
		if (EventSource.LOGGER.isLoggable(Level.FINE)) {
			if (!toString().contains("DebugTreeViewController")) {
				EventSource.LOGGER.log(
						Level.FINE,
						"NOTIFY event " + eventName
						+ " --> TO " + _runnable);
			}
		}
		_runnable.run();
		return true;
	}

	public Runnable getListener() {
		return _runnable;
	}
	
	public String getEventName() {
		return _eventName;
	}

	@Override
	public String toString() {
		return "Runnable " + _runnable.getClass()
			+ " for event '" + _eventName + "' only";
	}

	@Override
	public Object getTargetObject() {
		return _runnable;
	}
	
}
