import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumer {

    public static final int X = 2;
    public static final int Y = 3;
    public static final int Z = 11;
    public static final int D = 8;
    public static final int F = 2;

    public static void main(String[] args) {

        System.out.println("=== Pornire simulare producător-consumator (varianta fără Swing) ===\n");

        runSimulation();
    }

    private static void runSimulation() {
        int totalDeProduse = Y * Z;

        DepozitPool depozit = new DepozitPool(D, totalDeProduse);
        ExecutorService executor = Executors.newFixedThreadPool(X + Y);

        for (int i = 0; i < X; i++) {
            int producerId = i + 1;
            executor.submit(new ProducatorPool(depozit, producerId, F));
        }

        for (int i = 0; i < Y; i++) {
            int consumatorId = i + 1;
            executor.submit(new ConsumatorPool(depozit, consumatorId, Z));
        }

        executor.shutdown();

        new Thread(() -> {
            try {
                while (!executor.isTerminated()) {
                    Thread.sleep(100);
                }
                System.out.println("\nToate obiectele au fost produse si consumate. Program terminat.\n");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}



class DepozitPool {
    private final int[] buffer;
    private int count = 0;

    private final int totalDeProduse;
    private int produse = 0;
    private boolean terminat = false;

    public DepozitPool(int dimensiune, int totalDeProduse) {
        this.buffer = new int[dimensiune];
        this.totalDeProduse = totalDeProduse;
    }

    public synchronized boolean produce(int valoare, int idProducator) {
        if (produse >= totalDeProduse) return false;

        while (count == buffer.length) {
            System.out.println(">>> Depozitul este PLIN. Producatorul " + idProducator + " asteapta...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            if (produse >= totalDeProduse) return false;
        }

        buffer[count] = valoare;
        count++;
        produse++;

        System.out.println("Producatorul " + idProducator + " a produs: " + valoare +
                " (in depozit: " + count + ", produse total: " + produse + ")");

        if (produse == totalDeProduse) terminat = true;

        notifyAll();
        return true;
    }

    public synchronized Integer consuma(int idConsumator) {
        while (count == 0 && !terminat) {
            System.out.println("<<< Depozitul este GOL. Consumatorul " + idConsumator + " asteapta...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        if (count == 0 && terminat) return null;

        count--;
        int valoare = buffer[count];

        notifyAll();
        return valoare;
    }
}



class ProducatorPool implements Runnable {
    private final DepozitPool depozit;
    private final int id;
    private final int batchSize;
    private final Random random = new Random();

    public ProducatorPool(DepozitPool d, int id, int batchSize) {
        this.depozit = d;
        this.id = id;
        this.batchSize = batchSize;
    }

    private int genereazaNumarPar() {
        return 10 + random.nextInt(61) * 2; // numere pare
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < batchSize; i++) {
                int val = genereazaNumarPar();
                boolean ok = depozit.produce(val, id);
                if (!ok) {
                    System.out.println("Producatorul " + id + " se opreste (nu mai sunt necesare obiecte).");
                    return;
                }
                try {
                    Thread.sleep(random.nextInt(400));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}


class ConsumatorPool implements Runnable {
    private final DepozitPool depozit;
    private final int id;
    private final int deConsum;
    private final Random random = new Random();

    public ConsumatorPool(DepozitPool d, int id, int deConsum) {
        this.depozit = d;
        this.id = id;
        this.deConsum = deConsum;
    }

    @Override
    public void run() {
        int consumateLocal = 0;

        while (consumateLocal < deConsum) {
            Integer val = depozit.consuma(id);
            if (val == null) break;

            System.out.println("Consumatorul " + id + " a consumat: " + val +
                    " (consumat local: " + (consumateLocal + 1) + "/" + deConsum + ")");

            consumateLocal++;

            try {
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Consumatorul " + id +
                " s-a indestulat cu " + consumateLocal + " obiecte si se opreste.");
    }
}
