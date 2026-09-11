import java.util.Scanner;
import java.util.TreeSet;
public class Main {
	public static void main(String[] args){
		int i, j, q, order, x, L, R, next;
		TreeSet<Integer> set = new TreeSet<Integer>();
		try (Scanner sc = new Scanner(System.in)) {
			q = sc.nextInt();
			for(i = 0; i < q; i++) {
				order = sc.nextInt();
				x = sc.nextInt();
				switch(order) {
				case 0 :
					set.add(x);
					System.out.println(set.size());
					break;
				case 1 :
					System.out.println(set.contains(x) ?  "1" : "0");
					break;
				case 2 :
					set.remove(x);
					break;
				case 3 :
					for(Integer output : set.subSet(x, true, sc.nextInt(), true)) {
						System.out.println(output);
					}
					break;
				}
			}
		}
	}
}
