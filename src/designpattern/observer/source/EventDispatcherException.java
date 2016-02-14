package designpattern.observer.source;

public class EventDispatcherException extends RuntimeException {

	public EventDispatcherException(EventSource<?> src, IEventCallback<?> listener, String eventName, Throwable ex) {
		super(ex.getClass().getSimpleName() + " during event" +
				"propagation '" + eventName + "' from: " + listener, ex);
	}

	private static final long serialVersionUID = 1L;

}
