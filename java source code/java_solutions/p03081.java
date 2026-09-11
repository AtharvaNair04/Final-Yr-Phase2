import java.util.*;

public class Main{
	public static void main(String[] args){
		Scanner s=new Scanner(System.in);
		int n=s.nextInt(),Q=s.nextInt();
		char[] c=s.next().toCharArray();

		char[] q=new char[Q];
		boolean[] f=new boolean[Q];
		for(int i=0;i<Q;++i){
			q[i]=s.next().charAt(0);
			f[i]=s.next().charAt(0)=='L';
		}

		int l=0,r=n;
		for(int i=Q-1;i>=0;--i){
			if(f[i]){
				if(l<n&&c[l]==q[i]) ++l;
				if(r<n&&c[r]==q[i]) ++r;
			}else{
				if(l>0&&c[l-1]==q[i]) --l;
				if(r>0&&c[r-1]==q[i]) --r;
			}
		}
		System.out.println(r-l);
	}
}
