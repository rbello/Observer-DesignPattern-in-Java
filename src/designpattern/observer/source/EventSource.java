package designpattern.observer.source;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import designpattern.observer.dispatcher.IListener;

/**
 * @param <L> L'interface des listeners pour cette source.
 */
public class EventSource<L extends IEventListener> {
	
	/**
	 * Logger
	 */
	public static final Logger LOGGER = Logger.getLogger("Events");
	
	/**
	 * Liste des listeners.
	 */
	private List<IEventCallback<L>> _listeners = new ArrayList<IEventCallback<L>>();
	
	/**
	 * Source d'event interne pour signaler les ajouts ou suppressions de listeners,
	 * ainsi que les broadcast. Cette variable est laissée à NULL si l'objet sender
	 * est déjà une EventSource, afin d'éviter les problèmes de récursivité.
	 */
	private EventSource<SourceListener> _sourceEvents;

	/**
	 * L'objet qui envoie les messages. Les sources sont destinées à être
	 * utilisées en créant des attributs membres, on stockera ici l'instance
	 * de l'objet qui a les attributs.  
	 */
	private Object _sender;

	/**
	 * La classe du listener associé à cette source d'event.
	 */
	private Class<? extends IEventListener> _listenerClass;

	/**
	 * Le status activé.
	 */
	private boolean enabled = true;
	
	public EventSource() {
		this(IAnonymousListener.class, null);
	}
	
	public EventSource(Class<? extends IEventListener> listenerClass) {
		this(listenerClass, null);
	}
	
	public EventSource(Object sender) {
		this(IAnonymousListener.class, sender);
	}
	
	/**
	 * Constructeur.
	 * 
	 * @param sender L'envoyeur des messages, l'objet qui a la source. 
	 */
	public EventSource(Class<? extends IEventListener> listenerClass, Object sender) {
		
		// On ajoute une source pour pouvoir être informé des évolutions de cette
		// source, avec un controle de redondance cyclique.
		if (sender != null && !(sender instanceof EventSource)) {
			_sourceEvents = new EventSource<SourceListener>(this) {
				@Override
				public Class<? extends IEventListener> getListenerClass() {
					return SourceListener.class;
				}
			};
		}
		
		// On sauvegarde les références
		_listenerClass = listenerClass;
		_sender = sender;
		
	}
	
	/**
	 * Cette méthode permet de renvoyer la classe de l'interface des 
	 * listeners de cette source.
	 * 
	 * @return L'interface des listeners de la source, le type paramètré L.
	 */
	public Class<? extends IEventListener> getListenerClass() {
		return _listenerClass;
	}
	
	/**
	 * Enregistre un listener qui écoute tous les events de cette source.
	 * 
	 * @param listener Le listener à inscrire.
	 */
	@SuppressWarnings("unchecked")
	public <M extends IEventListener> EventSource<L> bind(M listener) {
		
		// Désactivé
		if (!enabled) {
			return this;
		}
		
		// Méfiance...
		if (listener == null) {
			throw new NullPointerException("listener is null");
		}
		
		// Log
		if (LOGGER.isLoggable(Level.FINE) && !getListenerClass().getSimpleName().equals("SourceListener")) {
			LOGGER.log(Level.FINE, "BIND event listener "
					+ listener.getClass().getSimpleName() + " --> TO "
					+ toString());
		}
		
		// On fabrique un CallbackEventListener
		IEventCallback<M> callback = new CallbackEventListener<M>(
				listener,
				getListenerClass().getSimpleName()
		);
		
		// Ajout dans la liste des listeners
		synchronized (this) {
			_listeners.add((IEventCallback<L>) callback);
		}
		
		// Propagation aux events internes
		if (_sourceEvents != null) {
			_sourceEvents.trigger("onBind", callback, this);
		}
		
		return this;
		
	}
	
