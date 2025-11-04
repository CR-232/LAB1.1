public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        int n = 100;
        int tab[] = new int[n];

        for (int i = 0; i < n; i++) {
            tab[i] = (int) (Math.random() * 100);
            System.out.print(tab[i] + " ");
        }

        System.out.println("\n");


        Thread1 fir1 = new Thread1(0, n - 1, tab, true);
        Thread1 fir2 = new Thread1(0, n - 1, tab, false);

        fir1.start();
        fir2.start();

        System.out.println("\n>>> Firele au fost pornite! \n");


        fir1.join();
        fir2.join();

        // După terminarea tuturor firelor, afișăm autorii litera cu literă
        String[] autori = {"COCIERU DRAGOS", "COSTRIBA SERAFIM", "MAXIM CRUCCCCCC"};

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
            System.out.println();
        }
    }
}


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