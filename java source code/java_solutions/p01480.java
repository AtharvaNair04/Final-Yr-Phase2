import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }

    Scanner kbd = new Scanner(System.in);
    double[] dice; // テヲツ慊淌・ツセツ?・ツ?、テ、ツソツ敕・ツュツ?
    int t, n, m, i, j;
    double v, r, p;

    void run(){
	while(kbd.hasNext()){
	    t = kbd.nextInt();
	    dice = new double[t+1];
	    for(i=0; i<t+1; i++){
		n = kbd.nextInt();
		m = kbd.nextInt();
		p = 0;
		for(j=0; j<m; j++){
		    v = kbd.nextDouble();
		    r = kbd.nextDouble();
		    p += r;
		    dice[i] += v*r;
		    // System.out.println("*"+v+" "+r);
		}
		dice[i] = dice[i]/p;
	        //System.out.println(dice[i]+" "+(p+1));
	    }

	    boolean ans = false;
	    for(i=0; i<t; i++){
		if(dice[i] > dice[t]){
		    if(Math.abs(dice[i]-dice[t]) > 0.0000001)
			ans = true;
		}
	    }
		
	    if(ans)
		System.out.println("YES");
	    else
		System.out.println("NO");
	}
    }
}
