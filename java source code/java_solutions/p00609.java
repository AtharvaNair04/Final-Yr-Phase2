import java.util.Arrays;
import java.util.Scanner;
 
public class Main {
	
	public static class Point implements Comparable<Point>{
		int x, y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Point o) {
			if(this.x != o.x){
				return this.x - o.x;
			}else{
				return this.y - o.y;
			}
		}

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
		}
	}
	
	public static int check(final int R, Point[] enemys, Point[] bullets){
		final int LIM = 4 * 4 * R * R;
		
		//System.out.println(Arrays.toString(enemys));
		//System.out.println(Arrays.toString(bullets));
		
		int sum = 0;
		Point start = new Point(0, 0);
		for(Point enemy : enemys){
			start.x = enemy.x - 4 * R;
			
			int start_pos = Arrays.binarySearch(bullets, start);
			if(start_pos  < 0){
				start_pos = -(start_pos + 2);
			}
			start_pos = Math.max(0, start_pos);
			
			for(int i = start_pos; i < bullets.length; i++){
				if(bullets[i].x - enemy.x > 4 * R){
					break;
				}
				
				final int dist = (enemy.x - bullets[i].x) * (enemy.x - bullets[i].x) + (enemy.y - bullets[i].y) * (enemy.y - bullets[i].y);
				
				if(dist <= LIM){
					sum++;
				}
			}
		}
		
		return sum;
	}
	
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
         
        while(sc.hasNext()){
        	final int A = sc.nextInt();
        	final int B = sc.nextInt();
        	final int R = sc.nextInt();
        	
        	if(A == 0 && B == 0 && R == 0){
        		break;
        	}
        	
        	System.gc();
        	
        	Point[] enemys = new Point[A];
        	Point[] bullets = new Point[B];
        	
        	for(int i = 0; i < A; i++){
        		enemys[i] = new Point(sc.nextInt(), sc.nextInt());
        	}
        	
        	for(int i = 0; i < B; i++){
        		bullets[i] = new Point(sc.nextInt(), sc.nextInt());
        	}
        	
        	Arrays.sort(enemys);
        	Arrays.sort(bullets);
        	
        	System.out.println(check(R, enemys, bullets));
        }
    }
}
