package designpattern.observer.source;

public interface ICallback<T, E> {

	public void onSuccess(T result);

	public void onFailure(E error);
	
	public static class Adapter<A, B> implements ICallback<A, B> {

		public void onSuccess(A result) { }

		public void onFailure(B error) { }
		
	}

}
