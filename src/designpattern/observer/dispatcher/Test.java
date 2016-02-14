package designpattern.observer.dispatcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
	
	public static class Sensor implements IObservable<String> {
		
		private IEventDispatcher<String> _dispatcher = new EventDispatcher<String>();
		
		public void readData() {
			double t = Math.random() * 20;
			double h = Math.random() * 100;
			_dispatcher.trigger("DataRead", t, h);
		}

		@Override
		public IEventDispatcher<String> events() {
			return _dispatcher;
		}
		
	}
	
	public static class OtherListener {

		public void maMethode(Double temperature, Double humidity) {
			System.out.println("Quelqu'un d'autre a écouté en premier... " + String.format("[T=%.2f°C H=%.2f%%]", temperature, humidity));
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Implémentation : DISPATCHER");
		
		// On fabrique le capteur
		Sensor s = new Sensor();
		
		// On s'inscrit aux nouvelles données en utilisant l'interface IListener
		s.events().bind("DataRead", new IListener<String>() {
			public boolean notifyEvent(String event, Object... args) {
				
				// On affiche la donnée
				System.out.println(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss"))
						+ " "
						+ String.format("[T=%.2f°C H=%.2f%%]", args[0], args[1]));
				
				// On permet la propagation
				return true;
			}
		});
		
		// On fabrique un autre observer, qui n'implémente pas forcément l'interface IListener
		OtherListener o = new OtherListener();
		
		// On bind la méthode OtherListener::maMethode comme listener, avec une priorité plus importante
		// que par défaut (100)
		s.events().bind("DataRead", o, "maMethode", 101);
		
		while (true) {
			
			s.readData();
			
			Thread.sleep(1000);
			
		}
		
	}

}
