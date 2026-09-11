import java.util.Scanner;

class Main {

    public static int maxkouyakusu(int a, int b) {
        int l;
        while (true) {
            if (a % b == 0) {
                return b;
            } else {
                l = a % b;
                a = b;
                b = l;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int amntcity = scanner.nextInt();
        int syokiiti = scanner.nextInt();
        int[] cities = new int[amntcity];
        for (int i = 0; i < amntcity; i++) {
            cities[i] = scanner.nextInt();
        }
        scanner.close();

        int[] sa = new int[amntcity];
        for (int i = 0; i < sa.length; i++) {
            sa[i] = Math.abs(cities[i] - syokiiti);
        }

        try {
            int saidaikouyakusu = maxkouyakusu(sa[0], sa[1]);
            for (int i = 2; i < sa.length; i++) {
                int kouyakusu = maxkouyakusu(saidaikouyakusu, sa[i]);
                if (kouyakusu < saidaikouyakusu) {
                    saidaikouyakusu = kouyakusu;
                }
            }
            System.out.println(saidaikouyakusu);
        } catch (Exception e) {
            System.out.println(sa[0]);
        }

    }

}
