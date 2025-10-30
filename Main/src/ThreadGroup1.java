public class ThreadGroup1 {

    static class MyThread extends Thread {
        public MyThread(ThreadGroup group, String name, int priority) {
            super(group, name);
            this.setPriority(priority);
        }

        @Override
        public void run() {
            System.out.println("Thread: " + getName() +
                    " | Group: " + getThreadGroup().getName() +
                    " | Priority: " + getPriority());
        }
    }

    public static void main(String[] args) {

        ThreadGroup sys = Thread.currentThread().getThreadGroup();

        MyThread Th1 = new MyThread(sys, "Th1", 3);
        MyThread Th2 = new MyThread(sys, "Th2", 3);
        Th1.start();
        Th2.start();
        sys.list();

        ThreadGroup g1 = new ThreadGroup("G1");
        MyThread ThA = new MyThread(g1, "ThA", 3);
        ThA.start();
        g1.list();

        ThreadGroup g2 = new ThreadGroup("G2");
        MyThread Th3 = new MyThread(g2, "Th3", 3);
        MyThread Th8 = new MyThread(g2, "Th8", 3);
        MyThread Th9 = new MyThread(g2, "Th9", 4);
        Th3.start();
        Th8.start();
        Th9.start();
        g2.list();

        ThreadGroup g3 = new ThreadGroup("G3");
        MyThread ThC = new MyThread(g3, "ThC", 3);
        MyThread ThD = new MyThread(g3, "ThD", 3);
        MyThread ThF = new MyThread(g3, "ThF", 3);
        MyThread ThB = new MyThread(g3, "ThB", 7);
        ThC.start();
        ThD.start();
        ThF.start();
        ThB.start();
        g3.list();

        System.out.println("\n--- Structura completă a grupurilor ---");
        sys.list();
    }
}
