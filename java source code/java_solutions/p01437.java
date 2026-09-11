import java.math.BigInteger;
import java.util.Scanner;

public class Main {

	static BigInteger one=BigInteger.ONE,two=new BigInteger("2"),zero=BigInteger.ZERO;
	static String map[];
	static BigInteger vis[][][];
	static int h=1,w=1;
	static boolean modswitch;
	private static Node dfs(Node n){
		//System.out.println(n.x+" "+n.y+" "+n.dir+" left=="+n.stepleft);
		int d=0;
		switch(n.dir){
		case'N':d=0;break;
		case'S':d=1;break;
		case'E':d=2;break;
		case'W':d=3;
		}
		if(n.stepleft.equals(zero))return n;
		if(!vis[n.x][n.y][d].equals(zero)&&modswitch){
			//System.out.println("mod=="+vis[n.x][n.y][d].subtract(n.stepleft));
			n.stepleft=n.stepleft.mod(vis[n.x][n.y][d].subtract(n.stepleft));
			modswitch=false;
		}
		else vis[n.x][n.y][d]=n.stepleft;
		if(n.stepleft.equals(zero))return n;
		while(!n.goable()){
			//System.out.println("turning");
			n.turn();
		}
		switch(n.dir){
		case'N':return dfs(new Node(n.x-1,n.y,n.dir,n.stepleft.subtract(one)));
		case'E':return dfs(new Node(n.x,n.y+1,n.dir,n.stepleft.subtract(one)));
		case'S':return dfs(new Node(n.x+1,n.y,n.dir,n.stepleft.subtract(one)));
		default:return dfs(new Node(n.x,n.y-1,n.dir,n.stepleft.subtract(one)));
		}
	}
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		BigInteger l=one;
		while(h!=0||w!=0||!l.equals(zero)){
			modswitch=true;
			h=in.nextInt();
			w=in.nextInt();
			l=new BigInteger(in.next());
			if(h==0&&w==0&&l.equals(zero))break;
			vis=new BigInteger[h][w][4];
			map=new String[h];
			Node start = null;
			//System.out.println(h+" "+w+" "+l);
			in.nextLine();
			for(int i=0;i<h;i++){
				map[i]=in.nextLine();
				//System.out.println(map[i]);
				for(int j=0;j<w;j++){
					char c=map[i].charAt(j);
					for(int k=0;k<4;k++){
						vis[i][j][k]=zero;
					}
					if(c!='.'&&c!='#'){
						start=new Node(i,j,c,l);
						map[i].replace(map[i].charAt(j),'.');
						//System.out.println(i+" "+j+" "+c+" "+l);
					}
				}
			}
			start=dfs(start);
			System.out.println((start.x+1)+" "+(start.y+1)+" "+start.dir);
		}
	}
static class Node{
	int x,y;
	char dir;
	BigInteger stepleft;
	public boolean goable(){
		switch(this.dir){
		case'N':return x>0&&map[x-1].charAt(y)!='#';
		case'S':return x+1<h&&map[x+1].charAt(y)!='#';
		case'E':return y+1<w&&map[x].charAt(y+1)!='#';
		case'W':return y>0&&map[x].charAt(y-1)!='#';
		}
		return false;
	}
	private void turn(){
		switch(this.dir){
		case'N':this.dir='E';break;
		case'S':this.dir='W';break;
		case'E':this.dir='S';break;
		case'W':this.dir='N';
		}
	}
	public Node(int x,int y,char dir,BigInteger stepleft){
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.stepleft = stepleft;
	}
}
}
