import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// disjoin set
public class Main {
    static class UnionFindTree {

        private int parent[];
        private int rank[];
        private int weight[];

        public UnionFindTree(int Num_element) {
            parent = new int[Num_element];
            rank = new int[Num_element];
            weight = new int[Num_element];
            for (int i = 0; i < Num_element; i++)
                makeSet(i);
        }

        public void makeSet(int x) {
            parent[x] = x;
            rank[x] = 0;
            weight[x] = 0;
        }

        public void union(int x, int y, int cost) {
            link(findSet(x), findSet(y), cost - weight[x] + weight[y]);
        }

        public int findSet(int x) {
            if (x != parent[x]) {
                int tmp = findSet(parent[x]);;
                weight[x] += weight[parent[x]];
                parent[x] = tmp;
            }
            return parent[x];
        }
        public boolean same(int x,int y){
            return findSet(x) == findSet(y);
        }

        public void link(int x, int y, int cost) {
            if (rank[x] > rank[y]) {
                parent[y] = x;
                weight[y] -= cost;
            } else {
                parent[x] = y;
                weight[x] += cost;
                if (rank[x] == rank[y]) {
                    rank[y]++;
                }
            }
        }

        public int sum(int x) {
            int sum = 0;
            int now = x;
            while ((now != parent[now])) {
                sum += weight[now];
                now = parent[now];
            }
            return sum;
        }

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Disjoin set element number
        int Num_element;

        //Query times
        int Num_query;


        Num_element = sc.nextInt();
        Num_query = sc.nextInt();
        UnionFindTree Disjoin_set = new UnionFindTree(Num_element);
        for (int i = 0; i < Num_query; i++) {
            // '0' denotes unite and '1' denotes same operation.
            int com = sc.nextInt();
            int x = sc.nextInt();
            int y = sc.nextInt();
            if (com == 0) {
                int z = sc.nextInt();
                Disjoin_set.union(x, y, z);
            } else {
                if(!Disjoin_set.same(x,y)){
                    System.out.println("?");
                }else
                    System.out.println(Disjoin_set.sum(x) - Disjoin_set.sum(y));
            }
        }

    }
}
