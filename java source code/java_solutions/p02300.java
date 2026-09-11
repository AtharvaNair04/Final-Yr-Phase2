public class Main{
  static int infi = 10001;
  public void run(java.io.InputStream in, java.io.PrintStream out){
    java.util.Scanner sc = new java.util.Scanner(in);
/*answer*/
    int n;
    int[] x, y;
    int i, numr, numl, ncon;
    int[] resx, resy, anslx, ansly;

    n = sc.nextInt();
    x = new int[n];
    y = new int[n];
    resx = new int[n];
    resy = new int[n];

    for(i = 0;i < n;i++){
      x[i] = sc.nextInt();
      y[i] = sc.nextInt();
    }
    sc.close();

    mergesort(x, y, 0, n);

    numr = rcovering(x, y, resx, resy);
    if(n == numr){
      ncon = numr;
    }else{
      anslx = new int[n];
      ansly = new int[n];
      reverse(x, y);
      numl = rcovering(x, y, anslx, ansly);

      ncon = numr + numl;
      for(i = 0;i < numl;i++){
        resx[numr + i] = anslx[i];
        resy[numr + i] = ansly[i];
      }
    }

    System.out.println(ncon);
    for(i = 0;i < ncon;i++){
      System.out.println(resx[i] + " " + resy[i]);
    }

  }
  public static void main(String[] args){
    (new Main()).run(System.in, System.out);
  }

  private static int rcovering(int[] x, int[] y, int[] ansx, int[] ansy){
    int i, n, num;
    n = x.length;

    ansx[0] = x[0];
    ansy[0] = y[0];
    ansx[1] = x[1];
    ansy[1] = y[1];
    num = 2;
    for(i = 2;i < n;i++){
      ansx[num] = x[i];
      ansy[num] = y[i];
      num = gscan(ansx, ansy, num);
    }
    if(ansx[num - 1] == x[n - 1] && ansy[num - 1] == y[n - 1])num--;

    return num;
  }

  private static void mergesort(int[] x, int[] y, int l, int r){
    int m, c;
    if(l + 1 >= r)return;
    else{
      m = (l + r) / 2;
      mergesort(x, y, l, m);
      mergesort(x, y, m, r);
      merge(x, y, l, m, r);
      return;
    }
  }
  private static void merge(int[] x, int[] y, int l, int m, int r){
    int nl, nr, count;
    int i, j, k;
    int[] xl, xr, yl, yr;

    nl = m - l;
    nr = r - m;
    xl = new int[nl + 1];
    xr = new int[nr + 1];
    yl = new int[nl + 1];
    yr = new int[nr + 1];
    for(i = 0;i < nl;i++){
      xl[i] = x[l + i]; yl[i] = y[l + i];
    }
    for(i = 0;i < nr;i++){
      xr[i] = x[m + i]; yr[i] = y[m + i];
    }
    xl[nl] = infi; xr[nr] = infi;
    yl[nl] = infi; yr[nr] = infi;

    i = 0;j = 0;count = 0;

    for(k = l;k < r;k++){
      if(alow(xl[i], yl[i], xr[j], yr[j])){
        x[k] = xl[i]; y[k] = yl[i++];
      }else{
        x[k] = xr[j]; y[k] = yr[j++];
      }
    }

    return;
  }
  private static boolean alow(int ax, int ay, int bx, int by){
    if(ay < by || (ay == by && ax <= bx)){
      return true;
    }else{
      return false;
    }
  }
  private static void reverse(int[] x, int[] y){
    int n, tmp, i;
    n = x.length;
    for(i = 0;i < n / 2;i++){
      tmp = x[i];
      x[i] = x[n - 1 - i];
      x[n - 1 - i] = tmp;
      tmp = y[i];
      y[i] = y[n - 1 - i];
      y[n - 1 - i] = tmp;
    }
    return;
  }
  private static int gscan(int[] ansx, int[] ansy, int num){
    int s1x, s1y, s2x, s2y;
    for(;num > 0;){
      if(num == 1)return ++num;
      s1x = ansx[num - 1] - ansx[num];
      s1y = ansy[num - 1] - ansy[num];
      s2x = ansx[num - 2] - ansx[num];
      s2y = ansy[num - 2] - ansy[num];
      if(s1x * s2y - s2x * s1y > 0){
        ansx[num - 1] = ansx[num];
        ansy[num - 1] = ansy[num];
        num--;
      }else{
        num++;
        return num;
      }
    }
    return num;
  }
}
