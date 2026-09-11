import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
 
 
public class Main {
     
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
         
        final int m = sc.nextInt();
        
        for(int tt = 0; tt < m; tt++){
        	boolean[][] map = new boolean[5][5];
        	
        	for(int i = 0; i < 5; i++){
        		for(int j = 0; j < 5; j++){
        			map[i][j] = sc.nextInt() == 1;
        		}
        	}
        	
        	int max = 0;
        	
        	for(int i = 0; i < 5; i++){
        		for(int j = 0; j < 5; j++){
        			loop:
        			for(int k = i; k < 5; k++){
        				for(int l = j; l < 5; l++){
        					for(int p = i; p <= k; p++){
        						for(int q = j; q <= l; q++){
        							if(!map[p][q]){
        								continue loop;
        							}
        						}
        					}
        					
        					max = Math.max(max, (k - i + 1) * (l - j + 1));
        				}
        			}
        		}
        	}
        	
        	System.out.println(max);
        }
         
         
    }
 
}
