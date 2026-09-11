import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        long a = scanner.nextLong();
        long b = scanner.nextLong();
        long c = scanner.nextLong();
        long d=c-a-b;
        if(4*a*b<d*d && d>0) {
            System.out.println("Yes");
        }else {
            System.out.println("No");
        }
    }
}
