import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);
		int count = sc.nextInt();
		int q = sc.nextInt();

		int totalTime = 0;
		String[] queue = new String[200000];
		int[] restTime = new int[200000];

		for(int i = 0; i < count; i++) {
			String name = sc.next();
			int time = sc.nextInt();

			queue[i] = name;
			restTime[i] = time;
		}

		int qhead = 0;
		int qtail = count;

		while(qhead != qtail) {
			if(restTime[qhead] <= q) {
				totalTime += restTime[qhead];
				System.out.println(queue[qhead] + " " + totalTime);
				qhead++;
			} else {
				queue[qtail] = queue[qhead];
				restTime[qtail] = restTime[qhead] - q;
				totalTime += q;
				qhead++;
				qtail++;
			}
		}

	}

}
