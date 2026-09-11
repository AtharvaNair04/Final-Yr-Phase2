import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		int doughCost, topingCost;
		int doughCal, topingCal[];
		double sumCost, sumCal;

		n = sc.nextInt();
		doughCost = sc.nextInt();
		topingCost = sc.nextInt();
		doughCal = sc.nextInt();
		topingCal = new int[n];
		sumCost = doughCost;
		sumCal = doughCal;
		for (int i = 0; i < n; i++) {
			topingCal[i] = sc.nextInt();
		}
		quickSort(topingCal, 0, n - 1);

		for (int i = 0; i < n; i++) {
			if (sumCal / sumCost < (sumCal + topingCal[i]) / (sumCost + topingCost)) {
				sumCal += topingCal[i];
				sumCost += topingCost;
			} else {
				break;
			}
		}
		System.out.println((int)(sumCal / sumCost));
	}

	public static void quickSort(int a[], int low, int high) {
		if (high <= low) {
			return ;
		}

		int x = a[low];
		int i = low;
		int j = high;

		while (i <= j) {
			while (x < a[i]) {
				i++;
			}
			while (a[j] < x) {
				j--;
			}
			if (i <= j) {
				int swap = a[i];
				a[i] = a[j];
				a[j] = swap;
				i++;
				j--;
			}
		}
		quickSort(a, low, j);
		quickSort(a, i, high);
	}
}
