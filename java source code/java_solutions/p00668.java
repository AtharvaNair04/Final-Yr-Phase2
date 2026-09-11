import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Main {
    private static void solve() {
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            final int q = scanner.nextInt();
            final int lim = scanner.nextInt();
            if (q == 0 && lim == 0) {
                break;
            }
            int counter = 0;
            final List<Integer> array = new LinkedList<Integer>();
            for (int i = 0; i < q; i++) {
                final int query = scanner.nextInt();
                final int xi = scanner.nextInt();
                switch (query) {
                    case 0:
                        array.add(xi);
                        counter++;
                        break;
                    case 1:
                        array.remove(xi - 1);
                        counter--;
                        break;
                    case 2:
                        System.out.println(array.get(xi - 1));
                        break;
                    case 3:
                        array.remove(Integer.valueOf(xi));
                        counter--;
                        break;
                }
                if (counter > lim) {
                    array.remove(0);
                    counter--;
                }
            }
            System.out.println("end");
        }
    }

    public static void main(String... args) {
        solve();
    }
}
