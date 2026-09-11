import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main{
	public static char[] s;
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);
		while(true){
			s = in.next().toCharArray();
			int idx = in.nextInt();
			if(s[0] == '0') break;
			Node base = new Node(1);
			cur = 0;
			while(cur < s.length){
				base.edge.add(gen());
			}
//			for(int i=0; i<100; i++){
//				int val = (int)base.dfs(i);
//				if(val >= 0) break;
//				System.out.print(res);
//			}
//			System.out.println();
			System.out.println(base.dfs(idx) < 0 ? res : '0');
		}
	}
	
	public static char res;
	public static int cur;
	public static Node gen(){
		if(Character.isDigit(s[cur])){
			int num = 0;
			while(Character.isDigit(s[cur])){
				num *= 10;
				num += s[cur++]-'0';
			}
			if(Character.isUpperCase(s[cur])){
				return new Node(s[cur++]+"", num);
			}
			cur++;
			Node node = new Node(num);
			while(cur < s.length && s[cur] != ')')
				node.edge.add(gen());
			cur++;
			return node;
		}else{
			return new Node(getStr(), 1);
		}
	}
	
	public static String getStr(){
		StringBuilder sb = new StringBuilder();
		while(cur < s.length && Character.isUpperCase(s[cur]))
			sb.append(s[cur++]);
		return sb.toString();
	}
}

class Node{
	ArrayList<Node> edge;
	String str;
	int num;
	public Node(int num){
		edge = new ArrayList<Node>();
		this.num = num;
	}
	
	public Node(String str, int num){
		this.str = str;
		this.num = num;
	}
	
	public String toString(){
		if(str != null){
			return (num>1?num:"")+str;
		}
		StringBuilder sb = new StringBuilder();
		if(num>1) sb.append(num+"<");
		for(Node v: edge) sb.append(v);
		if(num>1) sb.append('>');
		return sb.toString();
	}
	
	public long dfs(long sidx){
		// sidx : ??????????????¢
		if(str != null){
			if(sidx <= str.length()*num-1){
				sidx = sidx % str.length();
				Main.res = str.charAt((int)sidx);
				return -1;
			}
			return sidx-str.length()*num;
		}
		long origin = sidx;
		for(Node v: edge){
			sidx = v.dfs(sidx);
			if(sidx<0) return sidx;
		}
		long len = origin - sidx;
		if((origin+len)/len <= num){
			return dfs(origin % len);
		}
		return origin - num*len;
	}
}
