import java.util.Scanner;
public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int total = 0;
    int[] num = new int[9];
    for (int i = 0 ; i < 9 ; i++){
      num[i] = sc.nextInt();
      total += num[i];
    }
    int nextTotal1 = num[0] + num[1] + num[3] + num[4];
    int nextTotal2 = num[4] + num[5] + num[7] + num[8];
    int nextTotal3 = num[1] + num[2] + num[4] + num[5];
    int nextTotal4 = num[3] + num[4] + num[6] + num[7];
     int nextTotal5 = num[0] + num[2] + num[6] + num[8];
    if (total % 3 == 0 && nextTotal1 % 2 == 0 && nextTotal1 % 2 == 0 && nextTotal3 % 2 == 0 && nextTotal4 % 2 == 0 && nextTotal5 % 2 == 0) System.out.println("Yes");
    else System.out.println("No");
    sc.close();
  }
}
