import java.util.Arrays;
import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int n;
		int id;
		int s;
		while(true){
			n = scan.nextInt();
			if(n == 0)break;
			
			T[] t = new T[n];
			
			for(int i = 0; i < n; i++){
				id = scan.nextInt();
				s = 0;
				for(int j = 0; j < 4; j++){
					s += scan.nextInt()*60+scan.nextInt();
				}
				t[i] = new T(id, s);
			}
			Arrays.sort(t);
			System.out.println(t[0].id + "\n" + t[1].id + "\n" + t[n-2].id);
		}
	}
	static class T implements Comparable<T>{
		int id, t;
		public T(int id, int t) {
			this.id = id;
			this.t = t;
		}
		public int compareTo(T o) {
			return t - o.t;
		}
	}
}
