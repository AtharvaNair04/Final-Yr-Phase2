import java.util.ArrayDeque;
import java.util.Scanner;

public class Main {

	private static ArrayDeque<Character> q = new ArrayDeque<Character>();

	public static void main(String[] args) {

        //input
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());

        char[] c;
        while(n-- > 0){
        	c = sc.nextLine().toCharArray();
        	q.add(c[0]);
        	char t;
        	for(int i=1;i<c.length;i++){
        		t = c[i];
       			i+=2;
       			if(!q.contains(c[i])){
       				if(t == '<'){q.addFirst(c[i]);}
       				if(t == '-'){q.addLast(c[i]);}
   				}
        	}
        	for(char ch:q){System.out.print(ch);}
        	System.out.println();
        	q.clear();
        }
	}
}
