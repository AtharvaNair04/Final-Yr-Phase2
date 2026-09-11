public class Main{
  public void run(java.io.InputStream in, java.io.PrintStream out){
    java.util.Scanner sc = new java.util.Scanner(in);
    int n, m;
    int[] c, d, count;
    int i, j, ans;

    n = sc.nextInt(); m = sc.nextInt();
    c = new int[m]; d = new int[m]; count = new int[n];

    for(i = 0;i < m;i++){
      c[i] = sc.nextInt(); d[i] = sc.nextInt();
      for(j = c[i];j < d[i];j++)count[j]++;
    }

    ans = n + 1;
    for(i = 0;i < n;i++)if(count[i] > 0)ans+=2;

    out.println(ans);

    sc.close();
  }
  public static void main(String[] args){
    (new Main()).run(System.in, System.out);
  }
}
