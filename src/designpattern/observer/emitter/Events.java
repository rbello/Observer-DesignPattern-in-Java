package designpattern.observer.emitter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Events {
	
	public static void bind(Object source, Object listener) {
		bind(source, listener);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void bind(Object source, Object listener, ErrorHandler handler) {
		
		try {
		
			for (Method method : listener.getClass().getMethods()) {
				
				BindEvent[] annotations = method.getAnnotationsByType(BindEvent.class);
				if (annotations.length < 1) continue;
				
				for (String eventName : annotations[0].value()) {
					
					if ("".equals(eventName)) eventName = method.getName();
					
					Field field = getEventEmitter(source, eventName);
					
					IEventEmitter emitter = (IEventEmitter) field.get(source);
					
					emitter.add(new IListener() {
						public void notify(Object event) {
							try {
								method.invoke(listener, event);
							} catch (Throwable ex) {
								if (handler != null) handler.handleException(ex);
							}
						}
					});
					
				}
			}
		}
		catch (Throwable ex) {
			if (handler != null) handler.handleException(ex);
		}
		
	}

	private static Field getEventEmitter(Object source, String eventName) {
		
		// On parcours les attributs de l'objet source
		for (Field field : source.getClass().getFields()) {
			
			if (!field.getType().isAssignableFrom(IEventEmitter.class)) continue;
			
			if (!field.getName().equals(eventName)) continue;
			
			return field;
		}
		
		return null;
		
	}
	
	public static interface ErrorHandler {
		
		void handleException(Throwable ex);
		
	}

	public static void unbind(Object... listeners) {
		// TODO Auto-generated method stub
		
	}

}
