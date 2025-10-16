import static java.lang.Thread.currentThread;

public class Main {
    public static void main(String[] args) {
        int n = 100;
        int tab[] = new int[n];

        for (int i = 0; i < n; i++) {
            tab[i] = (int) (Math.random() * 100);
            System.out.print(tab[i] + " ");
        }

        System.out.println("\n");

        //  Firele lui Cocieru Dragoș
        Suma fir = new Suma(0, n - 1, tab, true, "Cocieru Dragoș");
        Suma sec = new Suma(0, n - 1, tab, false, "Cocieru Dragoș");
        Thread t1 = new Thread(fir, "SumaForward");
        Thread t2 = new Thread(sec, "SumaBackward");

        t1.start();
        t2.start();

        //  Firele lui Costriba Serafim
        SerafimThread f1 = new SerafimThread(0, n - 1, tab, false, "Costriba Serafim");
        SerafimThread f2 = new SerafimThread(0, n - 1, tab, true, "Costriba Serafim");
        Thread t3 = new Thread(f1, "Serafim1");
        Thread t4 = new Thread(f2, "Serafim2");

        t3.start();
        t4.start();

        //  Firele lui Maxim Cuciuc
        Thread1 fir1 = new Thread1(0, n - 1, tab, true, "Maxim Cuciuc");
        Thread1 fir2 = new Thread1(0, n - 1, tab, false, "Maxim Cuciuc");

        fir1.start();
        fir2.start();

        System.out.println("\n>>> Firele au fost pornite! \n");
    }
}

// ---------------------- CLASA SUMA ----------------------
class Suma implements Runnable {
    static int activeThreads = 2; // total fire pentru Cocieru Dragoș
    int from, to, tab[];
    boolean forward;
    String autor;

    public Suma(int from, int to, int tab[], boolean forward, String autor) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.forward = forward;
        this.autor = autor;
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

        // când firul s-a terminat
        activeThreads--;
        if (activeThreads == 0)
            System.out.println(" Firele lui " + autor + " s-au terminat.");
    }
}

// ---------------------- CLASA SERAFIMTHREAD ----------------------
class SerafimThread implements Runnable {
    static int activeThreads = 2; // total fire pentru Costriba Serafim
    int from, to, tab[];
    boolean revers;
    String autor;

    public SerafimThread(int from, int to, int[] tab, boolean revers, String autor) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.revers = revers;
        this.autor = autor;
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

        activeThreads--;
        if (activeThreads == 0)
            System.out.println(" Firele lui " + autor + " s-au terminat.");
    }
}

// ---------------------- CLASA THREAD1 ----------------------
class Thread1 extends Thread {
    static int activeThreads = 2; // total fire pentru Maxim Cuciuc
    int from, to, tab[];
    boolean directie;
    String autor;

    public Thread1(int from, int to, int tab[], boolean directie, String autor) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.directie = directie;
        this.autor = autor;
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

        activeThreads--;
        if (activeThreads == 0)
            System.out.println(" Firele lui " + autor + " s-au terminat.");
    }
}
