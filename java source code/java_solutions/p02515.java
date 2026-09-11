import java.util.Scanner;

public class Main{

	char[] s;
	int id;
	boolean assign;
	
	char get(){
		return s[id++];
	}
	
	void exp(){
		boolean[][] r = clause();
		check(r);
		while(true){
			char ch = get();
			if(ch!='|')break;
			r = clause();
			check(r);
		}
	}
	
	void check(boolean[][] r){
		for(int i=0;i<26*2;i++){
			if(r[0][i]&&r[1][i])return;
		}
		assign = true;
	}
	
	//r[0][26*2] = assigned true
	//r[1][26*2] = assigned false;
	boolean[][] clause(){
		get();
		boolean[][] r = new boolean[2][26*2];
		for(int k=0;k<3;k++){
			int a = 0;
			int x = 0;
			char ch = get();
			if(ch=='~'){
				a=1;
				ch = get();
			}
			if(Character.isLowerCase(ch)){
				ch = Character.toUpperCase(ch);
				x = 26;
			}
			r[a][x+ch-'A'] = true;
			get();
		}
		return r;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		while(true){
			s = (sc.next()+"$").toCharArray();
			if(s[0]=='#')break;
			id = 0;
			assign = false;
			exp();
			System.out.println(assign?"yes":"no");
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
