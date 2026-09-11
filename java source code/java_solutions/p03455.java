// package app;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        java.util.Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        if ((a*b)%2 == 0)
            System.out.println("Even");
        else
            System.out.println("Odd");
        }
}
