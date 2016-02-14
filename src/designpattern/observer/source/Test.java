package designpattern.observer.source;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
	
	public static interface SensorListener extends IEventListener {
		public void onSensorChanged(double temperature, double humidity);
	}
	
	public static interface TimeSensorListener extends SensorListener {
		public void onTimeChanged(LocalDateTime dateTime);
	}
	
	public static class Sensor {
		
		public final EventSource<SensorListener> Events = new EventSource<>(SensorListener.class);
		
		public void readData() {
			double t = Math.random() * 20;
			double h = Math.random() * 100;
			Events.trigger("onSensorChanged", t, h);
			Events.trigger("onTimeChanged", LocalDateTime.now());
		}
		
	}
	
	public static class OtherListener {

		public void maMethode(Double temperature, Double humidity) {
			System.out.println(String.format("Method [T=%.2f°C H=%.2f%%]", temperature, humidity));
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Implémentation : SOURCE");
		
		// On fabrique le capteur
		Sensor s = new Sensor();
		
		// Avec l'interface
		s.Events.bind(new SensorListener() {
			public void onSensorChanged(double temperature, double humidity) {
				System.out.println(String.format("Interface [T=%.2f°C H=%.2f%%]", temperature, humidity));
			}
		});
		
		// Avec une méthode, par réflexion
		s.Events.bind("onSensorChanged", new OtherListener(), "maMethode");
		
		// Avec un Runnable
		s.Events.bind("onSensorChanged", () -> {
			System.out.println("Runnable [T=? H=?]");
		});
		
		// Avec une extension du SensorListener
		s.Events.bind(new TimeSensorListener() {
			double temperature;
			double humidity;
			public void onSensorChanged(double temperature, double humidity) {
				this.temperature = temperature;
				this.humidity = humidity;
			}
			public void onTimeChanged(LocalDateTime dateTime) {
				System.out.println(String.format("Extension [T=%.2f°C H=%.2f%%]", temperature, humidity)
						+ dateTime.format(DateTimeFormatter.ofPattern(" dd-MM-YYYY HH:mm:ss")));
			}
		});
		
		while (true) {
			
			s.readData();
			
			Thread.sleep(1000);
			
		}
		
	}

}
