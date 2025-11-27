import java.util.Random;

public class ProducerConsumer {

    public static final int nrProd = 2;
    public static final int nrCons = 3;
    public static final int nrObj = 11;
    public static final int dimDep = 8;
    public static final int prod = 2;

    public static void main(String[] args) {
        int ProdusTotal = nrCons * nrObj;

        Depozit depozit = new Depozit(dimDep, ProdusTotal);

        Producer[] Produceri = new Producer[nrProd];
        Consumer[] Consumeri = new Consumer[nrCons];

        for (int i = 0; i < nrProd; i++) {
            Produceri[i] = new Producer(depozit, i + 1, prod);
            Produceri[i].setName("Prod-" + (i + 1));
        }

        for (int i = 0; i < nrCons; i++) {
            Consumeri[i] = new Consumer(depozit, i + 1, nrObj);
            Consumeri[i].setName("Cons-" + (i + 1));
        }

        for (Producer p : Produceri) p.start();
        for (Consumer c : Consumeri) c.start();

        try {
            for (Producer p : Produceri) p.join();
            for (Consumer c : Consumeri) c.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



class Producer extends Thread {
    private final Depozit depozit;
    private final int id;
    private final int counter;
    private final Random random = new Random();

    public Producer(Depozit d, int id, int counter) {
        this.depozit = d;
        this.id = id;
        this.counter = counter;
    }

    private int genNrPar() {
        return random.nextInt(51) * 2;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < counter; i++) {
                int val = genNrPar();
                boolean success = depozit.produce(val, id);
                if (!success) {
                    System.out.println("Depozitul este plin, producatorul " + id + " sa oprit ");
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

class Consumer extends Thread {
    private final Depozit depozit;
    private final int id;
    private final int deConsum;
    private final Random random = new Random();

    public Consumer(Depozit d, int id, int deConsum) {
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
                " s-a saturat cu " + consumateLocal + " obiecte si se opreste.");
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

    public synchronized boolean produce(int valoare, int idProducer) {
        if (produse >= ProdusTotal) return false;

        while (count == buffer.length) {
            System.out.println(" Depozitul este plin, Producatorul " + idProducer + " asteapta ");
            try { wait(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            if (produse >= ProdusTotal) return false;
        }

        buffer[count] = valoare;
        count++;
        produse++;

        System.out.println("Producatorul " + idProducer + " a produs: " + valoare +
                " (in depozit: " + count + ", produse total: " + produse + ")");
        if (produse == ProdusTotal) finisat = true;

        notifyAll();
        return true;
    }

    public synchronized Integer consuma(int idConsumer) {
        while (count == 0 && !finisat) {
            System.out.println("Depozitul este gol, Consumatorul " + idConsumer + " asteapta");
            try { wait(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        if (count == 0 && finisat) return null;
        count--;
        int valoare = buffer[count];
        consumate++;

        System.out.println("Consumatorul " + idConsumer + " a consumat: " + valoare +
                " (in depozit: " + count + ", consumate total: " + consumate + ")");

        notifyAll();
        return valoare;
    }
}