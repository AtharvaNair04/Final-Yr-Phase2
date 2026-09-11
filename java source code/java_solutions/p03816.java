import java.util.*;
import java.io.*;
public class Main{
    public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		PrintWriter ou = new PrintWriter(System.out);
		int n = Integer.parseInt(sc.next());
		Map<Integer , Integer> tm = new TreeMap<Integer , Integer>();
		int a;
		for(int i = 0 ; i < n ; i++){
			a = Integer.parseInt(sc.next());
			if(tm.containsKey(a)){
				tm.put(a , tm.get(a) + 1);
			}else{
				tm.put(a , 1);
			}
		}
		sc.close();
		if(tm.size() % 2 == 0) ou.println(tm.size() - 1);
		else ou.println(tm.size());
		ou.flush();
    }
}
