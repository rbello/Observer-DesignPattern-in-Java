package designpattern.observer.emitter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Classe utilitaire pour manipuler les événements, qui propose principalement la possibilité
 * de binder les événements à partir d'annotations dans le code.
 */
public class Events {

	/**
	 * Interface pour les gestionnaires d'erreurs.
	 */
	public static interface ErrorHandler {
		/**
		 * Quand une exception est levée.
		 * 
		 * @param ex L'exception à traiter.
		 * @param bindingError Indique si l'erreur a eu lieu durant le binding. Si non, c'est lors de la propagation.
		 */
		void handleException(Throwable ex, boolean bindingError);
	}
	
	/**
	 * Aliase de la méthode bind() sans gestionnaire d'erreur.
	 * 
	 * @param source L'objet source levant les événements.
	 * @param listener Le listener.
	 */
	public static void bind(Object source, Object listener) {
		bind(source, listener, null);
	}
	
	/**
	 * Bind l'ensemble des méthodes annotées du listener, sur les events de la source.
	 * Un gestionnaire d'erreur peut être donné, il aura en charge la gestion des
	 * exceptions qui pourraient être levées durant la propagation des events, ou bien
	 * lors du binding (c'est à dire dans cette méthode).
	 * 
	 * @param source L'objet source levant les événements.
	 * @param listener Le listener.
	 * @param handler Le gestionnaire d'erreur.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void bind(Object source, Object listener, ErrorHandler handler) {
		
		// Protection de l'ensemble de la méthode, pour éviter les exceptions liées à la reflexion
		try {
		
			// On parcours les méthodes du listener
			for (Method method : listener.getClass().getMethods()) {
				
				// On vérifie qu'il y ait une annotation de binding
				BindEvent[] annotations = method.getAnnotationsByType(BindEvent.class);
				if (annotations.length < 1) continue;
				
				// On parcours les noms des événements déclarés dans l'annotation
				for (String eventName : annotations[0].value()) {
					
					// Si aucun nom n'est précisé, on réutilise le nom de la méthode pour
					// faire l'association. Dans ce cas, la méthode doit avoir un nom
					// strictement identique à la propriété d'événement sur la source.
					if ("".equals(eventName)) eventName = method.getName();
					
					// On recupère le champ correspondant sur la source
					IEventEmitter emitter = getEventEmitter(source, eventName, true);
					
					// On n'a pas trouvé le champ correspondant à l'event demandé. Ce n'est
					// pas forcément une erreur, car il s'agit peut-être d'un event sur une
					// autre source, donc on ne lève pas d'exception.
					if (emitter == null) {
						continue;
					}
					
					// Et on inscrit un listener, qui va appeler dynamiquement la méthode
					emitter.add(new ListenerWrapper(method, listener, handler));
					
				}
			}
		}
		
		// Les erreurs levées durant le binding
		catch (Throwable ex) {
			// On confie l'erreur au binding
			if (handler != null) {
				handler.handleException(ex, true);
			}
			// Sinon on l'affiche en console
			else {
				System.err.println(String.format("Unable to bind events of %s on %s", source, listener));
				ex.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Retire les listeners donnés des 
	 * 
	 * @param source
	 * @param listeners
	 */
	public static void unbind(Object source, Object... listeners) {
		
		List<Object> objects = Arrays.asList(listeners);
		
		// On se protège des exceptions
		try {
			
			// On parcours l'ensemble des émetteurs qui ont des listeners
			getEventEmitters(source).entrySet().stream()
				.filter((emitter) -> emitter.getValue().hasListeners())
				.forEach((emitter) -> {
				
				// On retire les listeners qui on été wrappé, et qui donc ont été inscrit
				// à l'aide des annotations
				unbindWrapped(emitter, objects);
				
			});
			
		}
		
		// En cas d'erreur, on lève une RuntimeException
		catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static void unbindWrapped(Entry<String, IEventEmitter> emitter, List<Object> objects) {
		// On parcours l'ensemble des listeners sur cet émetteur, qui sont de type Wrapper
		Collection<ListenerWrapper<?>> li = emitter.getValue().getListeners(ListenerWrapper.class);
		for (ListenerWrapper<?> listener : li) {
			// Ce listener doit être enlevé
			if (objects.contains(listener.getListenerObject()) || objects.contains(listener)) {
				// On le retire via l'émetteur
				emitter.getValue().remove(listener);
			}
		}
	}
	
	
	
	/**
	 * Renvoie le champ de type IEventEmitter qui correspond à l'event donné sur l'objet source.
	 * 
	 * @param source
	 * @param eventName
	 * @param caseSensitive
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("rawtypes")
	public static IEventEmitter getEventEmitter(Object source, String eventName, boolean caseSensitive)
			throws IllegalArgumentException, IllegalAccessException {
		// Insensible à la case
		if (!caseSensitive) {
			eventName = eventName.toLowerCase();
		}
		// On parcours les attributs de l'objet source
		for (Field field : source.getClass().getFields()) {
			// On vérifie qu'il soit de type IEventEmitter
			if (!field.getType().isAssignableFrom(IEventEmitter.class)) continue;
			// Et que son nom match
			if (!caseSensitive) {
				if (!field.getName().toLowerCase().equals(eventName)) continue;
			}
			else if (!field.getName().equals(eventName)) continue;
			// Si tout est OK on renvoie le champ
			return (IEventEmitter) field.get(source);
		}
		// On n'a pas trouvé le champ
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, IEventEmitter> getEventEmitters(Object source) throws IllegalArgumentException, IllegalAccessException {
		Map<String, IEventEmitter> emitters = new HashMap<>();
		// On parcours les attributs de l'objet source
		for (Field field : source.getClass().getFields()) {
			// On vérifie qu'il soit de type IEventEmitter
			if (!field.getType().isAssignableFrom(IEventEmitter.class)) continue;
			// Si tout est OK on renvoie le champ
			emitters.put(field.getName(), (IEventEmitter) field.get(source));
		}
		return emitters;
	}
	
}
