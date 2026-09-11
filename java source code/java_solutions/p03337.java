import java.util.*;
public class Main{
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int p1 = a-b;
        int p2 = a+b;
        int p3 = a*b;
        System.out.println(Math.max(p1, Math.max(p2,p3)));
    }
}
