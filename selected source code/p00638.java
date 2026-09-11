import java.util.Scanner;
import java.util.Arrays;

class Island implements Comparable {
    int m, n;
    public Island(int m, int n) {
        this.m = m;
        this.n = n;
    }

    public int compareTo(Object arg) {
        Island otherIsland = (Island)arg;
        return this.n - otherIsland.n;
    }
}

public class Main {
    void run() {
        Scanner sc = new Scanner(System.in);
        int n;
        while (true) {
            n = sc.nextInt();
            if (n == 0) break;

            Island[] islands = new Island[n];
            for (int i = 0; i < n; i++) {
                islands[i] = new Island(sc.nextInt(), sc.nextInt());
            }
            Arrays.sort(islands);

            int total = 0;
            boolean yes = true;
            for (int i = 0; i < n; i++) {
                total += islands[i].m;
                if (total > islands[i].n) {
                    yes = false; break;
                }
            }

            System.out.println(yes ? "Yes" : "No");
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
