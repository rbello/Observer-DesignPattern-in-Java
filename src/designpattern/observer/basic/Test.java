package designpattern.observer.basic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
	
	public static class Sensor extends AObservable<SensorData> {
		public void readData() {
			double t = Math.random() * 20;
			double h = Math.random() * 100;
			notifyListeners(new SensorData(t, h));
		}
	}
	
	public static class SensorData {

		public final double temperature;
		public final double humidity;

		public SensorData(double temperature, double humidity) {
			this.temperature = temperature;
			this.humidity = humidity;
		}
		
		@Override
		public String toString() {
			return String.format("[T=%.2f°C H=%.2f%%]", temperature, humidity);
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		
		Sensor s = new Sensor();
		
		s.addListener(new IObserver<Test.SensorData>() {
			@Override
			public void onEvent(SensorData event) {
				System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss")) + " " + event);
			}
		});
		
		while (true) {
			
			s.readData();
			
			Thread.sleep(1000);
			
		}
		
	}

}
