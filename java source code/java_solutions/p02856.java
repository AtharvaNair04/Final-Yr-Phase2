import java.util.*;

class Main{
	public static void main(String[] $){
		System.err.println();
		Scanner s=new Scanner(System.in);
		int n=s.nextInt();
		int v=0;
		long r=0;
		for(int i=0;i<n;++i){
			int d=s.nextInt();
			long c=s.nextLong();
			while(true){
				//System.err.println(r+" "+v+" "+d+" "+c);
				if(d==0){
					r+=c;
					break;
				}
				if(c%2==1){
					--c;
					++r;
					if((v+=d)>9){
						v=f(v);
						++r;
					}
					if(c==0)
						break;
				}
				c/=2;
				r+=d<=4?c:c*2;
				d=f(d*2);
			}
		}
		System.out.println(r-1);
	}

	private static int f(int v){
		return v%10+v/10;
	}
}
