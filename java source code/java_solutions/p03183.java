import java.util.*;
import java.io.*;
class Main {
    static class Triplet implements Comparable<Triplet> {
        int weight;
        int strength;
        long value;
        Triplet(int weight, int strength, long value) {
            this.weight = weight;
            this.strength = strength;
            this.value = value;
        }
        public int compareTo(Triplet other) {
            return this.weight+this.strength-other.weight-other.strength;
        }
    }
    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] weights = new int[n];
        int[] strengths = new int[n];
        long[] values = new long[n];
        Triplet[] triplets = new Triplet[n];
        int maxWeight = 0;
        for(int i = 0; i<n; i++) {
            weights[i] = sc.nextInt();
            strengths[i] = sc.nextInt();
            values[i] = sc.nextLong();
            triplets[i] = new Triplet(weights[i], strengths[i], values[i]);
            maxWeight = Math.max(maxWeight, weights[i]);
        }
        Arrays.sort(triplets);
        long[] dp = new long[n*maxWeight+1];
        for(int i = 0; i<n; i++) {
            for(int j = triplets[i].strength; j>=0; j--) {
                int newWeight = j+triplets[i].weight;
                if(newWeight<dp.length)
                    dp[newWeight] = Math.max(dp[newWeight], dp[j]+triplets[i].value);
            }
        }
        long max = 0;
        for(long i: dp)
            max = Math.max(max, i);
        System.out.println(max);
    }
}
