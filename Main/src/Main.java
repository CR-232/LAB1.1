import static java.lang.Thread.currentThread;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int n = 100;
        int tab[] = new int[n];

        for (int i = 0; i < n; i++) {
            tab[i] = (int) (Math.random() * 100);
            System.out.print(tab[i] + " ");
        }

        System.out.println("\n");

        // Firele lui Cocieru Dragoș
        Suma fir = new Suma(0, n - 1, tab, true);
        Suma sec = new Suma(0, n - 1, tab, false);
        Thread t1 = new Thread(fir, "SumaForward");
        Thread t2 = new Thread(sec, "SumaBackward");

        t1.start();
        t2.start();

        // Firele lui Costriba Serafim
        SerafimThread f1 = new SerafimThread(0, n - 1, tab, false);
        SerafimThread f2 = new SerafimThread(0, n - 1, tab, true);
        Thread t3 = new Thread(f1, "Serafim1");
        Thread t4 = new Thread(f2, "Serafim2");

        t3.start();
        t4.start();

        // Firele lui Maxim Cuciuc
        Thread1 fir1 = new Thread1(0, n - 1, tab, true);
        Thread1 fir2 = new Thread1(0, n - 1, tab, false);

        fir1.start();
        fir2.start();

        System.out.println("\n>>> Firele au fost pornite! \n");

        // Așteptăm ca toate firele să se termine
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        fir1.join();
        fir2.join();

        // După terminarea tuturor firelor, afișăm autorii litera cu literă
        String[] autori = {"COCIERU DRAGOS", "COSTRIBA SERAFIM", "MAXIM CRUC"};

        System.out.println("\n>>> Toți firele s-au terminat. Autorii:");

        for (String autor : autori) {
            for (char c : autor.toCharArray()) {
                System.out.print(c);
                try {
                    Thread.sleep(100); // 100ms între litere
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(); // linie nouă după fiecare autor
        }
    }
}

// ---------------------- CLASA SUMA ----------------------
class Suma implements Runnable {
    int from, to, tab[];
    boolean forward;

    public Suma(int from, int to, int tab[], boolean forward) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.forward = forward;
    }

    public void run() {
        int S = 0, C = 0, i = forward ? from : to, pas = forward ? 1 : -1;
        int sume[] = new int[50], index = 0;

        for (; (forward ? i <= to : i >= from); i += pas) {
            if (tab[i] < 50 && tab[i] % 2 == 0) {
                S += tab[i];
                C++;
            }
            if (C >= 2) {
                System.out.println(currentThread().getName() + "  Suma este  " + S);
                sume[index++] = S;
                C = 0;
                S = 0;
            }
        }

        int suma2 = 0, contor2 = 0;
        for (int j = 0; j < index; j++) {
            suma2 += sume[j];
            contor2++;
            if (contor2 == 2) {
                System.out.println(currentThread().getName() + "  Suma sumelor este  " + suma2);
                suma2 = 0;
                contor2 = 0;
            }
        }
    }
}

// ---------------------- CLASA SERAFIMTHREAD ----------------------
class SerafimThread implements Runnable {
    int from, to, tab[];
    boolean revers;

    public SerafimThread(int from, int to, int[] tab, boolean revers) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.revers = revers;
    }

    public void run() {
        int S1 = 0, S2 = 0, k = 0, sumaDoua = 0, count = 0;
        if (!revers) {
            for (int i = from; i <= to; i++) {
                if (tab[i] % 2 == 0) {
                    if (k == 0) S1 = tab[i]; else S2 = tab[i];
                    k++;
                    if (k == 2) {
                        int sumaPereche = S1 + S2;
                        System.out.println(Thread.currentThread().getName() +
                                " -> Suma a două valori pare: " + sumaPereche);
                        if (count == 0) {
                            sumaDoua = sumaPereche; count = 1;
                        } else {
                            sumaDoua += sumaPereche;
                            System.out.println("Suma perechilor: " + sumaDoua);
                            count = 0;
                        }
                        S1 = S2 = 0; k = 0;
                    }
                }
            }
        } else {
            for (int i = to; i >= from; i--) {
                if (tab[i] % 2 == 0) {
                    if (k == 0) S1 = tab[i]; else S2 = tab[i];
                    k++;
                    if (k == 2) {
                        int sumaPereche = S1 + S2;
                        System.out.println(Thread.currentThread().getName() +
                                " -> Suma a două valori pare: " + sumaPereche);
                        if (count == 0) {
                            sumaDoua = sumaPereche; count = 1;
                        } else {
                            sumaDoua += sumaPereche;
                            System.out.println("Suma perechilor: " + sumaDoua);
                            count = 0;
                        }
                        S1 = S2 = 0; k = 0;
                    }
                }
            }
        }
    }
}

// ---------------------- CLASA THREAD1 ----------------------
class Thread1 extends Thread {
    int from, to, tab[];
    boolean directie;

    public Thread1(int from, int to, int tab[], boolean directie) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.directie = directie;
    }

    public void run() {
        int S = 0, C = 0, s1 = 0, s2 = 0, pair = 0;

        if (directie) {
            for (int i = from; i <= to; i++) {
                if (tab[i] % 2 == 0) {
                    S += tab[i];
                    C++;
                }
                if (C == 2) {
                    pair++;
                    if (pair == 1) {
                        s1 = S;
                        System.out.println(currentThread().getName() + "  s1: " + s1);
                    } else {
                        s2 = S;
                        System.out.println(currentThread().getName() + "  s2: " + s2);
                        System.out.println(currentThread().getName() + "  Suma celor 2 sume: " + (s1 + s2));
                        pair = 0;
                    }
                    C = 0; S = 0;
                }
            }
        } else {
            for (int i = to; i >= from; i--) {
                if (tab[i] % 2 == 0) {
                    S += tab[i];
                    C++;
                }
                if (C == 2) {
                    pair++;
                    if (pair == 1) {
                        s1 = S;
                        System.out.println(currentThread().getName() + "  s1: " + s1);
                    } else {
                        s2 = S;
                        System.out.println(currentThread().getName() + "  s2: " + s2);
                        System.out.println(currentThread().getName() + "  Suma celor 2 sume: " + (s1 + s2));
                        pair = 0;
                    }
                    C = 0; S = 0;
                }
            }
        }
    }
}