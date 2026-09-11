import java.util.*;
public class Main {
	
	public static class Ret{
		boolean ret;
		ArrayList<Integer> rev;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		ArrayList<Integer> b = new ArrayList<Integer>();
		for(int i=0;i<N;i++) {
			int nb = sc.nextInt();
			if(nb > i+1) {
				System.out.println(-1);
				return;
			}
			b.add(nb);
		}
		if(b.get(0) != 1) {
			System.out.println(-1);
			return;
		}
		ArrayList<Integer> rev = new ArrayList<Integer>();
		Ret ret = func(b,rev);
		if(ret.ret) {
			LinkedList<Integer> lis = new LinkedList<Integer>(ret.rev);
			for(int i=0;i<N;i++) {
				System.out.println(lis.remove());
			}
			return;
		}
		System.out.println(-1);
	}
	
	public static Ret func(ArrayList<Integer> b, ArrayList<Integer> rev) {
		Ret ret = new Ret();
		if(b.size() == 1 && b.get(0)==1) {
			rev.add(1);
			ret.ret = true;
			ret.rev = rev;
			return ret;
		}
		for(int i=b.size()-1;i>=0;i--) {
			//System.out.println(4);
			if(b.get(i)==i+1) {
				ArrayList<Integer> newb = new ArrayList<Integer>(b);
				newb.remove(i);
				Ret prev = func(newb,rev);
				if(prev.ret) {
					prev.rev.add(i+1);
					return prev;
				}
			}
		}
		ret.ret = false;
		return ret;
	}

}
