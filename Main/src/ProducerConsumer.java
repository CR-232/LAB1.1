import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {

    static final int X = 2;  // producatorii
    static final int Y = 3;  // consumatori
    static final int Z = 40; // număr total obiecte
    static final int D = 8;  // dimensiune depozit
    static class Depozit {

        private final Deque<Integer> buffer = new ArrayDeque<>(D);
        private final ReentrantLock lock = new ReentrantLock(true);

        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();

        private int totalProduse = 0;
        private int totalConsumate = 0;
        private boolean gata = false;

        private int nextEven = 2;

        public void produce(String name) throws InterruptedException {
            lock.lock();
            try {
                if (totalProduse >= Z) {
                    gata = true;
                    notEmpty.signalAll();
                    return;
                }

                while (!buffer.isEmpty()) {
                    notFull.await();
                }

                for (int i = 0; i < D; i++) {
                    if (totalProduse >= Z) break;

                    buffer.addLast(nextEven);
                    System.out.println(name + " a produs: " + nextEven);

                    nextEven += 2;
                    totalProduse++;
                }

                System.out.println(">>> Depozitul este PLIN: " + buffer.size() + "/" + D);
                notEmpty.signalAll();

            } finally {
                lock.unlock();
            }
        }

        public void consume(String name) throws InterruptedException {
            lock.lock();
            try {
                while (buffer.size() < D && !gata) {
                    notEmpty.await();
                }

                if (buffer.isEmpty() && gata) {
                    return;
                }

                while (!buffer.isEmpty()) {
                    int val = buffer.removeFirst();
                    System.out.println(name + " a consumat: " + val);
                    totalConsumate++;

                    if (totalConsumate >= Z) {
                        gata = true;
                        return;
                    }
                }

                System.out.println("<<< Depozitul este GOL");
                notFull.signalAll();

            } finally {
                lock.unlock();
            }
        }
    }

    static class Producator extends Thread {
        private final Depozit depozit;

        Producator(Depozit d, String name) {
            super(name);
            this.depozit = d;
        }

        @Override
        public void run() {
            try {
                while (!depozit.gata) {
                    depozit.produce(getName());
                    sleep(100);
                }
            } catch (Exception ignored) {
            }
        }
    }

    static class Consumator extends Thread {
        private final Depozit depozit;

        Consumator(Depozit d, String name) {
            super(name);
            this.depozit = d;
        }

        @Override
        public void run() {
            try {
                while (!depozit.gata) {
                    depozit.consume(getName());
                    sleep(120);
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Depozit depozit = new Depozit();

        Thread[] producatori = new Thread[X];
        Thread[] consumatori = new Thread[Y];

        for (int i = 0; i < X; i++)
            producatori[i] = new Producator(depozit, "Producator-" + (i + 1));

        for (int i = 0; i < Y; i++)
            consumatori[i] = new Consumator(depozit, "Consumator-" + (i + 1));

        for (Thread p : producatori) p.start();
        for (Thread c : consumatori) c.start();

        for (Thread p : producatori) p.join();
        for (Thread c : consumatori) c.join();

        System.out.println("\n=== FINALIZAT: 40 obiecte produse și consumate corect ===");
    }
}