	/**
	 * Associe un type d'event en particulier à une méthode d'un objet.
	 * 
	 * @param eventName Le nom de l'event à associer.
	 * @param target L'instance de l'objet cible.
	 * @param targetMethodName Le nom de la méthode à appeler.
	 */
	public EventSource<L> bind(String eventName, Object target, String targetMethodName) {
		
		// Désactivé
		if (!enabled) {
			return this;
		}
		
		// Méfiance
		if (eventName == null || target == null || targetMethodName == null) {
			throw new NullPointerException("");
		}
		
		// Log
		if (LOGGER.isLoggable(Level.FINE) && !getListenerClass().getSimpleName().equals("SourceListener")) {
			LOGGER.log(Level.FINE, "BIND method "
					+ target.getClass().getSimpleName() + "." + targetMethodName + "() --> TO "
					+ toString() + "." + eventName);
		}
		
		// On fabrique un CallbackSingleEventMethod
		IEventCallback<L> callback = new CallbackSingleEventMethod<L>(eventName, target, targetMethodName);
		
		// Ajout dans la liste des listeners
		synchronized (this) {
			_listeners.add(callback);
		}
		
		// Propagation aux events internes
		if (_sourceEvents != null) {
			_sourceEvents.trigger("onBind", callback, this);
		}
		
		return this;
		
	}
	
	public EventSource<L> bind(String eventName, Runnable target) {
		
		// Désactivé
		if (!enabled) {
			return this;
		}
		
		// Méfiance
		if (eventName == null || target == null) {
			throw new NullPointerException("");
		}
		
		// Log
		if (LOGGER.isLoggable(Level.FINE) && !getListenerClass().getSimpleName().equals("SourceListener")) {
			LOGGER.log(Level.FINE, "BIND runnable "
					+ target + " --> TO "
					+ toString() + "." + eventName);
		}
		
		// On fabrique un CallbackSingleEventMethod
		IEventCallback<L> callback = new CallbackSingleRunnable<L>(eventName, target);
		
		// Ajout dans la liste des listeners
		synchronized (this) {
			_listeners.add(callback);
		}
		
		// Propagation aux events internes
		if (_sourceEvents != null) {
			_sourceEvents.trigger("onBind", callback, this);
		}
		
		return this;
		
	}
	
	/**
	 * Associe un IListener à tous les events de cette source.
	 * 
	 * @param listener Le listener à associer.
	 */
	public EventSource<L> bindAll(IListener<String> listener) {
		
		// Désactivé
		if (!enabled) {
			return this;
		}
		
		// Méfiance...
		if (listener == null) {
			throw new NullPointerException("listener is null");
		}
		
		// Log
		if (LOGGER.isLoggable(Level.FINE) && !getListenerClass().getSimpleName().equals("SourceListener")) {
			LOGGER.log(Level.FINE, "BIND (IListener) "
					+ listener.getClass().getSimpleName() + " --> TO "
					+ toString() + ".*");
		}
		
		// On fabrique un CallbackMultipleEventIListener
		IEventCallback<L> callback = new CallbackMultipleEventIListener<L>(listener);
		
		// Ajout dans la liste des listeners
		synchronized (this) {
			_listeners.add(callback);
		}
		
		// Propagation aux events internes
		if (_sourceEvents != null) {
			_sourceEvents.trigger("onBind", callback, this);
		}
		
		return this;
	}
	
	/**
	 * Associer un IListener à un event en particulier.
	 * 
	 * @param eventName Le nom de l'event.
	 * @param listener L'instance du listener.
	 */
	public EventSource<L> bind(String eventName, IListener<String> listener) {
		
		// Désactivé
		if (!enabled) {
			return this;
		}
		
		// Méfiance...
		if (eventName == null || listener == null) {
			throw new NullPointerException("");
		}
		
		// Log
		if (LOGGER.isLoggable(Level.FINE) && !getListenerClass().getSimpleName().equals("SourceListener")) {
			LOGGER.log(Level.FINE, "BIND listener "
					+ listener.getClass() + " --> TO "
					+ toString() + "." + eventName);
		}
		
		// On fabrique un CallbackSingleEventIListener
		IEventCallback<L> callback = new CallbackSingleEventIListener<L>(eventName, listener);
		
		// Ajout dans la liste des listeners
		synchronized (this) {
			_listeners.add(callback);
		}
		
		// Propagation aux events internes
		if (_sourceEvents != null) {
			_sourceEvents.trigger("onBind", callback, this);
		}
		
		return this;
		
	}

