package designpattern.observer.emitter;

public class Test {
	
	public static class Sensor {
		
		public final IEventEmitter<Double> OnTemperatureChanged = new EventEmitter<Double>();
		
		public final IEventEmitter<Double> OnHumidityChanged = new EventEmitter<Double>();
		
		public void readData() {
			double t = Math.random() * 20;
			OnTemperatureChanged.notify(t);
			double h = Math.random() * 100;
			OnHumidityChanged.notify(h);
		}
		
	}
	
	public static class Observer {
		
		public void onTemperatureChanged(double value) {
			System.out.println(String.format("Lambda Temperature: %.2f°C", value));
		}
		
		public void onHumidityChanged(double value) {
			System.out.println(String.format("Lambda Humidity: %.2f%%", value));
		}
		
	}
	
	public static class AnnotedObserver {
		
		// Bindé implicitement par son nom
		@BindEvent
		public void OnTemperatureChanged(double value) {
			System.out.println(String.format("Annotation Implicite Temperature: %.2f°C", value));
		}
		
		// Bindé sur un event explicite
		@BindEvent("OnHumidityChanged")
		public void uneAutreMethode(double value) {
			System.out.println(String.format("Annotation Explicite Humidity: %.2f%%", value));
		}
		
		// Bindé sur plusieurs events
		@BindEvent({"OnHumidityChanged", "OnTemperatureChanged"})
		public void uneDerniereMethode(double value) {
			System.out.println(String.format("Annotations Value: %.2f%%", value));
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Implémentation : EMITTER");
		
		// On fabrique le capteur
		Sensor s = new Sensor();
		
		// Bind méthode par méthode
		Observer o1 = new Observer();
		s.OnHumidityChanged.add(o1::onHumidityChanged);
		s.OnTemperatureChanged.add(o1::onTemperatureChanged);
		
		// Bind des méthodes annotées
		AnnotedObserver o2 = new AnnotedObserver();
		Events.bind(s, o2);
		
		Events.unbind(o1, o2);
		
		while (true) {
			
			s.readData();
			
			Thread.sleep(1000);
			
		}
		
	}

}
