import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Main{
	static class System{
		private static final InputStream in=java.lang.System.in;
		private static final PrintWriter out=new PrintWriter(java.lang.System.out,false);
	}
	public static void main(String[]$){
		new Main().solve();
		System.out.flush();
	}
	static Scanner s=new Scanner(System.in);
	static IntStream REPS(int v){return IntStream.range(0,v);}
	static IntStream REPS(int l,int r){return IntStream.rangeClosed(l,r);}
	static int gInt(){return s.nextInt();}
	static long gLong(){return s.nextLong();}
	static long gDouble(){return s.nextLong();}

	static IntStream ints(int n){return REPS(n).map(i->gInt());}
	static Stream<String> strs(int n){return REPS(n).mapToObj(i->s.next());}

	private void solve(){
		int n=gInt(),x=gInt(),y=gInt(),a[]=ints(n).distinct().toArray();

		n=a.length;

		if(n==1)
			System.out.println(Math.abs(a[0]-y));
		else
			System.out.println(Math.max(Math.abs(a[n-1]-y),Math.abs(a[n-2]-a[n-1])));
	}
}
