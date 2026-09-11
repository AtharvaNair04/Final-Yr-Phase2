import java.util.*;
public class Main{
	static Scanner s = new Scanner(System.in);
	public static void main(String[] args) {
		int n=s.nextInt(),c=s.nextInt(),k=s.nextInt();

		PriorityQueue<Integer> t = new PriorityQueue<>();

		for(int i=0;i<n;i++) t.add(s.nextInt());

		int result=0,bus,buf;
		while(!t.isEmpty()) {
			result++;
			bus=0;
			buf=t.peek()+k;
			while(!t.isEmpty()&&t.peek()<=buf) {
				t.poll();
				bus++;
				if(bus==c)
					break;
			}
		}
		System.out.println(result);
	}
}
