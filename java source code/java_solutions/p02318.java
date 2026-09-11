import java.io.*;
import java.util.*;
 
public class Main {
	private static InputStreamReader isr;
	private static BufferedReader br;
	
	public static void main(String args[]){
		isr = new InputStreamReader(System.in);
		br = new BufferedReader(isr);
		
		System.out.println(Levenshtein( getstrp(), getstrp() ));
	}
	
	public static char[] getstrp(){
	    String buf;
		try{
			buf = br.readLine();
		}catch(Exception e){
			buf = "";
		}
		buf=" "+buf;
		return buf.toCharArray();
	}
	
	public static int Levenshtein(char[] a, char[] b){
	    int al=a.length, bl=b.length;
	    int[][] state = new int[al][bl];
	    int v=0, h=0;
	    state[v][h]=0;
	    for(h=1; h<bl; h++){
	        state[v][h]=state[v][h-1]+1;
	    }
	    for(v=1; v<al; v++){
	        h=0;
	        state[v][h]=state[v-1][h]+1;
	        for(h=1; h<bl; h++){
	            if(a[v]==b[h]){
	                state[v][h]=Math.min(Math.min(state[v-1][h],state[v][h-1]),state[v-1][h-1]-1)+1;
	            }else{
	                state[v][h]=Math.min(Math.min(state[v-1][h],state[v][h-1]),state[v-1][h-1])+1;
	            }
	            
	        }
	    }
	    return state[al-1][bl-1];
	}
	
}
