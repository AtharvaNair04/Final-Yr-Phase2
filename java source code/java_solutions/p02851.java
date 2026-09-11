import java.util.*;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int K = sc.nextInt();
		int[] A = new int[N];
		for(int i=0; i<N; i++) {
			A[i] = (sc.nextInt()+K-1)%K;
		}
		
		HashMap<Integer, ArrayList<Integer>> p = new HashMap<>();

		long ans = 0;
		int sum = 0;
		ArrayList<Integer> list = new ArrayList<>();
		list.add(0);
		p.put(0, list);
		for(int i=0; i<N; i++) {
			sum = (sum + A[i])%K;
			if(p.containsKey(sum)) {
				list = p.get(sum);
				int idx = Collections.binarySearch(list, i+1-(K-1));
				if(idx<0)
					idx = -idx-1;
				ans += list.size()-idx;
				list.add(i+1);
				p.put(sum, list);
				
			} else {
				list = new ArrayList<>();
				list.add(i+1);
				p.put(sum, list);
			}
		}

		System.out.println(ans);
		
		sc.close();
	}
}
