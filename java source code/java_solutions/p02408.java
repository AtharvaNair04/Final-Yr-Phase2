import java.util.*;
class Main{
  public static void main(String[] args) {
    Scanner scan= new Scanner(System.in);
    boolean[][] trump = new boolean[4][13];
    char[] head = {'S' , 'H' , 'C' , 'D'};
    int n = scan.nextInt();
    for (int i = 0; i < n ; i++ ) {
      char head_in = scan.next().charAt(0);
      int num = Integer.parseInt(scan.next());

      switch(head_in){
        case 'S':trump[0][num-1] = true;break;
        case 'H':trump[1][num-1] = true;break;
        case 'C':trump[2][num-1] = true;break;
        case 'D':trump[3][num-1] = true;break;
      }
    }
    for (int i =0 ;i < 4 ;i++ ) {
      for (int j = 0;j < 13 ;j++ ) {
        if (trump[i][j] == false) {
          System.out.println(head[i]+" "+(j+1));

        }

      }

    }
  }
}
