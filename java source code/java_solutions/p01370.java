import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);
    int[][] map;
    Queue<int[]> que;
    int x, y;
    int t, n;
    int count;
    int z = 100;

    void run(){
	while(sc.hasNext()){
	    map = new int[200][200];
	    que = new LinkedList<int[]>();
	    t = sc.nextInt();
	    n = sc.nextInt();
	    if(t!=0){
		int i=n;
		while(i>0){
		    i--;
		    int a = sc.nextInt();
		    int b = sc.nextInt();
		    map[a+z][b+z] = -1;
		}
		x = sc.nextInt();
		y = sc.nextInt();
		count = 0;
		solve(t, x, y);
		System.out.println(count);
	    }
	}
    }

    int[][] dir = { {0, 1}, {1, 1}, {1, 0}, 
		    {0, -1}, {-1, -1}, {-1, 0} };
		    
    void solve(int t, int x, int y){
	map[x+z][y+z] = 1;
	count++; // スタート地点
	for(int i=0; i<dir.length; i++){
	    int[] a = { x+dir[i][0], y+dir[i][1], 2};
	    que.add(a);
	}
	int now = 2;
	while(!que.isEmpty()){
	    int[] a = que.poll();
	    x = a[0];
	    y = a[1];
	    now = a[2];
	    if(now<=t+1){
		if(map[x+z][y+z]==0){
		    map[x+z][y+z] = now+1;
		    count++;
		    for(int i=0; i<dir.length; i++){
		        int[] b = { x+dir[i][0], y+dir[i][1], now+1};
			que.add(b);
		    }
		}
	    }
	}
    }   
}
