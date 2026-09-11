import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner t = new Scanner(System.in);
        
        int a = t.nextInt();
        int b = t.nextInt();
        int k = t.nextInt();
        int i;
        int s = 0, menor;
        if (a<=b){
            menor = a;
        } else {
            menor = b;
        }
        for (i = menor; i >= 1; i--) {
            if (a % i == 0 && b % i == 0){
                s++;
            }
            if (s == k){
                System.out.println(i);
                break;
            }
        }
    }   
}
