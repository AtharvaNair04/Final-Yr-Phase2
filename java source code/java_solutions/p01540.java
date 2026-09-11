import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;


public class Main {
	static int N, M;
	static Integer[] TX, TY;
	static Integer[][] ZX, ZY;
			
	static private void solve()
	{
		TreeSet<Integer> tsx = new TreeSet<Integer>();
		TreeSet<Integer> tsy = new TreeSet<Integer>();
		for (int i = 0; i < N; i++) {
			tsx.add(TX[i]);
			tsy.add(TY[i]);
		}
		ArrayList<Integer> alx = new ArrayList<Integer>(tsx);
		ArrayList<Integer> aly = new ArrayList<Integer>(tsy);
		int tsw = tsx.size();
		int tsh = tsy.size();
		Object[] alxobj = alx.toArray();
		Object[] alyobj = aly.toArray();
		
		int[][] treasure = new int[tsy.size() + 1][tsx.size() + 1];
		for (int y = 0; y < tsh+1; y++) {
			for (int x = 0; x < tsw+1; x++) {
				treasure[y][x] = 0;
			}
		}
		for (int i = 0; i < N; i++) {
			int x = alx.indexOf(TX[i]);
			int y = aly.indexOf(TY[i]);
			treasure[y + 1][x + 1]++;
		}
		
		for (int y = 1; y <= tsh; y++) {
			for (int x = 1; x <= tsw; x++) {
				treasure[y][x] += treasure[y-1][x] + treasure[y][x-1] - treasure[y-1][x-1];
			}
		}
		
		
		
		for (int i = 0; i < M; i++) {
			Integer xx1 = tsx.ceiling(ZX[i][0]);
			Integer xx2 = tsx.floor(ZX[i][1]);
			Integer yy1 = tsy.ceiling(ZY[i][0]);
			Integer yy2 = tsy.floor(ZY[i][1]);
			
			if( xx1 == null || xx2 == null || yy1 == null || yy2 == null )
			{
				System.out.println("0");
				continue;
			}
			
			int x1 = Arrays.binarySearch(alxobj, xx1);
			int x2 = Arrays.binarySearch(alxobj, xx2) + 1;
			int y1 = Arrays.binarySearch(alyobj, yy1);
			int y2 = Arrays.binarySearch(alyobj, yy2) + 1;
			
			/*int x1 = alx.indexOf(xx1);
			int x2 = alx.indexOf(xx2) + 1;
			int y1 = aly.indexOf(yy1);
			int y2 = aly.indexOf(yy2) + 1;*/
			
			int ret = treasure[y2][x2] - treasure[y2][x1] - treasure[y1][x2] + treasure[y1][x1];
			System.out.println(ret);
		}
	}
	
	static public void main(String[] args)
	{
		Scanner sca = new Scanner(System.in);
		
		while(true)
		{
			N = sca.nextInt();
			M = sca.nextInt();
			
			TX = new Integer[N];
			TY = new Integer[N];
			for (int i = 0; i < N; i++) {
				TX[i] = new Integer(sca.nextInt());
				TY[i] = new Integer(sca.nextInt());
			}
			
			ZX = new Integer[M][2];
			ZY = new Integer[M][2];
			for (int i = 0; i < M; i++) {
				ZX[i][0] = new Integer(sca.nextInt());
				ZY[i][0] = new Integer(sca.nextInt());
				ZX[i][1] = new Integer(sca.nextInt());
				ZY[i][1] = new Integer(sca.nextInt());
			}
			
			solve();
			
			break;
		}
	}
}
