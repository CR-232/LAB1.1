import java.util.Random;

public class ProducerConsumer {

    public static final int X = 2;
    public static final int Y = 3;
    public static final int Z = 11;
    public static final int D = 8;
    public static final int F = 2;

    public static void main(String[] args) {



        int ProdusTotal = Y * Z;

        Depozit depozit = new Depozit(D, ProdusTotal);

        Producator[] producatori = new Producator[X];
        Consumator[] consumatori = new Consumator[Y];

        for (int i = 0; i < X; i++) {
            producatori[i] = new Producator(depozit, i + 1, F);
            producatori[i].setName("Prod-" + (i + 1));
        }

        for (int i = 0; i < Y; i++) {
            consumatori[i] = new Consumator(depozit, i + 1, Z);
            consumatori[i].setName("Cons-" + (i + 1));
        }

        for (Producator p : producatori) p.start();
        for (Consumator c : consumatori) c.start();

        try {
            for (Producator p : producatori) p.join();
            for (Consumator c : consumatori) c.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    }
}

class Depozit {
    private final int[] buffer;
    private int count = 0;

    private final int ProdusTotal;
    private int produse = 0;
    private int consumate = 0;
    private boolean finisat = false;

    public Depozit(int dimensiune, int ProdusTotal) {
        this.buffer = new int[dimensiune];
        this.ProdusTotal = ProdusTotal;
    }

    public synchronized boolean produce(int valoare, int idProducator) {
        if (produse >= ProdusTotal) return false;

        while (count == buffer.length) {
            System.out.println(">>> Depozitul este PLIN. Producatorul " + idProducator + " asteapta...");
            try { wait(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            if (produse >= ProdusTotal) return false;
        }

        buffer[count] = valoare;
        count++;
        produse++;

        System.out.println("Producatorul " + idProducator + " a produs: " + valoare +
                " (in depozit: " + count + ", produse total: " + produse + ")");

        if (produse == ProdusTotal) finisat = true;

        notifyAll();
        return true;
    }

    public synchronized Integer consuma(int idConsumator) {
        while (count == 0 && !finisat) {
            System.out.println("<<< Depozitul este GOL. Consumatorul " + idConsumator + " asteapta...");
            try { wait(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        if (count == 0 && finisat) return null;

        count--;
        int valoare = buffer[count];
        consumate++;

        System.out.println("Consumatorul " + idConsumator + " a consumat: " + valoare +
                " (in depozit: " + count + ", consumate total: " + consumate + ")");

        notifyAll();
        return valoare;
    }
}

class Producator extends Thread {
    private final Depozit depozit;
    private final int id;
    private final int batch;
    private final Random random = new Random();

    public Producator(Depozit d, int id, int batch) {
        this.depozit = d;
        this.id = id;
        this.batch = batch;
    }

    private int genNrPar() {
        return 10 + random.nextInt(61) * 2;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < batch; i++) {
                int val = genNrPar();
                boolean success = depozit.produce(val, id);
                if (!success) {
                    System.out.println("Producatorul " + id + " sa oprit ");
                    return;
                }
                try { Thread.sleep(random.nextInt(400)); } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}

class Consumator extends Thread {
    private final Depozit depozit;
    private final int id;
    private final int deConsum;
    private final Random random = new Random();

    public Consumator(Depozit d, int id, int deConsum) {
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

            consumateLocal++;

            try { Thread.sleep(random.nextInt(500)); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Consumatorul " + id +
                " s-a indestulat cu " + consumateLocal + " obiecte si se opreste.");
    }
}
