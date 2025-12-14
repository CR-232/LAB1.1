import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Individual {

    public static final int NR_CLIENTI = 15;
    public static final int NR_SCAUNE = 4;

    public static void main(String[] args) {

        Frizerie frizerie = new Frizerie(NR_SCAUNE, NR_CLIENTI);
        ExecutorService pool = Executors.newFixedThreadPool(6);

        pool.submit(new Frizer(frizerie));

        for (int i = 1; i <= NR_CLIENTI; i++) {
            int id = i;
            try {
                Thread.sleep(new Random().nextInt(600));
            } catch (InterruptedException ignored) {
            }
            pool.submit(new Client(frizerie, id));
        }

        pool.shutdown();
    }
}

class Frizerie {

    private final int nrScaune;
    private final int totalClienti;

    private int clientiAsteptare = 0;
    private int clientiProcesati = 0;
    private boolean activ = true;

    private final ReentrantLock lock = new ReentrantLock(true);

    private final Condition clientiDisponibili = lock.newCondition();
    private final Condition scaunLiber = lock.newCondition();
    private final Condition frizerDisponibil = lock.newCondition();

    public Frizerie(int nrScaune, int totalClienti) {
        this.nrScaune = nrScaune;
        this.totalClienti = totalClienti;
    }

    public void intrareClient(int id) throws InterruptedException {
        lock.lock();
        try {
            while (clientiAsteptare >= nrScaune && activ) {
                scaunLiber.await();
            }

            if (!activ) {
                System.out.println("Clientul " + id + " pleacă – frizeria e închisă.");
                return;
            }

            clientiAsteptare++;
            System.out.println("Clientul " + id +
                    " a intrat. În așteptare: " + clientiAsteptare);

            clientiDisponibili.signal();

            frizerDisponibil.await();

            System.out.println("Clientul " + id + " a fost tuns și pleacă.");

        } finally {
            lock.unlock();
        }
    }

    public boolean tunde() throws InterruptedException {
        lock.lock();
        try {
            while (clientiAsteptare == 0 && activ) {
                clientiDisponibili.await();
            }

            if (!activ && clientiAsteptare == 0)
                return false;

            clientiAsteptare--;
            System.out.println("Frizerul tunde un client. În așteptare: "
                    + clientiAsteptare);

            scaunLiber.signal();

        } finally {
            lock.unlock();
        }

        Thread.sleep(1200);

        lock.lock();
        try {
            clientiProcesati++;
            System.out.println("Frizerul a terminat tunsul. Total serviți: "
                    + clientiProcesati);

            frizerDisponibil.signal();

            if (clientiProcesati == totalClienti) {
                activ = false;
                clientiDisponibili.signalAll();
                scaunLiber.signalAll();
            }

            return activ || clientiAsteptare > 0;

        } finally {
            lock.unlock();
        }
    }
}

class Frizer implements Runnable {

    private final Frizerie frizerie;

    public Frizer(Frizerie frizerie) {
        this.frizerie = frizerie;
    }

    @Override
    public void run() {
        try {
            while (frizerie.tunde()) {
                Thread.sleep(300);
            }
            System.out.println("Frizerul închide frizeria.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Client implements Runnable {

    private final Frizerie frizerie;
    private final int id;

    public Client(Frizerie frizerie, int id) {
        this.frizerie = frizerie;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            frizerie.intrareClient(id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
