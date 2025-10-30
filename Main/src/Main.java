public class Main {
    static class MyThread extends Thread {
        public MyThread(ThreadGroup group, String name, int priority) {
            super(group, name);
            this.setPriority(priority);
        }
        public void run() {

            System.out.println(
                    "Thread: " + getName() +
                    " | Group: " + getThreadGroup().getName() +
                    " | Priority: " + getPriority());
        }
    }

    public static void main(String[] args) {

        ThreadGroup sys = Thread.currentThread().getThreadGroup();

        MyThread Th1 = new MyThread(sys, "Th1", 7);
        MyThread Th2 = new MyThread(sys, "Th2", 7);
        MyThread ThA = new MyThread(sys, "ThA", 3);

        Th1.start();
        Th2.start();
        ThA.start();

        ThreadGroup g1 = new ThreadGroup(sys, "G1");

        ThreadGroup g3 = new ThreadGroup(g1, "G3");

        MyThread Tha = new MyThread(g3, "Tha", 3);
        MyThread Thb = new MyThread(g3, "Thb", 3);
        MyThread Thc = new MyThread(g3, "Thc", 3);
        MyThread Thd = new MyThread(g3, "Thd", 3);

        Tha.start();
        Thb.start();
        Thc.start();
        Thd.start();

        ThreadGroup g2 = new ThreadGroup(sys, "G2");

        MyThread Th1_g2 = new MyThread(g2, "Th1", 4);
        MyThread Th2_g2 = new MyThread(g2, "Th2", 5);
        MyThread Th3_g2 = new MyThread(g2, "Th3", 5);

        Th1_g2.start();
        Th2_g2.start();
        Th3_g2.start();

        System.out.println("\n Structura ThreadGroup");
    }
}
