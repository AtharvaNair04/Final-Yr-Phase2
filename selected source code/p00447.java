//Volume5-0524
import java.util.ArrayList;
import java.util.Scanner;

class Star {
	public int x,y;
	Star (int x,int y){
		this.x=x; this.y=y;
	}
	public boolean equals(Object o){
		Star s = (Star)o;
		return this.x==s.x&&this.y==s.y;
	}
}

class Main {

	private static ArrayList<Star> target = new ArrayList<Star>(),
			                       map    = new ArrayList<Star>();

	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);

		while(true){
			target.clear();
			map   .clear();
			int m = sc.nextInt();
			if(m==0){break;}
			for(int i=0;i<m;i++){target.add(new Star(sc.nextInt(),sc.nextInt()));}
			int n = sc.nextInt();
			for(int i=0;i<n;i++){map.   add(new Star(sc.nextInt(),sc.nextInt()));}
			Star    match_1 = target.get(0),
					match_2;
			int     diff_x,
			        diff_y;
			boolean flg;
			String  result = "";
			for(int i=0;i<map.size();i++){
				match_2 = map.get(i);
				diff_x  = match_2.x - match_1.x;
				diff_y  = match_2.y - match_1.y;
				flg     = true;
				for(int j=1;j<target.size();j++){
					Star t = target.get(j);
					if (!(map.contains(new Star(t.x + diff_x,t.y + diff_y)))){
						flg = false;
						break;
					}
				}
				if(flg){result = diff_x + " " + diff_y; break;}
			}
			System.out.println(result);
		}
	}
}
