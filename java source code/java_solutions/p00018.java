import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//		int a = Integer.parseInt(br.readLine());
//		int b = Integer.parseInt(br.readLine());
//		int c = Integer.parseInt(br.readLine());
//		int d = Integer.parseInt(br.readLine());
//		int e = Integer.parseInt(br.readLine());


		@SuppressWarnings("resource")
		Scanner sc = new Scanner (System.in);
		int a = sc.nextInt();
		int b = sc.nextInt();
		int c = sc.nextInt();
		int d = sc.nextInt();
		int e = sc.nextInt();

		List<Integer> list = new ArrayList<Integer>();
		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);
		list.add(e);

		Collections.sort (list);
		Collections.reverse(list);

		for (int i = 0; i < 4; i++){
			System.out.print(list.get(i)+ " ");
		}
		System.out.println(list.get(4));

	}

}
