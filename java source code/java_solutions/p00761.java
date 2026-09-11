import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
			while (true) {

				final int a0 = sc.nextInt();
				final int L = sc.nextInt();

				if (a0 == 0 && L == 0) {
					break;
				}

				Map<Integer, Integer> map = new HashMap<Integer, Integer>();
				map.put(a0, 0);

				int value = a0;
				for (int count = 1;; count++) {
					String value_str = value + "";
					for(int i = value_str.length(); i < L; i++){
						value_str = "0" + value_str;
					}
					char[] value_array = value_str.toCharArray();
					
					Arrays.sort(value_array);
					
					

					StringBuilder max = new StringBuilder(), min = new StringBuilder();
					for (int i = 0; i < value_array.length; i++) {
						max.append(value_array[value_array.length - i - 1]);
						min.append(value_array[i]);
					}

					final int min_value = Integer.parseInt(min.toString());
					final int max_value = Integer.parseInt(max.toString());
					final int next_value = max_value - min_value;

					//System.out.println(value + " " + next_value + " " + count + " " + min_value + " " + max_value);
					
					if (map.containsKey(next_value)) {
						System.out.println(map.get(next_value) + " " + next_value + " " + (count - map.get(next_value)));
						break;
					} else {
						map.put(next_value, count);
					}
					
					value = next_value;
				}
			
		}
	}
}
