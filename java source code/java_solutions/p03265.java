import java.util.Scanner;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int[] x = new int[4];
    int[] y = new int[4];
    for(int j = 0; j < 2; j++){
      x[j] = sc.nextInt();
      y[j] = sc.nextInt();
    }
    for(int j = 2; j < 4; j++){
      x[j] = x[j -1] - (y[j -1] - y[j -2]);
      y[j] = y[j -1] + (x[j -1] - x[j -2]);
    }
    System.out.print(x[2] + " ");
    System.out.print(y[2] + " ");
    System.out.print(x[3] + " ");
    System.out.println(y[3]);
  }
}