	/**
	 * Alias de redirect()
	 */
	public EventSource<L> bind(EventSource<? extends IEventListener> target) {
		redirect(target);
		return this;
	}
	
	/**
	 * Redirige tous les events de cette source vers une autre.
	 * 
	 * @param target La source qui ecoute les events de celle-ci.
	 */
	public void redirect(EventSource<? extends IEventListener> target) {
		
		// Désactivé
		if (!enabled) {
			return;
		}
		
		// Méfiance...
		if (target == null) {
			throw new NullPointerException("target is null");
		}
		
		// Méfiance encore plus...
		if (target == this || target == _sourceEvents) {
			throw new SecurityException("Cyclic Redundancy Check");
		}
		
		// Log
		if (LOGGER.isLoggable(Level.FINE) && !getListenerClass().getSimpleName().equals("SourceListener")) {
			LOGGER.log(Level.FINE, "BIND source "
					+ target + " --> TO "
					+ toString() + ".*");
		}
		
		// On fabrique un CallbackEventSource
		IEventCallback<L> callback = new CallbackEventSource<L>(target);
		
		// Ajout dans la liste des listeners
		synchronized (this) {
			_listeners.add(callback);
		}
		
		// Propagation aux events internes
		if (_sourceEvents != null) {
			_sourceEvents.trigger("onBind", callback, this);
		}
		
	}
	
	/**
	 * Enlève tous les listeners associés à cette source.
	 */
	public void unbind() {
		
		synchronized (this) {
			
			// Méfiance...
			if (_listeners == null) {
				return;
			}
			
			// On supprime tous les listeners
			_listeners.clear();
		
		}
		
		// Propagation aux events internes
		if (_sourceEvents != null) {
			_sourceEvents.trigger("onUnbindAll", this);
		}
		
	}
	
	/**
	 * Désinscrire un listener.
	 */
	public void unbind(L listener) {
		
		// Désactivé
		if (!enabled) {
			return;
		}
		
		// Méfiance
		if (listener == null) {
			throw new NullPointerException("listener is null");
		}
		
		// Liste des listeners retirés
		List<IEventCallback<L>> removed = new ArrayList<IEventCallback<L>>();
		
		synchronized (this) {
		
			// On parcours les listeners
			for (IEventCallback<L> c : new ArrayList<IEventCallback<L>>(_listeners)) {
				
				// Uniquement pour les CallbackEventListener
				if (c instanceof CallbackEventListener) {
					if (((CallbackEventListener<?>) c).getListener() == listener) {
						// On retire le listener
						_listeners.remove(c);
						// On conserve l'instance dans la liste des listeners retirés
						removed.add(c);
					}
				}
				
			}
		
		}
		
		// Si on a retiré des listeners, on va le propager
		if (removed.size() > 0 && _sourceEvents != null) {
			for (IEventCallback<L> c : removed) {
				_sourceEvents.trigger("onUnbind", c, this);
			}
		}
		
	}
	
	public void unbind(String eventName, Object target, String targetMethodName) {
		
		// Désactivé
		if (!enabled) {
			return;
		}
		
		// Liste des listeners retirés
		List<IEventCallback<L>> removed = new ArrayList<IEventCallback<L>>();
		
		synchronized (this) {
		
			// On parcours les listeners
			for (IEventCallback<L> c : new ArrayList<IEventCallback<L>>(_listeners)) {
				
				// Uniquement pour les CallbackSingleEventMethod
				if (!(c instanceof CallbackSingleEventMethod)) {
					continue;
				}
				
				// Cast de la callback
				final CallbackSingleEventMethod<L> callback = (CallbackSingleEventMethod<L>) c;
				
				// Filtre sur l'eventName
				if (eventName != null && !eventName.equals(callback.getEventName())) {
					continue;
				}
				
				// Filtre sur la target
				if (target != null && target != callback.getTargetObject()) {
					continue;
				}
				
				// Filtre sur la targetMethodName
				if (targetMethodName != null && !targetMethodName.equals(callback.getTargetMethodName())) {
					continue;
				}
				
				// On retire le listener
				_listeners.remove(c);
				
				// On conserve l'instance dans la liste des listeners retirï¿½s
				removed.add(c);
				
			}
		
		}
		
		// Si on a retirï¿½ des listeners, on va le propager
		if (removed.size() > 0 && _sourceEvents != null) {
			for (IEventCallback<L> c : removed) {
				_sourceEvents.trigger("onUnbind", c, this);
			}
		}
		
	}
	
