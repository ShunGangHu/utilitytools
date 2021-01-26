package svnkittest;

public class Singleton {
	 private Singleton() {}

	    private static class SingletonInstance {
	        private static final Singleton INSTANCE = new Singleton();
	    }

	    public static Singleton getInstance() {
	        return SingletonInstance.INSTANCE;
	    }
}
