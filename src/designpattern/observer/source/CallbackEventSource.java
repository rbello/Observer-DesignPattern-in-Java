package designpattern.observer.source;

public final class CallbackEventSource<L extends IEventListener> implements IEventCallback<L> {

	private EventSource<? extends IEventListener> _target;

	public CallbackEventSource(EventSource<? extends IEventListener> target) {
		_target = target;
	}

	@Override
	public boolean notifyEvent(EventSource<L> source, String eventName, Object... args) {
		
		// Log
		/*if (EventSource.LOGGER.isLoggable(IncaLogger.EVENT_NOTIFY)) {
			EventSource.LOGGER.log(
					IncaLogger.EVENT_NOTIFY,
					"REDIRECT event " + eventName
					+ " --> TO " + toString());
		}*/
		
		// Redirection vers la cible
//		synchronized (_target) {
			return _target.trigger(eventName, args);
//		}
		
	}
	
	@Override
	public String toString() {
		return _target + "";
	}

	@Override
	public String getMethodName(String eventName) {
		return "trigger";
	}

	@Override
	public Object getTargetObject() {
		// On n'a pas l'info de qui a réellement envoyé l'event
		return null;
	}

}
