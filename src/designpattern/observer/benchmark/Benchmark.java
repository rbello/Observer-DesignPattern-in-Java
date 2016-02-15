package designpattern.observer.benchmark;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @see http://stackoverflow.com/questions/504103/how-do-i-write-a-correct-micro-benchmark-in-java
 */
public class Benchmark {
	
	public static void main(String[] args) {
		
		List<IBenchmarkImplementation> tester = new ArrayList<>();
		tester.add(new ImplBasic());
		tester.add(new ImplDispatcher());
		tester.add(new ImplEmitter());
		tester.add(new ImplEmitter2());
		
//		Parallel.For(
//				tester, // La liste des implémentations à tester
//				Benchmark::execute, // La méthode d'execution
//				Benchmark::end // La callback de fin
//		);
		
		tester.forEach((t) -> {
			final long startTime = System.nanoTime();
			t.initListeners();
			System.err.println("Started: " + t.getClass().getName() + " with " + t.getObserversCount() + " observers");
			
			for (int i = 10000; i > 0; --i) {
				t.notifyListeners(Math.random(), "48f5d6s1v65s156FS165FSf1c561q6f1c8sd41v6dsf4v98E4SC86D1C861SDC<894SDQC891DQV9F1SD61VDF");
			}
			System.err.println(String.format("Duration : %s", ((System.nanoTime() - startTime) / 1000000000d)));
		});
		
	}
	
	public static interface IBenchmarkImplementation {
		public void initListeners();
		public int getObserversCount();
		public void notifyListeners(double number, String varchar);
	}
	
	public static void count(IBenchmarkImplementation impl) {
		
	}

}
