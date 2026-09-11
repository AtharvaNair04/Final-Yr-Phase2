import java.util.*;

class Main{
	static Scanner s=new Scanner(System.in);
	static int gInt(){
		return Integer.parseInt(s.next());
	}

	public static void main(String[] $){
		int n=gInt(),k=gInt();
		long[]a=new long[n+1];
		for(int i=1;i<=n;++i)
			a[i]=gInt()+a[i-1];
		
		ArrayList<Long>l=new ArrayList<>();
		for(int i=0;i<n;++i)
			for(int j=i+1;j<=n;++j)
				l.add(a[j]-a[i]);

		long r=0;
		for(int i=62;i>=0;--i) {
			long b=1L<<i;
			int c=0;
			for(long v:l) {
				if((v&b)>0)
					++c;
				if(c==k) {
					r|=b;
					l.removeIf(o->(o&b)==0);
					break;
				}
			}
		}
		System.out.println(r);
	}
}
