import java.util.*;

public class Main{
	static class Move{
		int fy,fx,ty,tx;
		public Move(int fy,int fx,int ty,int tx){
			this.fy=fy+1;
			this.fx=fx+1;
			this.ty=ty+1;
			this.tx=tx+1;
		}
		@Override
		public String toString(){
			return fy+" "+fx+" "+ty+" "+tx;
		}
	}

	static Scanner s=new Scanner(System.in);
	static int h=s.nextInt(),w=s.nextInt();
	static int[][] f=new int[h][w];
	
	static ArrayList<Move>r=new ArrayList<>();
	
	public static void main(String[] $){
		for(int i=0;i<h;++i)
			Arrays.setAll(f[i],o->s.nextInt());
		
		for(int i=0;i<h;++i) {
			for(int j=0;j<w-1;++j) {
				if(f[i][j]%2==1)
					f(i,j,i,j+1);
			}
		}
		for(int i=0;i<h-1;++i) {
			if(f[i][w-1]%2==1)
				f(i,w-1,i+1,w-1);
		}
		
		System.out.println(r.size());
		r.forEach(System.out::println);
	}
	static void f(int i,int j,int I,int J){
		r.add(new Move(i,j,I,J));
		++f[I][J];
	}
}
