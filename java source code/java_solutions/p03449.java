import java.util.*;
public class Main {
  	public static void main(String[] args) {
//		2xNのマス目
//		1,2,3,4..
//		5,6,7,8..
//		スタートは(1, 1)


		Scanner sc = new Scanner(System.in);
      	int N = sc.nextInt();
//      (2,N)
      	ArrayList<Integer> masu1 = new ArrayList<Integer>();
      	ArrayList<Integer> masu2 = new ArrayList<Integer>();
      	for(int i = 0; i < N; i++) {
      		 masu1.add(sc.nextInt());
      	}

      	for(int i = 0; i < N; i++) {
     		 masu2.add(sc.nextInt());
     	}
    	ArrayList<Integer> ans = new ArrayList<Integer>();
      	for (int i = 1; i < N; i++) {
      		int total = 0;
      		int count = i;
      		while (count != 0) {
      			total += masu1.get(count - 1);
      			count--;
      		}
      		for (int j = N; j >= i; j--) {
      			total += masu2.get(j - 1);
      		}
      		ans.add(total);
      	}
      	int total = 0;
      	for(int i = 0; i < N; i++) {
    		 total += masu1.get(i);
    	}
      	total += masu2.get(N-1);
      	ans.add(total);

      	System.out.println(Collections.max(ans));
	}
}
