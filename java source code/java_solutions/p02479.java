import java.util.Scanner;

public class Main{
public static void main(String[] args){
double p = Math.PI;
Scanner sc = new Scanner(System.in);
Double x = sc.nextDouble();
System.out.printf("%f %f",p*x*x,2*x*p);
}
}