	/**
	 * Désinscrire un listener.
	 */
	public void unbind(IListener<String> listener) {
		
		// Désactivé
		if (!enabled) {
			return;
		}
		
		// Méfiance
		if (listener == null) {
			throw new NullPointerException("listener is null");
		}
		
		// Liste des listeners retirés
		List<IEventCallback<L>> removed = new ArrayList<IEventCallback<L>>();
		
		synchronized (this) {
		
			// On parcours les listeners
			for (IEventCallback<L> c : new ArrayList<IEventCallback<L>>(_listeners)) {
				
				// Uniquement pour les CallbackMultipleEventIListener
				if (c instanceof CallbackMultipleEventIListener) {
					if (((CallbackMultipleEventIListener<?>) c).getListener() == listener) {
						// On retire le listener
						_listeners.remove(c);
						// On conserve l'instance dans la liste des listeners retirés
						removed.add(c);
					}
				}
				
				// Uniquement pour les CallbackSingleEventIListener
				if (c instanceof CallbackSingleEventIListener) {
					if (((CallbackSingleEventIListener<?>) c).getListener() == listener) {
						// On retire le listener
						_listeners.remove(c);
						// On conserve l'instance dans la liste des listeners retirés
						removed.add(c);
					}
				}
				
			}
		
		}
		
		// Si on a retiré des listeners, on va le propager
		if (removed.size() > 0 && _sourceEvents != null) {
			for (IEventCallback<L> c : removed) {
				_sourceEvents.trigger("onUnbind", c, this);
			}
		}
		
	}
	
	/**
	 * Désinscrire un listener.
	 */
	public void unbind(Runnable listener) {
		
		// Désactivé
		if (!enabled) {
			return;
		}
		
		// Méfiance
		if (listener == null) {
			throw new NullPointerException("listener is null");
		}
		
		// Liste des listeners retirés
		List<IEventCallback<L>> removed = new ArrayList<IEventCallback<L>>();
		
		synchronized (this) {
		
			// On parcours les listeners
			for (IEventCallback<L> c : new ArrayList<IEventCallback<L>>(_listeners)) {
				
				// Uniquement pour les CallbackSingleRunnable
				if (c instanceof CallbackSingleRunnable) {
					if (((CallbackSingleRunnable<?>) c).getListener() == listener) {
						// On retire le listener
						_listeners.remove(c);
						// On conserve l'instance dans la liste des listeners retirés
						removed.add(c);
					}
				}
				
				// Uniquement pour les CallbackSingleEventIListener
				if (c instanceof CallbackSingleEventIListener) {
					if (((CallbackSingleEventIListener<?>) c).getListener() == listener) {
						// On retire le listener
						_listeners.remove(c);
						// On conserve l'instance dans la liste des listeners retirés
						removed.add(c);
					}
				}
				
			}
		
		}
		
		// Si on a retiré des listeners, on va le propager
		if (removed.size() > 0 && _sourceEvents != null) {
			for (IEventCallback<L> c : removed) {
				_sourceEvents.trigger("onUnbind", c, this);
			}
		}
		
	}
	
	/**
	 * Propager un event.
	 * 
	 * @param eventName Nom de la méthode à déclancher.
	 * @param args Arguments de l'event.
	 */
	public boolean trigger(String eventName, Object... args) {
		return broadcast(eventName, null, args, false);
	}
	
