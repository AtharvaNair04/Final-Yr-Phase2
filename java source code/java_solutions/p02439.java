import java.util.Scanner;
public class Main{
    public static long max(long a, long b, long c){
        if(a >= b && a >= c)
            return a;
        else if(b >= c)
            return b;
        return c;
    }
    public static long min(long a, long b, long c){
        if(a <= b && a <= c)
            return a;
        else if(b <= c)
            return b;
        return c;
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        long a = sc.nextLong();
        long b = sc.nextLong();
        long c = sc.nextLong();
        System.out.println(min(a,b,c) + " " + max(a,b,c));
    }
}
