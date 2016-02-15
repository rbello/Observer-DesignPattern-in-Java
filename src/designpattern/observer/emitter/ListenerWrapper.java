package designpattern.observer.emitter;

import java.lang.reflect.Method;

import designpattern.observer.emitter.Events.ErrorHandler;

class ListenerWrapper<E> implements IListener<E> {

	private Method _method;
	private Object _listener;
	private ErrorHandler _handler;

	public ListenerWrapper(Method method, Object listener, Events.ErrorHandler handler) {
		this._method = method;
		this._listener = listener;
		this._handler = handler;
	}
	
	@Override
	public void notify(E event) {
		try {
			_method.invoke(_listener, event);
		}
		catch (Throwable ex) {
			if (_handler != null) {
				_handler.handleException(ex, false);
			}
		}
	}
	
	public Object getListenerObject() {
		return _listener;
	}

}
