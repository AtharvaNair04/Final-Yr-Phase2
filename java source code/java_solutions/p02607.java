import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
            int n = input.nextInt();
            int []a = new int[n];
            int count = 0;
            for(int i = 1; i <= n; i++){
                a[i -1] = input.nextInt();
                if((i%2 != 0 && a[i-1] % 2 !=0))
                    count++;
            }
        System.out.println(count);

    }
}
