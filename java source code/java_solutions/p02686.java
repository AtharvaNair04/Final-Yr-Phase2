import java.util.*;

public class Main {
	static class Bracket {
		int diff;
		int bottom;
		public Bracket(int diff, int bottom) {
			this.diff = diff;
			this.bottom = bottom;
		}
		
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt();
		ArrayList<Bracket> list1 = new ArrayList<>();
		ArrayList<Bracket> list2 = new ArrayList<>();
		for(int i=0; i<N; i++) {
			char[] c = sc.next().toCharArray();
			int cur = 0;
			int bottom = 0;
			for(int j=0; j<c.length; j++) {
				if(c[j]=='(') {
					cur++;
				} else {
					cur--;
					bottom = Math.min(bottom, cur);
				}
			}
			if(cur>=0)
				list1.add(new Bracket(cur, bottom));
			else
				list2.add(new Bracket(cur, bottom));
		}
		
		Collections.sort(list1, new Comparator<Bracket>() {
			public int compare(Bracket b1, Bracket b2) {
				return - Integer.compare(b1.bottom, b2.bottom);
			}
		});
		
		int pos = 0;

		for(Bracket b : list1) {
			if(pos + b.bottom < 0) {
				System.out.println("No");
				sc.close();
				return;
			}
			pos += b.diff;
		}
		
		Collections.sort(list2, new Comparator<Bracket>() {
			public int compare(Bracket b1, Bracket b2) {
				return Integer.compare(b1.bottom-b1.diff, b2.bottom-b2.diff);
			}
		});
		
		for(Bracket b : list2) {
			if(pos + b.bottom < 0) {
				System.out.println("No");
				sc.close();
				return;
			}
			pos += b.diff;
		}
		
		System.out.println(pos==0 ? "Yes" : "No");

		sc.close();
	}

}
