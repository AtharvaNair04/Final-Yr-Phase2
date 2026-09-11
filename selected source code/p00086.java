//Volume0-0086
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class Main {

	private static TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();

	public static void main(String[] args) {

		//declare
		Iterator<Map.Entry<Integer, Integer>> I;
		String[] s;
		String   result;
		boolean flg;
		int i,v;

        //input
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
        	map.clear();
        	out:while(true){
        		s = sc.nextLine().split("\\s");
        		for(i=0;i<2;i++){
        			v = Integer.parseInt(s[i]);

        	    	//calculate
        			if(v == 0){break out;}
        			if   (map.containsKey(v)){ map.put(v,map.get(v)+1);}
        			else                     { map.put(v,1);           }
        		}
        	}

        	map.put(1,map.get(1)+1);
        	map.put(2,map.get(2)+1);

        	flg = true;
        	I = map.entrySet().iterator();
        	while(I.hasNext()){
       			if (I.next().getValue()%2 != 0){
       				flg = false;
       				break;
       			}
   			}
        	if   (flg){result = "OK";}
        	else      {result = "NG";}

        	//output
        	System.out.println(result);
        }
	}
}
