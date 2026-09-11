import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
       Scanner s = new Scanner (System.in);
       
       int E = s.nextInt();
       int Y = s.nextInt();
       
       if(1912 > Y && Y > 1867){
           int A = Y-1867;
           System.out.print("M");
           System.out.println(A);
       }else if(1926 > Y && Y >1911){
           int B = Y-1911;
           System.out.print("T");
           System.out.println(B);
       }else if(1989 > Y && Y > 1925){
            int C = Y-1925;
            System.out.print("S");
            System.out.println(C);
       }else if(2017 > Y && Y > 1988){
           int D = Y-1988;
           System.out.print("H");
           System.out.println(D);
       }else if(E == 1){
           System.out.println(Y+1867);
       }else if(E == 2){
           System.out.println(Y+1911);
       }else if(E == 3){
            System.out.println(Y+1925);
       }else if(E == 4){
            System.out.println(Y+1988);
       }
}}
