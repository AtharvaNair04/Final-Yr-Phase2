import java.util.*;


import java.io.*;
/**
 * MLASERP
 */
public class Main {

    
    static MyScanner in  = new MyScanner();
   static int mod  = 1000000007;
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      static StringTokenizer st;
      static int []dr  = {1,-1,0,0};
      static int [] dc = {0,0,-1,1};
public static void main(String[] args) {
    int N = in.nextInt();
    int [] a = new int[N];
    for(int i=0;i<N;i++){
        a[i] = in.nextInt();
    }
    int max = 1000*1000;
    Seive seive = new Seive(max);
    boolean isPairwiseCoprime = true;
    HashSet<Integer> factor = new HashSet<>();
    int gcdAll = a[0];
    for(int i=0;i<N;i++){
        ArrayList<Integer> flist = seive.factorList(a[i]);
        for(int fac:flist){
            if(fac!=1&&factor.contains(fac)){
                isPairwiseCoprime = false;
            }
        }
        factor.addAll(flist);
        gcdAll = gcd(a[i],gcdAll);
    }
    if(isPairwiseCoprime){
        out.println("pairwise coprime");
    }else if(gcdAll==1){
        out.println("setwise coprime");
    }else{
        out.println("not coprime");
    }
   out.flush();
   

}
static int gcd (int a, int b) {
    return b!=0 ? gcd (b, a % b) : a;
}
static int rcToxy(int r, int c,int W){
    return r*W+c;
}
static class Node{
    int r, c,prev,change;
    Node(int r, int c){
        this.r =r ;
        this.c = c;
    }
}
static class Seive{
    /**
     * Question: You are given N integers less than or equal to A

. Factorize all of them into primes.

Compute Sieve of Eratosthenes as a precalculation, and record
the prime that sieved the number'' to an array D. Foe example, 
D[4]=D[6]=2 and D[35]=5. If x is a prime, let D[x]=x. 
This array can be constructed in a total of O(AloglogA)

, same as Sieve of Eratosthenes.

Since D[x]
is the minimum prime that divides x, 
so with this array, you no longer have to "try diving" when factorizing
(instead you can just divide by D[x]), so the prime factorization of an integer can be achieved 
in an O(logA) time, which is the number of prime factors.
     */
    BitSet bs;
    int [] factor;
    HashSet<Long> primes;
    Seive(int N){
        bs = new BitSet(N+2);
        factor = new int[N+2];
        factor[0] = -1;
        factor[1] = -1;
        primes = new HashSet<>();
        for(long i=2;i<=N;i++){
            if(factor[(int)i]==0){
                primes.add(i);
           for(long j=i+i;j<=N;j+=i){
                if(factor[(int)j]==0) factor[(int)j]=(int)i;
                bs.set((int)j,true);
           }
           factor[(int)i] = (int)i;
        }
        }
    }
    boolean isPrime(int i){
        return primes.contains(i);
    }
    public ArrayList<Integer> factorList(int i){
        ArrayList<Integer> res = new ArrayList<>();
        while(i!=1){
            res.add(factor[i]);
            i/=factor[i];
        }
        return res;
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
