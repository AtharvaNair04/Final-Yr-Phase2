import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		Queue<time> E=new ArrayDeque<>(),T=new ArrayDeque<>();

		int N=in.nextInt();
		for(int i=0;i<N;i++) {
			time t=new time();
			t.h=Integer.parseInt(in.next());
			t.m=Integer.parseInt(in.next());
			E.add(t);
		}

		int M=in.nextInt();
		for(int i=0;i<M;i++) {
			time t=new time();
			t.h=Integer.parseInt(in.next());
			t.m=Integer.parseInt(in.next());
			T.add(t);
		}

		Queue<time> MG=new ArrayDeque<>();
		while(!E.isEmpty() && !T.isEmpty()) {
			time e=E.peek(),t=T.peek();
			//System.out.println(e.h+":"+e.m+" "+t.h+":"+t.m);

			if((e.h*100)+e.m < (t.h*100)+t.m) {
				MG.add(e);
				E.remove();
			}
			else if((e.h*100)+e.m > (t.h*100)+t.m) {
				MG.add(t);
				T.remove();
			}
			else {
				MG.add(e);
				E.remove(); T.remove();
			}
		}

		while(!E.isEmpty()) {
			MG.add(E.remove());
		}

		while(!T.isEmpty()) {
			MG.add(T.remove());
		}

		int size=MG.size();
		for(int i=0;i<size-1;i++) {
			time t=MG.remove();
			System.out.printf("%d:%02d ",t.h,t.m);
		}
		System.out.printf("%d:%02d\n",MG.peek().h,MG.peek().m);
	}

}

class time{
	int h=0,m=0;
}
