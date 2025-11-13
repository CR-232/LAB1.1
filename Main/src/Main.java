public class Main {

    public static void main(String[] args) {

        int n = 1000;
        int tab[] = new int[n];
        for (int i = 0; i < n; i++)
            tab[i] = (int) (Math.random() * 100);

        Thread1 Th1 = new Thread1(0, n - 1, tab, true);
        Thread1 Th2 = new Thread1(0, n - 1, tab, false);

        FirParcurgere Th3 = new FirParcurgere(
                100, 500, tab, true,
                "Disciplina: Programare Concurenta si Distribuita"
        );
        FirParcurgere Th4 = new FirParcurgere(
                300, 700, tab, false,
                "Grupa: CR-232"
        );

        Th1.setName("Th1");
        Th2.setName("Th2");
        Th3.setName("Th3");
        Th4.setName("Th4");

        Th1.start();
        Th2.start();
        Th3.start();
        Th4.start();

        try {
            Thread.sleep(3000);
            Th2.interrupt();

            Thread.sleep(1500);
            Th4.interrupt();

            Thread.sleep(1500);
            Th1.interrupt();

            Thread.sleep(1500);
            Th3.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void afisareCuPauza(String text) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(100); } catch (Exception ignored) {}
        }
        System.out.println();
    }
}

class FirParcurgere extends Thread {
    int from, to;
    int tab[];
    boolean directie;
    String mesajFinal;

    public FirParcurgere(int from, int to, int tab[], boolean directie, String mesajFinal) {
        this.from = from;
        this.to = to;
        this.tab = tab;
        this.directie = directie;
        this.mesajFinal = mesajFinal;
    }

    public void run() {

        if (directie) {
            System.out.println(getName() + " → Parcurgere crescătoare ["+from+".."+to+"]:");
            for (int i = from; i <= to; i++) {
                System.out.print(tab[i] + " ");
                try { Thread.sleep(2); } catch (Exception ignored) {}
            }
        } else {
            System.out.println(getName() + " → Parcurgere descrescătoare ["+to+".."+from+"]:");
            for (int i = to; i >= from; i--) {
                System.out.print(tab[i] + " ");
                try { Thread.sleep(2); } catch (Exception ignored) {}
            }
        }

        System.out.println();

        while (!isInterrupted()) {
            try { Thread.sleep(200); }
            catch (InterruptedException e) {
                Main.afisareCuPauza(mesajFinal);
                break;
            }
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
                if (tab[i] % 2 == 0) { S += tab[i]; C++; }
                if (C == 2) {
                    pair++;
                    if (pair == 1) s1 = S;
                    else {
                        s2 = S;
                        System.out.println(getName()+" → Suma = " + (s1+s2));
                        pair = 0;
                    }
                    C = 0; S = 0;
                }
                try { Thread.sleep(1); } catch (Exception ignored) {}
            }
        }
        else {
            for (int i = to; i >= from; i--) {
                if (tab[i] % 2 == 0) { S += tab[i]; C++; }
                if (C == 2) {
                    pair++;
                    if (pair == 1) s1 = S;
                    else {
                        s2 = S;
                        System.out.println(getName()+" → Suma = " + (s1+s2));
                        pair = 0;
                    }
                    C = 0; S = 0;
                }
                try { Thread.sleep(1); } catch (Exception ignored) {}
            }
        }

        while (!isInterrupted()) {
            try { Thread.sleep(200); }
            catch (InterruptedException e) {
                if (directie)
                    Main.afisareCuPauza("Prenume: Dragos, Max");
                else
                    Main.afisareCuPauza("Nume: Cocieru, Cuciuc");
                break;
            }
        }
    }
}
