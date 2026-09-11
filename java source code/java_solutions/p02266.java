import java.util.Scanner;

public class Main {
	public static int sumarea(String str, int sdepth, int edepth) {
		int i, len, start = sdepth, end = edepth, depth = 0;
		double area = 0;
		len = str.length();
		for (i = 0; i < len; i++) {
			if (start > end && str.charAt(i) == '\\') {
				start--;
			} else {
				switch (str.charAt(i)) {
					case '\\':
						area += depth - 0.5;
						depth -= 1;
						break;
					case '/':
						area += depth + 0.5;
						depth += 1;
						break;
					case '_':
						area += depth;
						break;
					default:
						continue;
				}
			}
		}
		return (int) -area;
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int i = 0, j = 0, len, start = -1, end = -1, depth = 0, sdepth = 0, edepth = 0, count = 0, memo = 0;
		int sum = 0;
		String str;
		str = scan.next();
		len = str.length();
		j = len / 2 + 1;
		int[] area = new int[j];
		j = 0;
		while (i < len) {
			for (i = memo; i < len; i++) {
				if (start == -1 && str.charAt(i) == '\\') {
					start = i;
					depth = 0;
					sdepth = 0;
				}
				if (start != -1) {
					switch (str.charAt(i)) {
						case '\\':
							depth -= 1;
							break;
						case '/':
							if (end == -1) {
								end = i;
								edepth = depth;
							}
							depth += 1;
							if (depth > edepth) {
								end = i;
								edepth = depth;
							}
							if (edepth == 0) {
								area[j] = sumarea(str.substring(start, end + 1), sdepth, edepth);
								sum += area[j];
								count++;
								j++;
								start = -1;
								end = -1;
							}
							break;
						default:
							continue;
					}
				}
			}
			if (start < end  && sdepth > edepth) {
				area[j] = sumarea(str.substring(start, end + 1), sdepth, edepth);
				sum += area[j];
				count++;
				i = end + 1;
				j++;
				memo = end;
				start = -1;
				end = -1;
			}
			if (i >= len)
				break;
		}
		System.out.print(sum + "\n" + count);
		for (i = 0; i < count; i++) {
				System.out.print(" "+area[i]);
		}
		System.out.println("");
		scan.close();
	}
}
