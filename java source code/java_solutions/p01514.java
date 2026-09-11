import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			int t = sc.nextInt();
			int p = sc.nextInt();
			int r = sc.nextInt();
			if (t+p+r==0) {
				break;
			}
			Team[] teams = new Team[t];
			for(int i=0;i<t;i++) {
				teams[i] = new Team(i, p);
			}
			for(int i=0;i<r;i++) {
				int tid = sc.nextInt()-1;
				int pid = sc.nextInt()-1;
				int time = sc.nextInt();
				String message = sc.next();
				if (message.equals("WRONG")) {
					teams[tid].wrong(time, pid);
				}else{
					teams[tid].correct(time, pid);
				}
			}
			Arrays.sort(teams);
			for(int i=0;i<t;i++) {
				System.out.println(teams[i]);
			}
		}
	}

}
class Team implements Comparable<Team>{
	int id;
	int ac = 0;
	int penalty = 0;
	int[] wa;
	public Team(int id,int problemnum) {
		this.id = id;
		wa = new int[problemnum];
	}
	public void correct(int time,int id) {
		ac++;
		penalty += wa[id] * 1200 + time;
	}
	public void wrong(int time,int id) {
		wa[id]++;
	}
	public int compareTo(Team o) {
		int temp = (ac-o.ac) * -1;
		if (temp != 0) {
			return temp;
		}
		temp = penalty-o.penalty;
		if (temp != 0) {
			return temp;
		}
		return id- o.id;
	}
	public String toString() {
		return (id+1) + " " + ac + " " + penalty;
	}
}
