import java.util.*;

class Main {

	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		int w=0, k=0;
		int ws[] = new int[10], ks[] = new int[10];
		for(int i=0; i<10; i++)
                   ws[i] = in.nextInt();
		for(int i=0; i<10; i++)
                   ks[i] = in.nextInt();
		Arrays.sort(ks);
		Arrays.sort(ws);
		for(int i=0; i<3; i++)
                  w += ws[9-i];
		for(int i=0; i<3; i++)
                  k += ks[9-i];
		System.out.println(w + " " + k);
	}
	
}
