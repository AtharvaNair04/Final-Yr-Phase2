import java.util.*;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);

    int p;

    int[][] a = new int[3][3];
    boolean[][] b = new boolean[3][3];

    for(int i=0; i<3; i++){
      for(int j=0; j<3; j++){
        a[i][j] = sc.nextInt();
      }
    }
    int n = sc.nextInt();
    for(int i=0; i<n; i++){
      p = sc.nextInt();
      for(int j=0; j<3;j++){
        for(int k=0; k<3;k++){
          if(a[j][k]==p){
            b[j][k] = true;
          }
        }
      }
    }

    for(int i=0; i<3; i++){
      if(b[i][0] && b[i][1] && b[i][2] == true){
        System.out.println("Yes");
        System.exit(0);
      }
    }
    for(int i=0; i<3; i++){
      if(b[0][i] && b[1][i] && b[2][i] == true){
        System.out.println("Yes");
        System.exit(0);
      }
    }
    if(b[0][0]&&b[1][1]&&b[2][2]==true){
      System.out.println("Yes");
      System.exit(0);
    }
    if(b[0][2]&&b[1][1]&&b[2][0]==true){
      System.out.println("Yes");
      System.exit(0);
    }

    System.out.println("No");


  }

}
