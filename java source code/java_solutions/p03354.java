import java.util.*;

public class Main{
  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);
    int N = scanner.nextInt();
    int M = scanner.nextInt();
    int[] pArray = new int[N];
    int[] x = new int[M];
    int[] y = new int[M];
    for (int i = 0; i < N; i++){
      pArray[i] = scanner.nextInt() - 1;
    }
    UnionFind uf = new UnionFind(N);
    for (int i = 0; i < M; i++){
      x[i] = scanner.nextInt() - 1;
      y[i] = scanner.nextInt() - 1;
      uf.unite(x[i], y[i]);
    }
    int count = 0;
    for (int i = 0; i < N; i++){
      if(uf.same(i, pArray[i])) count++;
    }
    System.out.println(count);
  }
}

class UnionFind{
  int[] parent;

  public UnionFind(int N){
    this.parent = new int[N];
    for (int i = 0; i < N; i++){
      parent[i] = i;
    }
  }

  public int root(int x){
    if (parent[x] == x) return x;
    else{
      return parent[x] = root(parent[x]);
    }
  }
  public void unite(int x, int y){
    if (same(x, y)) return;
    int xroot = root(x);
    int yroot = root(y);
    parent[xroot] = yroot;
    return;
  }
  public boolean same(int x, int y){
    return (root(x) == root(y));
  }
}
