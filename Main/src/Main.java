import static java.lang.Thread.currentThread;

public class Main {
    public static void main(String[] args) {
        int tab[] = new int[100];

        for (int i = 0; i < 99; i++) {
            tab[i] = (int)(Math.random() * 100);
            System.out.print(tab[i] + " ");
        }

        System.out.println();
        Suma fir = new Suma(0, 99, tab, true);
        Suma sec = new Suma(0, 99, tab, false);

        Thread t1 = new Thread(fir);
        Thread t2 = new Thread(sec);

        t1.start();
        t2.start();

        SerafimThread f1 = new SerafimThread(0, 99, tab, false);
        SerafimThread f2 = new SerafimThread(0, 99, tab, true);

        Thread t3 = new Thread(f1, "Firul1");
        Thread t4 = new Thread(f2, "Firul2");

        t3.start();
        t4.start();

    }
}

class Suma implements Runnable {

    int from;
    int to;
    int tab[];
    boolean forward;

    public Suma(int from, int to, int tab[], boolean forward) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.forward = forward;
    }

    public void run() {
        int S = 0;
        int C = 0;

        int i = forward ? from : to;
        int pas = forward ? 1 : -1;

        int sume[] = new int[50];
        int index = 0;

        for (; (forward ? i <= to : i >= from); i += pas) {
            if (tab[i] < 50 && tab[i] % 2 == 0) {
                S = S + tab[i];
                C++;
            }
            if (C >= 2) {
                System.out.println(currentThread().getName() + "  Suma este  " + S);
                sume[index++] = S;
                C = 0;
                S = 0;
            }
        }

        int suma2 = 0;
        int contor2 = 0;
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

class SerafimThread implements Runnable {
    int from;
    int to;
    int[] tab;
    boolean revers;

    public SerafimThread(int from, int to, int[] tab, boolean revers) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.revers = revers;
    }

    public void run() {
        int S1 = 0;        // prima valoare pară
        int S2 = 0;        // a doua valoare pară
        int k = 0;         // contor pentru valorile pare găsite
        int sumaDoua = 0;  // suma a două perechi consecutive
        int count = 0;     // marchează dacă avem o pereche sau două

        if (!revers) {
            for (int i = from; i <= to; i++) {
                if (tab[i] % 2 == 0) {
                    if (k == 0) S1 = tab[i];
                    else S2 = tab[i];
                    k++;

                    if (k == 2) {
                        int sumaPereche = S1 + S2;
                        System.out.println(Thread.currentThread().getName() +
                                " -> Suma a două valori pare: " + sumaPereche +
                                " (valori: " + S1 + ", " + S2 + ")");

                        if (count == 0) {
                            sumaDoua = sumaPereche;
                            count = 1;
                        } else {
                            sumaDoua += sumaPereche;
                            System.out.println((sumaDoua - sumaPereche) +
                                    " + " + sumaPereche + " = " + sumaDoua);
                            count = 0;
                        }

                        S1 = 0;
                        S2 = 0;
                        k = 0;
                    }
                }
            }
        } else {
            for (int i = to; i >= from; i--) {
                if (tab[i] % 2 == 0) {
                    if (k == 0) S1 = tab[i];
                    else S2 = tab[i];
                    k++;

                    if (k == 2) {
                        int sumaPereche = S1 + S2;
                        System.out.println(Thread.currentThread().getName() +
                                " -> Suma a două valori pare: " + sumaPereche +
                                " (valori: " + S1 + ", " + S2 + ")");

                        if (count == 0) {
                            sumaDoua = sumaPereche;
                            count = 1;
                        } else {
                            sumaDoua += sumaPereche;
                            System.out.println((sumaDoua - sumaPereche) +
                                    " + " + sumaPereche + " = " + sumaDoua);
                            count = 0;
                        }

                        S1 = 0;
                        S2 = 0;
                        k = 0;
                    }
                }
            }
        }

        if (count == 1) {
            System.out.println(sumaDoua + " = " + sumaDoua);
        }
    }
}