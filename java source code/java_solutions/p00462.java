import java.util.Arrays;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		while(true){
			int result = 0;
			int distance = Integer.parseInt(s.nextLine());
			if(distance==0)
				break;
			int shopCnt = Integer.parseInt(s.nextLine());
			int orderCnt = Integer.parseInt(s.nextLine());
			int[] load = new int[shopCnt];
			load[0] = 0;
			for(int i=1 ; i<shopCnt ; i++) {
				load[i] = Integer.parseInt(s.nextLine());
			}
			Arrays.sort(load);
			for(int i=0 ; i<orderCnt ; i++) {
				int p = Integer.parseInt(s.nextLine());
				int position = Arrays.binarySearch(load, p);
				position = position >= 0 ? position : ~position;
				if(position == load.length) {
					result += Math.min(distance - p, p - load[position-1]);
				}else if(position==0) {
					result += Math.min(load[position] - p, load[load.length-1] - p);
				}else {
					result += Math.min(load[position] - p, p - load[position-1]);
				}
			}
		System.out.println(result);
		}
	}
}
