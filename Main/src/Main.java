public class Main {
    public static void main(String[] args) {
        int n = 1000;
        int tab[] = new int[n];
        for (int i = 0; i < n; i++)
            tab[i] = (int) (Math.random() * 100);

        Thread1 Th1 = new Thread1(0, n - 1, tab, true);
        Thread1 Th2 = new Thread1(0, n - 1, tab, false);
        FirCocieruStart Th3 = new FirCocieruStart(100, 500, tab);
        FirCocieruSfarsit Th4 = new FirCocieruSfarsit(300, 700, tab);

        Th1.setName("Th1");
        Th2.setName("Th2");
        Th3.setName("Th3");
        Th4.setName("Th4");

        Th1.start();
        Th2.start();
        Th3.start();
        Th4.start();

        try {
            Th2.join();
            Th2.interrupt();

            Th4.join();
            Th4.interrupt();

            Th1.join();
            Th1.interrupt();

            Th3.join();
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

class FirCocieruStart extends Thread {
    int from, to, tab[];
    public FirCocieruStart(int from, int to, int tab[]) {
        this.from = from; this.to = to; this.tab = tab;
    }
    public void run() {
        System.out.println(getName() + " → Parcurgere crescătoare ["+from+".."+to+"]:");
        for (int i = from; i <= to; i++) {
            System.out.print(tab[i] + " ");
            try { Thread.sleep(2); } catch (Exception ignored) {}
        }
        System.out.println();
        while (!isInterrupted()) {
            try { Thread.sleep(100); } catch (InterruptedException e) {
                Main.afisareCuPauza("Disciplina: Programare Concurenta si Distribuita");
                break;
            }
        }
    }
}

class FirCocieruSfarsit extends Thread {
    int from, to, tab[];
    public FirCocieruSfarsit(int from, int to, int tab[]) {
        this.from = from; this.to = to; this.tab = tab;
    }
    public void run() {
        System.out.println(getName() + " → Parcurgere descrescătoare ["+to+".."+from+"]:");
        for (int i = to; i >= from; i--) {
            System.out.print(tab[i] + " ");
            try { Thread.sleep(2); } catch (Exception ignored) {}
        }
        System.out.println();
        while (!isInterrupted()) {
            try { Thread.sleep(100); } catch (InterruptedException e) {
                Main.afisareCuPauza("Grupa: CR-232");
                break;
            }
        }
    }
}

class Thread1 extends Thread {
    int from, to, tab[];
    boolean directie;
    public Thread1(int from, int to, int tab[], boolean directie) {
        this.from = from; this.to = to; this.tab = tab; this.directie = directie;
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
        } else {
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
            try { Thread.sleep(100); } catch (InterruptedException e) {
                if (directie)
                    Main.afisareCuPauza("Prenume: Dragos");
                else
                    Main.afisareCuPauza("Nume: Cocieru");
                break;
            }
        }
    }
}
