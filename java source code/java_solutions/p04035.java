import java.util.Arrays;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jiakang Chen
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        int N = sc.nextInt();
        
        int L = sc.nextInt();
        Rope[] S = new Rope[N];
        
        for (int i = 0; i < N; i++) {
            S[i] = new Rope(i + 1, sc.nextInt());
        }
        
        int x = isValid(S, L);
        if (x != -1) {
            System.out.println("Possible");
            for (int i = 1; i < x; i++) {
                System.out.println(i);
            }
            
            for (int i = N - 1; i > x; i--) {
                System.out.println(i);
            }
            
            System.out.println(x);
        } else {
            System.out.println("Impossible");
        }
    }
    
    public static int isValid(Rope[] S, int L) {
        for (int i = 0; i < S.length - 1; i++) {
            if (S[i].l + S[i + 1].l >= L) {
                return S[i].i;
            }
        }
        
        return -1;
    }
}

class Rope implements Comparable<Rope> {
    public int i;
    public int l;
    
    public Rope(int index, int length) {
        i = index;
        l = length;
    }
    
    public int compareTo(Rope other) {
        return this.l - other.l;
    }
}
