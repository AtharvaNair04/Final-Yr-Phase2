import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
public class Main {
	public static void main(String[] args){
		int i, q, order;
		String key, key2, str = "";
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();
		try (Scanner sc = new Scanner(System.in)) {
			q = sc.nextInt();
			for(i = 0; i < q; i++) {
				order = sc.nextInt();
				key = sc.next();
				switch(order) {
				case 0 :
					tm.put(key, sc.nextInt());
					break;
				case 1 :
					if(tm.get(key) == null) {
						System.out.println(0);
					}else {
						System.out.println(tm.get(key));
					}
					break;
				case 2 :
					tm.remove(key);
					break;
				case 3 :
					key2 = sc.next();
					for(Entry<String, Integer> output : tm.subMap(key, true, key2, true).entrySet()) {
						System.out.println(output.getKey() + " " + output.getValue());
					}
					break;
				}
			}
		}
	}
}
