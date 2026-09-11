import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		while(true){
			int n = in.nextInt();
			if(n==0) break;
			Lines[] line = new Lines[n+1];
			Queue<Integer> ans = new LinkedList<Integer>();
			for(int i=0; i<=n; i++){
				int m = in.nextInt();
				Pos[] p = new Pos[m];
				for(int j=0; j<m; j++){
					p[j] = new Pos(in.nextInt(), in.nextInt());
				}
				line[i] = new Lines(p);
				if(i==0) continue;
				if(line[0].equals(line[i])) ans.add(i);
			}
			for(int i: ans) System.out.println(i);
			System.out.println("+++++");
		}
	}
	
}

class Lines{
	String[] str;
	public Lines(Pos[] pos){
		str = new String[2];
		for(int i=0; i<2; i++){
			str[i] = ""+pos[0].dist(pos[1]);
			for(int j=1; j<pos.length-1; j++){
				str[i]+= (ccw(pos[j-1], pos[j], pos[j+1])==-1?'a':'b')+"";
				str[i]+= pos[j].dist(pos[j+1])+"";
			}
			if(i==1)break;
			Pos[] tmp = new Pos[pos.length];
			for(int j=0; j<pos.length; j++)
				tmp[j] = pos[pos.length-1-j];
			pos = tmp;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		Lines o = (Lines)obj;
		if(o.str[0].equals(str[0]) || o.str[0].equals(str[1]))
			return true;
		return false;
	}
	
	public static int ccw(Pos a, Pos b, Pos c){
		int dx1 = b.x-a.x;
		int dy1 = b.y-a.y;
		int dx2 = c.x-a.x;
		int dy2 = c.y-a.y;
		return Integer.signum(dx1*dy2-dx2*dy1);
	}
}

class Pos{
	int x, y;
	public Pos(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int dist(Pos p){
		return (x-p.x)*(x-p.x) + (y-p.y)*(y-p.y);
	}
}
