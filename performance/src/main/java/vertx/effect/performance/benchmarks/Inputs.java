package vertx.effect.performance.benchmarks;

public class Inputs {
    public static int TIMES;
    public static int DELAY;
    public static int VERTICLE_INSTANCES;
    public static int WORKERS;

    static {
            String times = System.getProperty("times");
            String delay = System.getProperty("delay");
            String instances = System.getProperty("instances");
            String workers = System.getProperty("workers");
            System.out.println("times "+times);
            System.out.println("delay "+delay);
            System.out.println("instances "+instances);
            System.out.println("workers "+workers);
            if(times==null) throw new NullPointerException("times is null");
            if(delay==null) throw new NullPointerException("delay is null");
            if(instances==null) throw new NullPointerException("instances is null");
            if(workers==null) throw new NullPointerException("workers is null");
            DELAY = Integer.parseInt(delay);
            TIMES = Integer.parseInt(times);
            VERTICLE_INSTANCES = Integer.parseInt(instances);
            WORKERS = Integer.parseInt(workers);
    }
}
