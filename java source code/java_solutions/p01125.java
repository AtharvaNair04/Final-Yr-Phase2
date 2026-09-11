import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    if(n!=0){
		int[] rob = {10, 10};
		int i;
		int[][] jem = new int[2][n];
		boolean[] jemG = new boolean[n];
		for(i=0; i<n; i++){
		    jem[0][i] = kbd.nextInt();
		    jem[1][i] = kbd.nextInt();
		    jemG[i] = false;
		}
		int m = kbd.nextInt();
		while(m>0){
		    String d = kbd.next();
		    int l = kbd.nextInt();
		    while(l>0){
			move1(d, rob);
			for(i=0; i<n; i++){
			    if(rob[0]==jem[0][i] && rob[1]==jem[1][i]){
				jemG[i] = true;
				break;
			    }
			}
			l--;
		    }
		    m--;
		}
		if(get(jemG)) System.out.println("Yes");
		else System.out.println("No");
	    }
	}
    }

    static void move1(String d, int[] rob){
	     if(d.equals("N")) rob[1]++;
	else if(d.equals("E")) rob[0]++;
	else if(d.equals("S")) rob[1]--;
	else if(d.equals("W")) rob[0]--;
    }

    static boolean get(boolean[] jemG){
	int i;
	boolean ans = true;
	for(i=0; i<jemG.length; i++){
	    ans = ans && jemG[i];
	}
	/*
	for(i=0; i<jemG.length; i++){
	    System.out.println(jemG[i]);
	}
	*/
	return ans;
    }
}
