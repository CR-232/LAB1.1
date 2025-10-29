public class Main {
    public static void main(String[] args) {
        //Cocieru
        ThreadGroup sys = Thread.currentThread().getThreadGroup();

        Thread curr = Thread.currentThread();
        curr.setPriority(curr.getPriority()-2);

        Thread Th1 = new Thread(curr, "Th1");
        Th1.setPriority(7);
        Thread Th2 = new Thread(curr, "Th2");
        Th2.setPriority(7);
        Thread ThA = new Thread(curr, "ThA");
        ThA.setPriority(3);

        Th1.start();
        Th2.start();
        ThA.start();
        sys.list();

        ThreadGroup g1 = new ThreadGroup("G1");
        g1.setMaxPriority(Thread.MAX_PRIORITY);
        ThreadGroup g3 = new ThreadGroup(g1, "G3");
        g3.setMaxPriority(Thread.MAX_PRIORITY);

        Thread Tha = new Thread(g3, "Tha");
        Tha.setPriority(3);
        Tha.start();
        Thread Thb = new Thread(g3, "Thb");
        Thb.setPriority(3);
        Thb.start();
        Thread Thc = new Thread(g3, "Thc");
        Thc.setPriority(3);
        Thc.start();
        Thread Thd = new Thread(g3, "Thd");
        Thd.setPriority(3);
        Thd.start();
        g3.list();

        ThreadGroup g2 = new ThreadGroup("G2");
        g2.setMaxPriority(Thread.MAX_PRIORITY);

        Thread Th1_g2 = new Thread(g2, "Th1");
        Th1_g2.start();
        Th1_g2.setPriority(4);
        Thread Th2_g2 = new Thread(g2, "Th2");
        Th2_g2.start();
        Th2_g2.setPriority(5);
        Thread Th3_g2 = new Thread(g2, "Th3");
        Th3_g2.start();
        Th3_g2.setPriority(5);
        g2.list();

    }
}
