import java.util.*;


public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N=in.nextInt();
        int sum=0;
        for (int i = 0; i < N; i++) {
            if (in.nextInt()%2==1) {
                sum++;
            }
        }
        if(sum%2==0) System.out.println("YES");
        else System.out.println("NO");
        }

        }
