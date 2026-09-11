import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int l = sc.nextInt();
		int[] a = new int[n+4];
		for(int i=0;i<n;i++) {
			a[i+2] = sc.nextInt();
		}
		PriorityQueue<Icicle> growing = new PriorityQueue<Icicle>();
		for(int i=2;i<n+2;i++) {
			if (a[i] >= a[i-1] && a[i] >= a[i+1]) {
				growing.add(new Icicle(i, l-a[i]));
			}
		}
		int ans = 0;
		while(!growing.isEmpty()) {
			Icicle icicle = growing.poll();
			int i = icicle.pos;
			int t = icicle.time;
			a[i] = 0;
			if (a[i-1] > a[i-2]) {
				growing.offer(new Icicle(i-1, t+l-a[i-1]));
			}
			if (a[i+1] > a[i+2]) {
				growing.offer(new Icicle(i+1, t+l-a[i+1]));
			}
			ans = t;
		}
		System.out.println(ans);
	}
}
class Icicle implements Comparable<Icicle>{
	int pos;
	int time;
	public Icicle(int pos,int time) {
		this.pos = pos;
		this.time = time;
	}
	public boolean equalsTo(Object o) {
		if (o instanceof Icicle) {
			return time == ((Icicle) o).time;
		}else{
			return false;
		}
	}
	@Override
	public int compareTo(Icicle o) {
		return time - o.time;
	}

}
