import java.util.*;
class Main{
    static int[] dx = {0,0,1,-1};
    static int[] dy = {-1,1,0,0};
    static int h;
    static int w;
    static int thismax;
    static int[][] visited;
    static char[][] s;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        h = scanner.nextInt();
        w = scanner.nextInt();
        thismax = 0;
        
        s = new char[h][w];
        for(int i = 0; i < h; i++){
            String str = scanner.next();
            for(int j = 0; j < w; j++){
                s[i][j] = str.charAt(j); 
            }
        }
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                visited = new int[h][w];
                if(s[i][j] == '.'){
                    s[i][j] = '#';
                    bfs(i,j,visited);
                    s[i][j] = '.';
                }
            }
        }
        System.out.println(thismax);
    }
    
    public static boolean bfs (int x, int y, int[][] visited) {
        Queue <Point> queue = new LinkedList<Point>();
        queue.add(new Point(x,y));
        while (!queue.isEmpty()) {
            Point cur = queue.poll();
            if(!(cur.x >= 0 && cur.y >= 0 && cur.x < h && cur.y < w)){continue;}
            for (int i=0; i<4; i++) {
                Point now = new Point(cur.x+dx[i], cur.y+dy[i]);
                if(now.x >= 0 && now.y >= 0 && now.x < h && now.y < w){
                    if(s[now.x][now.y] == '.' && visited[now.x][now.y] == 0){
                        visited[now.x][now.y] = visited[cur.x][cur.y] + 1;
                        thismax = Math.max(thismax, visited[now.x][now.y]);
                        queue.add(now);
                    }
                }
            }
        }
        return false;
    }

    static class Point implements Comparable<Point>{
		int x,y;
		public Point(int x,int y){
			this.x=x;
			this.y=y;
		}
		@Override
		public int compareTo(Point j){
			return this.y-j.y;
		}
	}


}
