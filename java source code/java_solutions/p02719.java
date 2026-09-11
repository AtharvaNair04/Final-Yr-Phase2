import java.util.*;

class Main{
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        long k = sc.nextLong();
        if(n==0 || n==k){
            System.out.println(0);
            return;
        }

        long min = n;
        if(n > k){
            n = Math.abs(n - (n/k - 1)*k);
        }
        else if(n<k){
            n = Math.abs(n - (k/n - 1)*n);
        }
        n = Math.min(n, min);
        min = n;
        while(true){
            long tmp = Math.abs(n-k);
            if(tmp < min){
                min = tmp;
                n = tmp;
            }
            else{
                break;
            }
        }

        System.out.println(min);
    }
}
