import java.util.Scanner;

public class Main {

	static int count = 0;
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++)
        	a[i] = sc.nextInt();
        mergeSort(a, 0, n);

        System.out.print(a[0]);
        for (int i = 1; i < n; i++) {
            System.out.print(" " +a[i]);
        }
        System.out.println();
        System.out.println(count);
	}

	static void mergeSort(int[] a, int left, int right) {
		if(left + 1 < right) {
			int mid = (left + right) / 2;
			mergeSort(a, left, mid);
			mergeSort(a, mid, right);
			merge(a, left, mid, right);
		}
	}

	static void merge(int[] a, int left, int mid, int right) {
		int n1 = mid - left;
		int n2 = right - mid;
		int[] L = new int[n1 + 1];
		int[] R = new int[n2 + 1];

		for(int i = 0; i < n1; i++) {
			L[i] = a[left + i];
		}
		for(int i = 0; i < n2; i++) {
			R[i] = a[mid + i];
		}

		L[n1] = Integer.MAX_VALUE;
		R[n2] = Integer.MAX_VALUE;

		int l = 0;
		int r = 0;
		for(int k = left; k < right; k++) {
			count++;
			if(L[l] < R[r]) {
				a[k] = L[l];
				l++;
			}
			else {
				a[k] = R[r];
				r++;
			}
		}
	}
}
