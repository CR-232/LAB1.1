import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumer {

    public static final int nrProd = 2;
    public static final int nrCons = 3;
    public static final int nrObj = 11;
    public static final int dimDep = 8;
    public static final int prod = 2;

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        int totalDeProduse = nrCons * nrObj;

        DepozitPool depozit = new DepozitPool(dimDep, totalDeProduse);
        ExecutorService executor = Executors.newFixedThreadPool(nrProd + nrCons);

        for (int i = 0; i < nrProd; i++) {
            int producerId = i + 1;
            executor.submit(new ProducerPool(depozit, producerId, prod));
        }

        for (int i = 0; i < nrCons; i++) {
            int consumatorId = i + 1;
            executor.submit(new ConsumerPool(depozit, consumatorId, nrObj));
        }

        executor.shutdown();

        new Thread(() -> {
            try {
                while (!executor.isTerminated()) {
                    Thread.sleep(100);
                }
                System.out.println("\nProgram terminat.\n");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}

class ProducerPool implements Runnable {

    private final DepozitPool depozit;
    private final int id;
    private final int gram;
    private final Random random = new Random();

    public ProducerPool(DepozitPool d, int id, int gram) {
        this.depozit = d;
        this.id = id;
        this.gram = gram;
    }

    private int genNrPar() {
        return random.nextInt(51) * 2;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < gram; i++) {
                int val = genNrPar();
                boolean ok = depozit.produce(val, id);
                if (!ok) {
                    System.out.println("Producatorul " + id + " se opreste.");
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


class ConsumerPool implements Runnable {
    private final DepozitPool depozit;
    private final int id;
    private final int deConsum;
    private final Random random = new Random();

    public ConsumerPool(DepozitPool d, int id, int deConsum) {
        this.depozit = d;
        this.id = id;
        this.deConsum = deConsum;
    }

    @Override
    public void run() {
        int consumate = 0;

        while (consumate < deConsum) {
            Integer val = depozit.consuma(id);
            if (val == null) break;

            System.out.println("Consumatorul " + id + " a consumat: " + val +
                    " (consumat local: " + (consumate + 1) + "/" + deConsum + ")");

            consumate++;

            try {
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Consumatorul " + id + " s-a saturat cu " + consumate + "obiecte");
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
            System.out.println("Depozit plin. Producatorul " + idProducator + " asteapta");
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

        System.out.println("Producatorul " + idProducator + " a produs: " + valoare + " depozitul are: " + count + ", produse total: " + produse);

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