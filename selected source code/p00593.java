import java.io.*;

class Main {
	public static void main (String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String buf;
		int count = 1;

		try {
			while (!(buf = br.readLine()).equals("0")) {
				int n = Integer.parseInt(buf);
				int scan[][] = new int[n][n];
				int x = 0;
				int y = 0;
				int p = 1;
				boolean flag = false;
				for (int i=0;i<n;i++) {
					if (flag) {
						x = 0;y = i;
						for (int j=0;j<=i;j++) {
							scan[x][y] = p++;
							x++;y--;
						}
						flag = false;
					} else {
						x = i;y = 0;
						for (int j=0;j<=i;j++) {
							scan[x][y] = p++;
							x--;y++;
						}
						flag = true;
					}
				}
				for (int i=n-2;i>=0;i--) {
					if (flag) {
						x = n-i-1;y = n-1;
						for (int j=0;j<=i;j++) {
							scan[x][y] = p++;
							x++;y--;
						}
						flag = false;
					} else {
						x = n-1;y = n-i-1;
						for (int j=0;j<=i;j++) {
							scan[x][y] = p++;
							x--;y++;
						}
						flag = true;
					}
				}
				System.out.println("Case "+count+":");
				for (int i=0;i<n;i++) {
					for (int j=0;j<n;j++) System.out.printf("%3d",scan[i][j]);
					System.out.println("");
				}
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
