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
 * Classe utilitaire pour manipuler les �v�nements, qui propose principalement la possibilit�
 * de binder les �v�nements � partir d'annotations dans le code.
 */
public class Events {

	/**
	 * Interface pour les gestionnaires d'erreurs.
	 */
	public static interface ErrorHandler {
		/**
		 * Quand une exception est lev�e.
		 * 
		 * @param ex L'exception � traiter.
		 * @param bindingError Indique si l'erreur a eu lieu durant le binding. Si non, c'est lors de la propagation.
		 */
		void handleException(Throwable ex, boolean bindingError);
	}
	
	/**
	 * Aliase de la m�thode bind() sans gestionnaire d'erreur.
	 * 
	 * @param source L'objet source levant les �v�nements.
	 * @param listener Le listener.
	 */
	public static void bind(Object source, Object listener) {
		bind(source, listener, null);
	}
	
	/**
	 * Bind l'ensemble des m�thodes annot�es du listener, sur les events de la source.
	 * Un gestionnaire d'erreur peut �tre donn�, il aura en charge la gestion des
	 * exceptions qui pourraient �tre lev�es durant la propagation des events, ou bien
	 * lors du binding (c'est � dire dans cette m�thode).
	 * 
	 * @param source L'objet source levant les �v�nements.
	 * @param listener Le listener.
	 * @param handler Le gestionnaire d'erreur.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void bind(Object source, Object listener, ErrorHandler handler) {
		
		// Protection de l'ensemble de la m�thode, pour �viter les exceptions li�es � la reflexion
		try {
		
			// On parcours les m�thodes du listener
			for (Method method : listener.getClass().getMethods()) {
				
				// On v�rifie qu'il y ait une annotation de binding
				BindEvent[] annotations = method.getAnnotationsByType(BindEvent.class);
				if (annotations.length < 1) continue;
				
				// On parcours les noms des �v�nements d�clar�s dans l'annotation
				for (String eventName : annotations[0].value()) {
					
					// Si aucun nom n'est pr�cis�, on r�utilise le nom de la m�thode pour
					// faire l'association. Dans ce cas, la m�thode doit avoir un nom
					// strictement identique � la propri�t� d'�v�nement sur la source.
					if ("".equals(eventName)) eventName = method.getName();
					
					// On recup�re le champ correspondant sur la source
					IEventEmitter emitter = getEventEmitter(source, eventName, true);
					
					// On n'a pas trouv� le champ correspondant � l'event demand�. Ce n'est
					// pas forc�ment une erreur, car il s'agit peut-�tre d'un event sur une
					// autre source, donc on ne l�ve pas d'exception.
					if (emitter == null) {
						continue;
					}
					
					// Et on inscrit un listener, qui va appeler dynamiquement la m�thode
					emitter.add(new ListenerWrapper(method, listener, handler));
					
				}
			}
		}
		
		// Les erreurs lev�es durant le binding
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
	 * Retire les listeners donn�s des 
	 * 
	 * @param source
	 * @param listeners
	 */
	public static void unbind(Object source, Object... listeners) {
		
		List<Object> objects = Arrays.asList(listeners);
		
		// On se prot�ge des exceptions
		try {
			
			// On parcours l'ensemble des �metteurs qui ont des listeners
			getEventEmitters(source).entrySet().stream()
				.filter((emitter) -> emitter.getValue().hasListeners())
				.forEach((emitter) -> {
				
				// On retire les listeners qui on �t� wrapp�, et qui donc ont �t� inscrit
				// � l'aide des annotations
				unbindWrapped(emitter, objects);
				
			});
			
		}
		
		// En cas d'erreur, on l�ve une RuntimeException
		catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static void unbindWrapped(Entry<String, IEventEmitter> emitter, List<Object> objects) {
		// On parcours l'ensemble des listeners sur cet �metteur, qui sont de type Wrapper
		Collection<ListenerWrapper<?>> li = emitter.getValue().getListeners(ListenerWrapper.class);
		for (ListenerWrapper<?> listener : li) {
			// Ce listener doit �tre enlev�
			if (objects.contains(listener.getListenerObject()) || objects.contains(listener)) {
				// On le retire via l'�metteur
				emitter.getValue().remove(listener);
			}
		}
	}
	
	
	
	/**
	 * Renvoie le champ de type IEventEmitter qui correspond � l'event donn� sur l'objet source.
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
		// Insensible � la case
		if (!caseSensitive) {
			eventName = eventName.toLowerCase();
		}
		// On parcours les attributs de l'objet source
		for (Field field : source.getClass().getFields()) {
			// On v�rifie qu'il soit de type IEventEmitter
			if (!field.getType().isAssignableFrom(IEventEmitter.class)) continue;
			// Et que son nom match
			if (!caseSensitive) {
				if (!field.getName().toLowerCase().equals(eventName)) continue;
			}
			else if (!field.getName().equals(eventName)) continue;
			// Si tout est OK on renvoie le champ
			return (IEventEmitter) field.get(source);
		}
		// On n'a pas trouv� le champ
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, IEventEmitter> getEventEmitters(Object source) throws IllegalArgumentException, IllegalAccessException {
		Map<String, IEventEmitter> emitters = new HashMap<>();
		// On parcours les attributs de l'objet source
		for (Field field : source.getClass().getFields()) {
			// On v�rifie qu'il soit de type IEventEmitter
			if (!field.getType().isAssignableFrom(IEventEmitter.class)) continue;
			// Si tout est OK on renvoie le champ
			emitters.put(field.getName(), (IEventEmitter) field.get(source));
		}
		return emitters;
	}
	
}
