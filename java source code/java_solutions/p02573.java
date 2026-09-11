import java.util.*;


import java.io.*;
/**
 * MLASERP
 */
public class Main {

    static int [] parent;
    static int [] size;
    static MyScanner in  = new MyScanner();
   static int mod  = 1000000007;
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      static StringTokenizer st;
      static int []dr  = {1,-1,0,0};
      static int [] dc = {0,0,-1,1};
      static int max =1;
public static void main(String[] args) {
    int N = in.nextInt();
    int M = in.nextInt();
    parent(N);
    for(int i=0;i<M;i++){
        int a = in.nextInt();
        int b = in.nextInt();
        union(a, b);
    }
    out.println(max);
   out.flush();
}
static int find_set(int v){
    if(v==parent[v])
        return v;
    return parent[v]=find_set(parent[v]);
}
static void union(int a, int b){
    a =find_set(a);
    b = find_set(b);
    if(a!=b){
        parent[b] =a;
        size[a]+=size[b];
        max = Math.max(size[a], max);
    }
}
static void parent(int V){
    parent = new int[V+1];
    size = new int[V+1];
    for(int i=0;i<=V;i++){
        parent[i] = i;
        size[i] = 1;
    }
}
public static class MyScanner {
      BufferedReader br;
      StringTokenizer st;
 
      public MyScanner() {
         br = new BufferedReader(new InputStreamReader(System.in));
      }
 
      String next() {
          while (st == null || !st.hasMoreElements()) {
              try {
                  st = new StringTokenizer(br.readLine());
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          return st.nextToken();
      }
 
      int nextInt() {
          return Integer.parseInt(next());
      }
 
      long nextLong() {
          return Long.parseLong(next());
      }
 
      double nextDouble() {
          return Double.parseDouble(next());
      }
 
      String nextLine(){
          String str = "";
	  try {
	     str = br.readLine();
	  } catch (IOException e) {
	     e.printStackTrace();
	  }
	  return str;
      }

   }
}
