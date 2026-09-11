import java.util.*;

public class Main { 
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		String[] arr = input.nextLine().split(" ");
		
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		String max = arr[0];
		String frequent = max;
		words.put(max, 1);
		
		
		for (int i = 1; i < arr.length; i++) {
			String word = arr[i];
			
			
			
			if (words.containsKey(word)) {
				words.put(word, words.get(word) + 1);
			} else {
				words.put(word, 1);
			}
			
			if (words.get(word) > words.get(frequent)) {
				frequent = word;
			}
			
			if (word.length() > max.length()) {
				max = word;
			}
		}

		System.out.println(frequent + " " + max);
		
		input.close();
	}	
}
