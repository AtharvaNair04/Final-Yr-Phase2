import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
 
 
public class Main {
     
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
         
        while(true){
        	final int m = sc.nextInt();
        	
        	if(m == 0){
        		break;
        	}
        	
        	int[] next = new int[m + 1];
        	Arrays.fill(next, -1);
        	boolean[] base = new boolean[m + 1];
        	Arrays.fill(base, true);
        	
        	while(true){
        		final int I = sc.nextInt();
        		final int J = sc.nextInt();
        		
        		//System.out.println(Arrays.toString(next));
        		//System.out.println(Arrays.toString(base));
        		
        		if(I == 0 && J == 0){
        			break;
        		}
        		
        		if(base[I] && J == 0){
        			continue;
        		}else if(I == J){
        			continue;
        		}
        		
        		//check
        		boolean flag = true;
        		if(J != 0){
        			int cur = J;
        			
        			while(cur != -1){
        				cur = next[cur];
        				
        				if(cur == I){
        					flag = false;
        					break;
        				}
        			}
        		}
        		
        		if(!flag){
        			continue;
        		}
        		
        		//broke_up_I
        		{
        			int cur = I;
        			

        			for(int i = 1; i <= m; i++){
        				if(next[i] == cur){
        					next[i] = -1;
        				}
        			}
        			
        			while(cur != -1){
        				base[cur] = true;
        				int old = cur;
        				cur = next[cur];
        				next[old] = -1;
        			}
        		}
        		
        		if(J == 0){
        			continue;
        		}else{
        			int limit = J;
        			
        			while(next[limit] != -1){
        				limit = next[limit];
        			}
        			
        			next[limit] = I;
        			base[I] = false;
        		}
        	}
        	
        	ArrayList<Integer> list = new ArrayList<Integer>();
        	for(int i = 1; i <= m ; i++){
        		if(!base[i]){
        			continue;
        		}
        		
        		int height = 0;
        		int cur = i;
        		while(cur != -1){
        			cur = next[cur];
        			height++;
        		}
        		
        		list.add(height);
        	}
        	
        	Collections.sort(list);
        	for(int number : list){
        		System.out.println(number);
        	}
        	System.out.println("end");
        }
    }
 
}
