import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

class Main {
    public static void main (String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Baggage[] w = new Baggage[n];
        boolean[] checked = new boolean[n];
        for (int i = 0; i < n; i ++) {
            w[i] = new Baggage(sc.nextInt(), i);
        }
        Arrays.sort(w);

        int i, j, swap;
        int cost = 0;
        int min = w[0].weight;
        for (i = 0; i < n; i++) {
            if (checked[i]) continue;
            checked[i] = true;
            swap = 0;
            j = w[i].index;

            while (j != i) {
                checked[j] = true;
                cost += w[j].weight;
                swap++;
                j = w[j].index;
            }
            cost += Math.min(w[i].weight * swap, min * swap + (min + w[i].weight) * 2);
        }

        System.out.println(cost);
    }
}

class Baggage implements Comparable<Baggage> {
    public int weight;
    public int index;

    public Baggage(int weight, int index) {
        this.weight = weight;
        this.index = index;
    }

    @Override
    public int compareTo(Baggage b) {
        if (weight < b.weight) return -1;
        else if (weight > b.weight) return 1;
        else return 0;
    }
}
