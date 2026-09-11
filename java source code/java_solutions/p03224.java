import java.util.*;

class Main{
	public static void main(String[] $){
		Scanner s=new Scanner(System.in);
		int n=s.nextInt();
		
		if(n==1) {
			System.out.println("Yes\n2\n1 1\n1 1");
			return;
		}
		
		int m=0;
		{
			for(long i=0;i<=n;++i){
				if(i*(i-1)==n*2){
					m=(int)i;
				}
			}
			if(m==0){
				System.out.println("No");
				return;
			}
		}
		System.out.println("Yes\n"+m);

		StringJoiner[] sj=new StringJoiner[m];
		Arrays.setAll(sj,i->new StringJoiner(" "));
		{
			String t=String.valueOf(n*2/m);;
			Arrays.stream(sj).forEach(o->o.add(t));
		}

		int p=1;
		for(int i=0;i<m-1;++i){
			for(int j=i+1;j<m;++j){
				String P=String.valueOf(p);
				sj[i].add(P);
				sj[j].add(P);
				++p;
			}
		}
		Arrays.stream(sj).forEach(System.out::println);
	}
}
