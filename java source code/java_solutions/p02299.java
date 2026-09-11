public class Main{
  public void run(java.io.InputStream in, java.io.PrintStream out){
    java.util.Scanner sc = new java.util.Scanner(in);
/*answer*/
    int n, q;
    double[] x, y;
    int i, j, countr;
    double x0, y0, ax, ay, bx, by, tmp;

    n = sc.nextInt();
    x = new double[n + 1];
    y = new double[n + 1];

    for(i = 0;i < n;i++){
      x[i] = sc.nextDouble();
      y[i] = sc.nextDouble();
    }
    x[n] = x[0];
    y[n] = y[0];

    q = sc.nextInt();

    for(i = 0;i < q;i++){
      x0 = sc.nextDouble();
      y0 = sc.nextDouble();
      countr = 0;

      for(j = 0;j < n;j++){
        if((x[j] - x0) * (y[j + 1] - y0) - (y[j] - y0) * (x[j + 1] - x0) == 0){
          if((x[j] - x0) * (x[j + 1] - x0) <= 0 && (y[j] - y0) * (y[j + 1] - y0) <= 0){
            break;
          }else if(x0 < x[j + 1] && x0 < x[j] && y0 == y[j] && y0 == y[j + 1]){
            if((y[(j - 1 + n) % n] - y0) * (y[(j + 2) % n] - y0) > 0){
              countr++;
            }
          }
        }

        if((y[j] - y0) * (y[j + 1] - y0) <= 0 && y0 != y[j + 1]){
          ax = x[j];
          ay = y[j];
          bx = x[j + 1];
          by = y[j + 1];

          if(ay > by){
            tmp = ay;
            ay = by;
            by = tmp;
            tmp = ax;
            ax = bx;
            bx = tmp;
          }
          if((ax - x0) * (by - y0) - (bx - x0) * (ay - y0) > 0){
            countr++;
            if((y[(j - 1 + n) % n] - y0) * (y[j + 1] - y0) > 0 && y[j] == y0){
              countr++;
            }
          }
        }
      }

      if(j < n){
        System.out.println("1");
      }else{
        if(countr % 2 == 1){
          System.out.println("2");
        }else{
          System.out.println("0");
        }
      }
    }

    sc.close();
  }
  public static void main(String[] args){
    (new Main()).run(System.in, System.out);
  }
}
