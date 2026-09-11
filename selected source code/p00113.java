//Volume1-0113
import java.util.*;

public class Main {

    public static void main(String[] args) {

    	//declare
    	int     p,q,quo,rem;
    	String  ans,rec_p;
    	boolean rec;
    	ArrayList<Integer> Rem = new ArrayList<Integer>();
    	
    	//input
    	Scanner sc = new Scanner(System.in);
    	while(sc.hasNext()){
    		p = sc.nextInt();
    		q = sc.nextInt();
    		
    		//initialize
    		Rem.clear();
    		rem = p%q;
    		p   = rem*10;
    		Rem.add(rem);
    		ans = "";
    		
    		//calculate
    		while(true) {
    			quo = p/q;
    			rem = p%q;
    			ans += quo;
    			if(Rem.contains(rem)){rec = true;  break;}
    			if(rem == 0)         {rec = false; break;}
    			Rem.add(rem);
    			p = rem*10;
    		}
    		//output
    		System.out.println(ans);
    		if(rec){
	    		rec_p = "";
    			for(int i=0;i<ans.length();i++){
    				if   (i>=Rem.indexOf(rem)){rec_p += "^";}
    				else                      {rec_p += " ";}
    			}
    			System.out.println(rec_p);
    		}
    	}
    }
}