	/**
	 * Propager un event uniquement pour le listener donné.
	 * 
	 * @param eventName
	 * @param filter
	 * @param args
	 */
	public void triggerOnly(String eventName, Object filter, Object... args) {
		broadcast(eventName, filter, args, false);
	}
	
	/**
	 * Propager un event en levant des exceptions.
	 * 
	 * @param eventName Nom de la méthode à déclancher.
	 * @param args Arguments de l'event.
	 */
	public boolean dispatch(String eventName, Object... args) {
		return broadcast(eventName, null, args, true);
	}
	
	/**
	 * Méthode interne de propagation.
	 */
	protected boolean broadcast(String eventName, Object filter, Object[] args, boolean raiseExceptions) {
		
		// Log
		/*if (LOGGER.isLoggable(IncaLogger.EVENT)) {
			LOGGER.log(IncaLogger.EVENT, "TRIGGER event " + this + "." + eventName);
		}*/
		
		// Désactivé
		if (!enabled) {
			return false;
		}
		
		// Aucun listener
		if (_listeners.size() == 0) {
			return true;
		}
		
		// On fait une copie
		List<IEventCallback<L>> copy = null;
		synchronized (this) {
			copy = new ArrayList<IEventCallback<L>>(_listeners);
		}
		
		// On parcours les listeners de la source
		for (IEventCallback<L> listener : copy) {
			
			// Filtrage
			if (filter != null) {
				if (!filter.equals(listener) && !filter.equals(listener.getTargetObject()))
					continue;
			}
			
			try {
				// Notification
				if (!listener.notifyEvent(this, eventName, args)) {
					// Log
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.log(Level.FINE, "HALT BROADCAST event " + eventName
								+ " by " + listener.toString());
					}
					// Rupture de la propagation
					return false;
				}
			}
			catch (Throwable t) {
				System.err.println("Error in EventSource.broadcast() : " + t.getClass().getSimpleName() + " - " + t.getMessage());
				if (raiseExceptions) {
					throw new EventDispatcherException(this, listener, eventName, t);
				}
				else {
					onUncaughtException(t, listener, eventName);
				}
			}
			
			// Continue
			
		}
		
		return true;
		
	}

	protected void onUncaughtException(Throwable ex, IEventCallback<L> listener, String eventName) {
		ex.printStackTrace();
	}

	/**
	 * Détruire cet objet
	 */
	public synchronized void dispose() {
		enabled = false;
		unbind();
		_listeners = null;
	}

	public Object getSender() {
		return _sender;
	}
	
	@Override
	public String toString() {
		return (_sender != null ? _sender : this).getClass().getSimpleName()
				+ "<"
				+ getListenerClass() != null ? getListenerClass().getSimpleName() : "AnonymousListener"
				+ ">";
	}

	public EventSource<SourceListener> getEventsSource() {
		return _sourceEvents;
	}

	public List<IEventCallback<L>> getListeners() {
		return new ArrayList<IEventCallback<L>>(_listeners);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}

	public void unbindTarget(Object target) {
		
		// Désactivé
		if (!enabled) {
			return;
		}
		
		// Méfiance
		if (target == null) {
			throw new NullPointerException("target is null");
		}
		
		// Liste des listeners retirés
		List<IEventCallback<L>> removed = new ArrayList<IEventCallback<L>>();
		
		synchronized (this) {
			// On parcours les listeners
			for (IEventCallback<L> c : new ArrayList<IEventCallback<L>>(_listeners)) {
				if (c.getTargetObject() == target) {
					// On retire le listener
					_listeners.remove(c);
					// On conserve l'instance dans la liste des listeners retirés
					removed.add(c);
				}
			}
		}
		
		// Si on a retiré des listeners, on va le propager
		if (removed.size() > 0 && _sourceEvents != null) {
			for (IEventCallback<L> c : removed) {
				_sourceEvents.trigger("onUnbind", c, this);
			}
		}
	}
	
}