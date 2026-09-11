import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		LinkedList<Double> list = new LinkedList<Double>();
		while(s.hasNext()){
			list.add(s.nextDouble());
		}
		Collections.sort(list);
		System.out.println(list.get(list.size()-1)-list.get(0));
	}

}
