import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner scan = new Scanner(System.in);
		// Scanner scan = new Scanner(new File("c:\\0519-in7.txt"));
		while (scan.hasNext()) {
			int n = scan.nextInt();
			if (n == 0)
				break;
			int m = scan.nextInt();

			Match match = new Match(n);
			for (int i = 0; i < m; i++)
				match.add(scan.nextInt(), scan.nextInt());
			for (int i = 0; i < n; i++)
				System.out.println(match.getTopOrder());
			System.out.println(match.getDefined());
		}

		scan.close();
		System.exit(0);
	}

}

class Match {
	class Team {
		int loseCnt = 0;
		List<Integer> winTo = new ArrayList<Integer>();
	}

	Team[] team;
	boolean vague = false;

	public Match(int n) {
		team = new Team[n + 1];
		for (int i = 1; i < team.length; i++)
			team[i] = new Team();
	}

	public int getDefined() {
		if (vague)
			return 1;
		else
			return 0;
	}

	public int getTopOrder() {
		int top = 0;
		for (int i = 1; i < team.length; i++) {
			if (team[i].loseCnt == 0) {
				if (top != 0) {
					vague = true;
					break;
				}
				top = i;
				team[i].loseCnt = -1;
				if (vague)
					break;
			}
		}
		for (int i = 0; i < team[top].winTo.size(); i++)
			team[team[top].winTo.get(i)].loseCnt--;
		return top;
	}

	public void add(int winner, int loser) {
		team[loser].loseCnt++;
		team[winner].winTo.add(loser);
	}
}
