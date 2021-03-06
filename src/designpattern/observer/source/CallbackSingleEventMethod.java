package designpattern.observer.source;

import java.lang.reflect.Method;
import java.util.logging.Level;

public final class CallbackSingleEventMethod<L extends IEventListener> implements IEventCallback<L> {

	private String _eventName;
	private Object _target;
	private String _targetMethodName;

	public CallbackSingleEventMethod(String eventName, Object target, String targetMethodName) {
		_eventName = eventName;
		_target = target;
		_targetMethodName = targetMethodName;
	}

	@Override
	public boolean notifyEvent(EventSource<L> source, String event, Object... args) throws Exception {
		
		// Filtre
		if (!_eventName.equals(event)) {
			return true;
		}
		
		// Fetch methods
		for (Method m : _target.getClass().getMethods()) {
			
			// Name mismatch
			if (!m.getName().equals(_targetMethodName)) {
				continue;
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
			
			// On adapte le nombre d'argument à la méthode
			int length = m.getParameterTypes().length;
			if (length != args.length) {
				Object[] copy = new Object[length];
				for (int i = 0; i < length; i++) {
					copy[i] = args[i];
				}
				args = copy;
				copy = null;
			}
			
			try {
				
				// Pour permetre l'invocation d'une méthode dans une classe anonyme
				m.setAccessible(true);
				
				// Si la méthode renvoie une boolean, on s'en sert pour le retour
				if (m.getReturnType().toString().equals("boolean") || m.getReturnType() == Boolean.class) {
					return (Boolean) m.invoke(_target, args);
				}
				// Sinon on fait juste l'invocation, et on renvera TRUE à la fin
				else {
					m.invoke(_target, args);
					return true;
				}
				
			}
			
			// Invoked method arguments error
			catch (IllegalArgumentException e) {
				System.err.println("Method broadcast exception: '" + e.getMessage() + "' for event " + event
						+ " to " + _target.getClass().getSimpleName() + "." + _targetMethodName + "()");
				StringBuilder sb = new StringBuilder();
				sb.append("  Event args: ");
				for (int i = 0, l = args.length; i < l; i++) {
					if (i > 0) sb.append(", ");
					sb.append(args[i] == null ? "NULL" : args[i].getClass().getSimpleName());
				}
				System.err.println(sb.toString());
				sb = new StringBuilder();
				sb.append("  Method:     ");
				sb.append(_target.getClass().getSimpleName());
				sb.append('.');
				sb.append(_targetMethodName);
				sb.append('(');
				{
					int i = 0;
					for (Class<?> type : m.getParameterTypes()) {
						if (i++ > 0) sb.append(", ");
						sb.append(type.getSimpleName());
					}
				}
				sb.append(')');
				if (e.getCause() != null) {
					sb.append("\n  Cause: ");
					sb.append(e.getCause().getMessage());
				}
				System.err.println(sb.toString());
				throw e;
			}
			
		}
		
		return true;
	
	}
	
	@Override
	public String toString() {
		return "Method " + _target.getClass().getSimpleName()+"."+_targetMethodName+"()"
			+ " for event '" + _eventName + "' only";
	}

	@Override
	public String getMethodName(String eventName) {
		return _targetMethodName;
	}

	public String getEventName() {
		return _eventName;
	}

	public Object getTargetObject() {
		return _target;
	}

	public String getTargetMethodName() {
		return _targetMethodName;
	}

}
