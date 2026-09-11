import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {

	static int N,p1,p2;
	static boolean[] isDevine;
	static int[][] dp,nums;
	static int num;
	public static void main(String[] args) {
		Scanner cin=new Scanner(System.in);
		for(;;){
			N=cin.nextInt();
			p1=cin.nextInt();
			p2=cin.nextInt();
			if((N|p1|p2)==0)break;
			num=p1+p2+1;
			isDevine=new boolean[num];
			Node[] node=new Node[num];
			
			for(int i=0;i<node.length;i++){
				node[i]=new Node(i,-1);
			}
			for(int i=1;i<=N;i++){
				int a=cin.nextInt();
				int b=cin.nextInt();
				String s=cin.next();
				Edge edge=new Edge(node[a],node[b]);
				edge.yes=s.equals("yes");
				node[a].list.add(edge);
				node[b].list.add(edge);
			}
			List<int[]>list=new LinkedList<int[]>();
			List<Group>groups=new LinkedList<Group>();
			for(int i=1;i<node.length;i++){
				if(node[i].isDevine!=-1)continue;
				node[i].isDevine=0;
				g=new Group();
				
				int[] a=bfs(node[i]);
				list.add(a);
				
				groups.add(g);
//				System.out.println(a[0]+" "+a[1]);
			}
			ans=false;
			ichii=true;
			nums=list.toArray(new int[][]{});
			stack=new Stack<Integer>();
			for(int i=0;i<nums.length;i++){
//				System.out.println(nums[i][0]+" "+nums[i][1]);
			}
			dp=new int[nums.length][num];
			for(int i=0;i<dp.length;i++){
				Arrays.fill(dp[i], 0);
			}
			ansArray=new Integer[]{};
			dp(0,p1+p2);
			List<Integer> godList=new LinkedList<Integer>();
			if(ans&&ichii){
				int cnt=-1;
				for(Integer a:ansArray){
					cnt++;
					// gods
					if(a==1){
						for(Integer gods:groups.get(cnt).gods){
							godList.add(gods);
						}
					}
					
					// devils
					else{
						for(Integer gods:groups.get(cnt).devils){
							godList.add(gods);
						}
					}
					
				}
				Collections.sort(godList);
				for(Integer a:godList){
					System.out.println(a);
				}
				System.out.println("end");
			}
			else{
				System.out.println("no");
			}
		}
	}
	static boolean ans,ichii;
	static Integer[] ansArray;
	static Stack<Integer>stack;
	static Group g;
	static int dp(int a,int b){
		if(!ichii)return -1;
		if(b<0)return -1;
		if(a==nums.length){
			if(b!=num-p2-1)return -1;
			ansArray=stack.toArray(ansArray);
			ans=true;
			return 1;
		}
		
		if(dp[a][b]!=0)return dp[a][b];
		
		stack.add(0);
		int x=dp(a+1,b-nums[a][0]);
		stack.pop();
		
		stack.add(1);
		int y=dp(a+1,b-nums[a][1]);
		stack.pop();
		
		if(x+y==2){
			ichii=false;
			return -1;
		}
		
		int re=Math.max(x,y);
		return dp[a][b]=re;
	}
	static int[] bfs(Node node){
		if(node.already)return null;
		node.already=true;
		if(node.isDevine==0){
			g.gods.add(node.num);
		}
		else{
			g.devils.add(node.num);
		}
		int[] re={0,0};
		re[node.isDevine]++;
		for(Edge edge:node.list){
			int neibor=0;
			if(edge.yes){
				neibor=node.isDevine;
			}
			else{
				neibor=1-node.isDevine;
			}
			int[] a=null;
			if(edge.node1==node){
				edge.node2.isDevine=neibor;
				a=bfs(edge.node2);
			}
			else{
				edge.node1.isDevine=neibor;
				a=bfs(edge.node1);
			}
			if(a!=null){
				re[0]+=a[0];
				re[1]+=a[1];				
			}
		}
		return re;
	}
	
	// グループの代表者をgodとしたときの、それぞれの値
	// グループの代表者は、グループの中で一番最小のインデックス
	static class Group{
		List<Integer> gods;
		List<Integer> devils;
		Group(){
			gods=new LinkedList<Integer>();
			devils=new LinkedList<Integer>();
		}
		int godnum,devilnum;
	}
	static class Node{
		int num;
		int isDevine;
		List<Edge>list;
		boolean already;
		Node(int a,int b){
			num=a;
			isDevine=-1;
			list=new LinkedList<Edge>();
			already=false;
		}
	}
	static class Edge{
		Node node1,node2;
		boolean yes;
		Edge(Node a,Node b){
			node1=a;node2=b;
		}
	}
}
