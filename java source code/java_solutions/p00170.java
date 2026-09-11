import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static class Lunch implements Comparable<Lunch>{
		String name;
		int weight;
		int capacity;
		int number;

		Lunch (String name, int weight, int capacity) {
			this.name = name;
			this.weight = weight;
			this.capacity = capacity;
			this.number = 0;
		}

		public int compareTo(Lunch o) {
			if (o.number != this.number) {
				return this.number - o.number;
			} else {
				return o.weight - this.weight;
			}
		}
	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		Lunch lunch[];
		int sumWeight;

		while (true) {
			n = sc.nextInt();
			if (n == 0) {
				break;
			}
			lunch = new Lunch[n];
			sumWeight = 0;
			for (int i = 0; i < n; i++) {
				String name = sc.next();
				int weight = sc.nextInt();
				int capacity = sc.nextInt();
				lunch[i] = new Lunch(name, weight, capacity);
				sumWeight += weight;
			}
			Arrays.sort(lunch);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (lunch[j].number == 0 && sumWeight - lunch[j].weight <= lunch[j].capacity) {
						lunch[j].number = i + 1;
						sumWeight -= lunch[j].weight;
						break;
					}
				}
			}
			Arrays.sort(lunch);
			for (int i = 0; i < n; i++) {
				System.out.println(lunch[i].name);
			}
		}
	}
}
