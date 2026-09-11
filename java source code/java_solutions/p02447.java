import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
public class Main {
	public static void main(String[] args){
		int i, n;
		try (Scanner sc = new Scanner(System.in)) {
			n = sc.nextInt();
			List<Pairs> list = new ArrayList<>();
			for(i = 0; i < n; i++) {
				Pairs pairs = new Pairs(sc.nextInt(),sc.nextInt());
				list.add(pairs);
			}
			Comparator<Pairs> comparator =
					Comparator.comparing(Pairs::getx)
					.thenComparing(Pairs::gety);
			list.stream().sorted(comparator)
			.forEach(a -> System.out.println(a.getx() + " " + a.gety()));
		}
	}
}
class Pairs{
	private int x, y;
	public Pairs(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getx() {
		return this.x;
	}
	public int gety() {
		return this.y;
	}
}
