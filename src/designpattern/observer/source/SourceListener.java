package designpattern.observer.source;


public interface SourceListener extends IEventListener {
	
	public void onBind(IEventCallback<?> listener, EventSource<?> source);
	
	public void onUnbind(IEventCallback<?> listener, EventSource<?> source);
	
	public void onUnbindAll(EventSource<?> source);

}
