import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        long a = scanner.nextLong();
        long b = scanner.nextLong();
        long c = scanner.nextLong();
        long k = scanner.nextLong();
        long ans = k;
        if(k <= a){System.out.println(ans);return;}
        else if(k <= a+b){System.out.println(ans-(k-a));return;}
        else{System.out.println(ans-b-(k-a-b)*2);return;}
    }
}
