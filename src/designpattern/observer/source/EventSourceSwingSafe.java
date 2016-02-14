package designpattern.observer.source;

import javax.swing.SwingUtilities;

/**
 * Une source d'event permettant le trigger asynchrone
 *
 * @param <E>
 */
public class EventSourceSwingSafe<E extends IEventListener> extends EventSourceAsynch<E> {

	public EventSourceSwingSafe() {
		super();
	}

	public EventSourceSwingSafe(Object sender) {
		super(sender);
	}
	
	public EventSourceSwingSafe(Class<? extends IEventListener> listenerClass) {
		super(listenerClass);
	}

	public EventSourceSwingSafe(Class<? extends IEventListener> listenerClass,
			Object sender) {
		super(listenerClass, sender);
	}
	
	public void triggerSafe(final String eventName, final Object... args) {
		
		// On est dans le thread de propagation d'event de swing.
		// Il ne faut pas le bloquer si on veut garder une interface fluide.
		// On détecte ici si on est dans ce thread.
		if (SwingUtilities.isEventDispatchThread()) {
			triggerAsynch(eventName, args);
		}
		
		// Lancement synchrone
		else {
			trigger(eventName, args);
		}
		
	}

}
