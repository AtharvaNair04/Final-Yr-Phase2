import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			int cityCnt = scanner.nextInt();
			int routeCnt = scanner.nextInt();

			Map<Integer, Integer> map = initMap(cityCnt);
			// input
			for (int i = 0; i < routeCnt; i++) {
				for (int j = 0; j < 2; j++) {
					int routeA = scanner.nextInt();
					if (map.containsKey(routeA)) {
						map.put(routeA, map.get(routeA) + 1);
					} else {
						map.put(routeA, 1);
					}
				}
			}
			// output
			for (Entry<Integer, Integer> entry : map.entrySet()) {
				System.out.println(entry.getValue());
			}
		}
	}

	private static Map<Integer, Integer> initMap(int cityCnt) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 1; i <= cityCnt; i++) {
			map.put(i, 0);
		}
		return map;
	}
}
