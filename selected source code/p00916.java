import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;


public class Main {

	private static int[] xDelta = {-1, 1, 0, 0},
						 yDelta = {0, 0, -1, 1};
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int n = in.nextInt();
		while (n != 0)
		{
			ArrayList<Rect> rects = new ArrayList<Rect>();
			TreeSet<Integer> 	xCoors = new TreeSet<Integer>(),
								yCoors = new TreeSet<Integer>();
			
			for (int i = 0; i < n; i++)
			{
				int l = in.nextInt(),
						t = in.nextInt(),
						r = in.nextInt(),
						b = in.nextInt();
				
				Rect rec = new Rect(l, t, r, b);
				rects.add(rec);
				
				xCoors.add(l);
				xCoors.add(r);
				yCoors.add(t);
				yCoors.add(b);
			}
			
			ArrayList<Integer> uniqueX = new ArrayList<Integer>(xCoors);
			ArrayList<Integer> uniqueY = new ArrayList<Integer>(yCoors);
			
			Collections.sort(uniqueX);
			Collections.sort(uniqueY);
			
			int xn = uniqueX.size()*2 + 1,
				yn = uniqueY.size()*2 + 1;
			
			int[][] g = new int[xn][yn];
			
			for (Rect r : rects)
			{
				int x1i = uniqueX.indexOf(r.x1)*2 + 1,
					x2i = uniqueX.indexOf(r.x2)*2 + 1,
					y1i = uniqueY.indexOf(r.y1)*2 + 1,
					y2i = uniqueY.indexOf(r.y2)*2 + 1;
				
				for (int xi = x1i; xi <= x2i; xi++)	
				{
					g[xi][y1i] = -1;
					g[xi][y2i] = -1;
				}
				for (int yi = y1i; yi <= y2i; yi++)	
				{
					g[x1i][yi] = -1;
					g[x2i][yi] = -1;
				}
			}
			
			int regions = 0; 
			for (int x = 0; x < xn; x++)
			{
				for (int y = 0; y < yn; y++)
				{
					if (g[x][y] == 0)
					{
						g[x][y] = 1;
						
						Tuple p = new Tuple(x,y);
						regions++;
						Queue<Tuple> q = new ArrayDeque<Tuple>();
						q.add(p);
						
						while (!q.isEmpty())
						{
							Tuple t = q.poll();
							
							for (int i = 0; i < 4; i++)
							{
								int newX = t.x + xDelta[i],
									newY = t.y + yDelta[i];
								
								if (newX >= 0 && newX < xn && newY >= 0 && newY < yn)
								{
									if (g[newX][newY] == 0)
									{
										g[newX][newY] = 1;
										q.add(new Tuple(newX, newY));
									}
								}
							}
						}
					}
				}
			}
			
			System.out.println(regions);
			
			n = in.nextInt();
		}
	}
	
	private static class Tuple
	{
		int x, y;
		
		public Tuple(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	private static class Rect
	{
		int x1, y1, x2, y2;
		
		public Rect(int x1, int y1, int x2, int y2)
		{
			this.x1 = Math.min(x1, x2);
			this.y1 = Math.min(y1, y2);
			this.x2 = Math.max(x1, x2);
			this.y2 = Math.max(y1, y2);
		}
	}
}
