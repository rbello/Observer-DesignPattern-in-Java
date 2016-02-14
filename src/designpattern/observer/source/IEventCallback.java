package designpattern.observer.source;

public interface IEventCallback<L extends IEventListener> {
	
	public boolean notifyEvent(EventSource<L> source, String eventName, Object... args)
			throws Exception;
	
	public String getMethodName(String eventName);
	
	public Object getTargetObject();

}
