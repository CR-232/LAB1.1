import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Individual {

    public static final int nrClient = 15;
    public static final int nrScaune = 4;

    public static void main(String[] args) {

        Frizerie shop = new Frizerie(nrScaune, nrClient);
        ExecutorService pool = Executors.newFixedThreadPool(6);

        pool.submit(new Frizer(shop));

        for (int i = 1; i <= nrClient; i++) {
            int id = i;
            try {
                Thread.sleep(new Random().nextInt(600));
            } catch (Exception ignored) {}
            pool.submit(new Client(shop, id));
        }

        pool.shutdown();
    }
}

class Frizerie {

    private final int nrScaune;
    private final int totalClienti;

    private final AtomicInteger clientiProcesati = new AtomicInteger(0);

    private int clientiAsteptare = 0;
    private int clientiServiti = 0;
    private boolean activ = true;

    private final Semaphore clientiGata = new Semaphore(0);
    private final Semaphore frizerGata = new Semaphore(0);
    private final Semaphore mutex = new Semaphore(1);

    public Frizerie(int nrScaune, int totalClienti) {
        this.nrScaune = nrScaune;
        this.totalClienti = totalClienti;
    }

    public void intrareClient(int id) throws InterruptedException {

        mutex.acquire();

        if (clientiAsteptare >= nrScaune) {
            System.out.println("Clientul " + id + " pleacă – sala este plină.");

            if (clientiProcesati.incrementAndGet() == totalClienti) {
                activ = false;
                clientiGata.release();
            }

            mutex.release();
            return;
        }

        clientiAsteptare++;
        System.out.println("Clientul " + id + " a intrat. În așteptare: " + clientiAsteptare);

        clientiGata.release();
        mutex.release();

        frizerGata.acquire();

        System.out.println("Clientul " + id + " a fost tuns și pleacă.");
    }

    public boolean tunde() throws InterruptedException {

        clientiGata.acquire();

        mutex.acquire();

        if (!activ && clientiAsteptare == 0) {
            mutex.release();
            return false;
        }

        if (clientiAsteptare == 0) {
            mutex.release();
            return activ;
        }

        clientiAsteptare--;
        System.out.println("Bărbierul tunde un client. În așteptare rămân: " + clientiAsteptare);

        mutex.release();

        Thread.sleep(1200);

        mutex.acquire();

        clientiServiti++;
        System.out.println("Bărbierul a terminat tunsul. (Total serviți: " + clientiServiti + ")");

        if (clientiProcesati.incrementAndGet() == totalClienti) {
            activ = false;
        }

        mutex.release();
        frizerGata.release();

        return activ || clientiAsteptare > 0;
    }
}

class Frizer implements Runnable {

    private final Frizerie shop;

    public Frizer(Frizerie shop) {
        this.shop = shop;
    }

    @Override
    public void run() {
        try {
            while (shop.tunde()) {
                Thread.sleep(300);
            }
            System.out.println("Frizerul închide frizeria.");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Client implements Runnable {

    private final Frizerie shop;
    private final int id;

    public Client(Frizerie shop, int id) {
        this.shop = shop;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            shop.intrareClient(id);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
