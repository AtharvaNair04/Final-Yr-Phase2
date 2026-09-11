import java.util.Scanner;
import java.util.HashSet;
public class Main
{
	public static void main (String[] args) throws java.lang.Exception
	{
		final int ROCK = 1;
		final int SCISSORS = 2;
		Scanner sc = new Scanner(System.in);
		while (true) {
			int[] hands = new int[5];
			HashSet<Integer> set = new HashSet<>();
			for (int i = 0; i < 5; i++) {
				int temp = sc.nextInt();
				if (temp == 0) {
					return;
				}
				hands[i] = temp;
				set.add(temp);
			}
			if (set.size() != 2) {	//?????????
				for (int i = 0; i < 5; i++) {
					System.out.println(3);
				}
			} else {
				if (!set.contains(ROCK)) {	//?????§??????????????????
					for (int temp : hands) {
						System.out.println(temp - 1);
					}
				} else if (!set.contains(SCISSORS)) {	//??°??????????????????
					for (int temp : hands) {
						System.out.println(temp % 3 + 1);
					}
				} else {	//??°???????????§?????????
					for (int temp : hands) {
						System.out.println(temp);
					}
				}
			}
		}
	}
}
