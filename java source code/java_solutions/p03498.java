import java.util.*;
import java.io.PrintWriter;
public class Main{
	public static void main(String []args){
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter(System.out);
		Task solver = new Task();
		solver.solve(1,in,out);
		out.close();
	}
	static class Task{
		public void solve(int testNumber,Scanner in,PrintWriter out){
			int n = in.nextInt();
			int Max = (int)(-1e6 + 10);
			int Min = (int)(1e6 + 10);
			int posMax = 0;
			int posMin = 0;
			for(int i = 0 ; i < n ; i++){
				int x = in.nextInt();
				if(x > Max) {
					Max = x;
					posMax = i + 1;
				}
				if(x < Min){
					Min = x;
					posMin = i + 1;
				}
			}
			if(Math.abs(Max) >= Math.abs(Min)){
				out.println(2 * n - 2);
				for(int i = 1 ; i <= 2 ; i++){
					out.println(posMax + " " + 2);
				}
				for(int i = 2 ; i <= n - 1; i++){
					for(int j = 1 ; j <= 2 ; j++){
						out.println(i + " " + (i + 1));
					}
				}
			} else {
				out.println(2 * n - 2);
				for(int i = 1 ; i <= 2 ; i++){
					out.println(posMin + " " + (n - 1));
				}
				for(int i = n - 2 ; i >= 1 ; i--){
					for(int j = 1 ; j <= 2 ; j++){
						out.println((i + 1) + " " + i);
					}
				}
			}
		}
	}
}
