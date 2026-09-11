import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int len = scanner.nextInt();
		int nums[] = new int[len];
		int counts = 0;

		for (int i = 0; i < len; i++) {
			nums[i] = scanner.nextInt();
		}
		int flag = 1;
		while (flag == 1) {
			flag = 0;
			if (nums.length > 1) {
				for (int i = 1; i < nums.length; i++) {
					if (nums[i-1] > nums[i]) {
						int tmp = nums[i-1];
						nums[i-1] = nums[i];
						nums[i] = tmp;
						flag = 1;
						counts++;
					}
				}
			}
		}

		new Main().arrayToString(nums);
		System.out.println(counts);
	}

	public void arrayToString(int[] arr) {
		for (int i = 0; i < arr.length; i++) {

			if (i == arr.length - 1) {
				System.out.println(arr[i]);
			} else {
				System.out.print(arr[i] + " ");
			}
		}
	}

}
