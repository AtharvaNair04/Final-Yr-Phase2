import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			String s = sc.next();
			if (s.equals("0")) {
				break;
			}

			int n = s.length();
			int step = -1;
			boolean flag = false;
			LinkedList<String> list = new LinkedList<String>();
			LinkedList<String> next = new LinkedList<String>();
			HashSet<String> set = new HashSet<String>();

			list.add(s);

			while (!list.isEmpty() && !flag) {
				step++;
				for (String t: list) {
					if (!set.contains(t)) {
						set.add(t);
						char bug[] = t.toCharArray();
						boolean b = true;
						for (int i = 0; i < n - 1; i++) {
							if (bug[i] != bug[i + 1]) {
								b = false;
								char le = bug[i];
								char ri = bug[i + 1];
								char an = le != 'r' && ri != 'r' ? 'r' : le != 'g' && ri != 'g' ? 'g' : 'b';
								bug[i] = an;
								bug[i + 1] = an;
								next.add(new String(bug));
								bug[i] = le;
								bug[i + 1] = ri;
							}
						}
						if (b) {
							flag = true;
							break;
						}
					}
				}
				list = next;
				next = new LinkedList<String>();
			}
			
			System.out.println(flag ? step : "NA");
		}
	}
}
