import static java.lang.Thread.currentThread;

public class Main {
    public static void main(String[] args) {
        int tab[] = new int[50];

        for (int i = 0; i < 50; i++) {
            tab[i] = (int)(Math.random() * 100);
            System.out.print(tab[i] + " ");
        }

        System.out.println();
        Suma fir = new Suma(0, 49, tab, true);
        Suma sec = new Suma(0, 49, tab, false);

        Thread t1 = new Thread(fir);
        Thread t2 = new Thread(sec);

        t1.start();
        t2.start();
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
