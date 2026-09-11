import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int N = scan.nextInt();
		String[] C = new String[N];
		for (int i = 0; i < N; i++) {
			C[i] = scan.next();
		}
		// bubble sort
		String[] bubbleC = bubbleSort(Arrays.copyOf(C, N), N);
		String outputB = Arrays.toString(bubbleC).replaceAll(",", "");
		System.out.println(outputB.substring(1, outputB.length() - 1));
		System.out.println("Stable"); // ????????????????????????
		
		// selection sort
		String[] selectionC = selectionSort(Arrays.copyOf(C, N), N);
		String outputC = Arrays.toString(selectionC).replaceAll(",", "");
		System.out.println(outputC.substring(1, outputC.length() - 1));
		String message = "Not stable";
		if (isStable(bubbleC, selectionC)) {
			message = "Stable";
		}
		System.out.println(message); // bubble sort???????????¨??????
		scan.close();
	}

	public static String[] bubbleSort(String[] C, int N) {
		for (int i = 0; i < N; i++) {
			for (int j = N - 1; j > i; j--) {
				
				if ( Character.getNumericValue(C[j].charAt(1)) < Character.getNumericValue(C[j - 1].charAt(1))) {
					String temp = C[j];
					C[j] = C[j - 1];
					C[j - 1] = temp;
				}
			}
		}
		
		return C;
	}
	
	public static String[] selectionSort(String[] C, int N){
		for (int i = 0; i < N; i++) {
			int minj = i;
			for (int j = i; j < N; j++) {
				if (Character.getNumericValue(C[minj].charAt(1)) > Character.getNumericValue(C[j].charAt(1))) {
					minj = j;
				}
			}
			String temp = C[i];
			C[i] = C[minj];
			C[minj] = temp;
		}
		
		return C;
	}
	
	public static boolean isStable(String[] C1, String[] C2){
		for (int i = 0; i < C2.length; i++) {
			if (!C1[i].equals(C2[i])) {
				return false;
			}
		}
		return true;
	}
}
