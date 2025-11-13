public class Main {

    static int n = 1000;
    static int[] tabCalc = new int[n];
    static int[] tabPar = new int[n];

    static FirCalcInce Th1 = new FirCalcInce();
    static FirCalcSfar Th2 = new FirCalcSfar();
    static FirParcurgereCrescator Th3 = new FirParcurgereCrescator();
    static FirParcurgereDesc Th4 = new FirParcurgereDesc();

    public static void main(String[] args) {

        for (int i = 0; i < n; i++) {
            tabCalc[i] = (int) (Math.random() * 1000);
            tabPar[i] = i;
        }

        Th1.setName("Th1");
        Th2.setName("Th2");
        Th3.setName("Th3");
        Th4.setName("Th4");

        Th1.start();
        Th2.start();
        Th3.start();
        Th4.start();
    }

    public static void afisare(String text) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(100); } catch (Exception e) {}
        }
        System.out.println();
    }

    static class FirCalcInce extends Thread {
        public void run() {

            int S = 0, C = 0, s1 = 0, s2 = 0, pair = 0;

            for (int i = 0; i < n; i++) {
                if (tabCalc[i] % 2 == 0) { S += tabCalc[i]; C++; }

                if (C == 2) {
                    pair++;
                    if (pair == 1) s1 = S;
                    else {
                        s2 = S;
                        System.out.println(getName() + " → Suma = " + (s1 + s2));
                        pair = 0;
                    }
                    S = 0;
                    C = 0;
                }
                try { Thread.sleep(1); } catch (Exception e) {}
            }

            while (Th4.isAlive())
                try { Thread.sleep(50); } catch (Exception e) {}

            Main.afisare("Prenume: Dragos, Max");
        }
    }

    static class FirCalcSfar extends Thread {
        public void run() {

            int S = 0, C = 0, s1 = 0, s2 = 0, pair = 0;

            for (int i = n - 1; i >= 0; i--) {
                if (tabCalc[i] % 2 == 0) { S += tabCalc[i]; C++; }

                if (C == 2) {
                    pair++;
                    if (pair == 1) s1 = S;
                    else {
                        s2 = S;
                        System.out.println(getName() + " → Suma = " + (s1 + s2));
                        pair = 0;
                    }
                    S = 0;
                    C = 0;
                }
                try { Thread.sleep(1); } catch (Exception e) {}
            }

            Main.afisare(currentThread().getName() + " Nume: Cocieru, Cuciuc");
        }
    }

    static class FirParcurgereCrescator extends Thread {
        public void run() {

            for (int i = 100; i <= 500; i++) {
                System.out.print(tabPar[i] + " ");
                try { Thread.sleep(2); } catch (Exception e) {}
            }
            System.out.println();

            while (Th1.isAlive())
                try { Thread.sleep(50); } catch (Exception e) {}

            Main.afisare(currentThread().getName() + " Disciplina: Programare Concurenta si Distribuita");
        }
    }

    static class FirParcurgereDesc extends Thread {
        public void run() {

            for (int i = 700; i >= 300; i--) {
                System.out.print(tabPar[i] + " ");
                try { Thread.sleep(2); } catch (Exception e) {}
            }
            System.out.println();

            while (Th2.isAlive())
                try { Thread.sleep(50); } catch (Exception e) {}

            Main.afisare("Grupa: CR-232");
        }
    }
}
