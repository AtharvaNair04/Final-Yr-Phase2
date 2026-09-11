import java.io.*;
public class Main{
	static int N,Q;
	static Node[] ns;
	static boolean type[];
	static int no[];
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while(true){
			String input[] = br.readLine().split(" +");
			N = new Integer(input[0]);
			Q = new Integer(input[1]);
			if(N==0 & Q==0) break;

			ns = new Node[N+1];
			type = new boolean[Q];
			no = new int[Q];

			ns[1] = new Node(0);
			ns[1].marked = true;
			for(int i = 2; i <= N; i++){
				ns[i] = new Node(new Integer(br.readLine()));
			}
			for(int i = 0 ; i < Q; i++){
				input = br.readLine().split(" +");
				if(input[0].equals("Q")){
					type[i] = true;
				}else{
					type[i] = false;
				}
				no[i] = new Integer(input[1]);
			}
			
			long sum = 0;
			for(int i = 0 ; i < Q; i++){
				if(type[i]){
					sum += findmark(no[i]);
				}else{
					ns[no[i]].marked = true;
				}
			}
			System.out.println(sum);
		}
	}
	static int findmark(int no){
		while(true){
			if(ns[no].marked) return no;
			else no = ns[no].par;
		}
	}
}

class Node{
	int par;
	boolean marked = false;
	Node(int par){
		this.par = par;
	}
}
