import java.util.Scanner;

//The Tower
public class Main{

	int h, w, T, pi, pj, gi, gj, t;
	char[][] m;
	String[] str = {
			"Game Over : Cleared",
			"Game Over : Death by Hole",
			"Game Over : Death by Block",
			"Game Over : Death by Walking Goal",
			"Game Over : Gave Up"
	};
	String[][] cmd;
	
	int check(boolean soon){
		if(pi+1==gi&&pj==gj)return 0;
		if(m[pi+1][pj]=='B')return 1;
		if(m[pi][pj]!='.')return 2;
		if(m[gi][gj]!='G')return 3;
		if(soon)return 5;
		return t==T?4:5;
	}
	
	void push(int i, int j, int dir){
		if(m[i][j+dir]!='.')return;
		if(j+dir==0 || j+dir==w+1){
			m[i][j] = '.'; return;
		}
		m[i][j+dir] = m[i][j];
		m[i][j] = '.';
		if(m[i+1][j+dir]=='.')return;
		if(m[i][j+dir]=='I' || m[i+1][j+dir]=='I')push(i, j+dir, dir);
		return;
	}
	
	void command(){
		if("MOVETO".equals(cmd[t][0])){
			int s = Integer.parseInt(cmd[t][1]);
			if(s < 1 || w < s || pj==s)return;
			if(m[pi][s]!='.')return;
			boolean ok = true;
			for(int j=Math.min(pj, s);j<=Math.max(pj, s);j++)ok&=m[pi+1][j]!='.';
			if(!ok)return;
			if(Character.isDigit(m[pi+1][pj]))m[pi+1][pj]--;
			pj = s;
		}
		else if("GETDOWN".equals(cmd[t][0])){
			if(pi==h)return;
			if("LEFT".equals(cmd[t][1])){
				if(pj==1 || m[pi][pj-1]!='.' || m[pi+1][pj-1]!='.')return;
				if(Character.isDigit(m[pi+1][pj]))m[pi+1][pj]--;
				pi++; pj--;
			}
			else{
				if(pj==w || m[pi][pj+1]!='.' || m[pi+1][pj+1]!='.')return;
				if(Character.isDigit(m[pi+1][pj]))m[pi+1][pj]--;
				pi++; pj++;
			}
		}
		else if("CLIMB".equals(cmd[t][0])){
			if(pi==1)return;
			if("LEFT".equals(cmd[t][1])){
				if(m[pi][pj-1]=='.' || m[pi-1][pj-1]!='.' || m[pi-1][pj]!='.')return;
				if(Character.isDigit(m[pi+1][pj]))m[pi+1][pj]--;
				pi--; pj--;
			}
			else{
				if(m[pi][pj+1]=='.' || m[pi-1][pj+1]!='.' || m[pi-1][pj]!='.')return;
				if(Character.isDigit(m[pi+1][pj]))m[pi+1][pj]--;
				pi--; pj++;
			}
		}
		else if("PUSH".equals(cmd[t][0])){
			if("LEFT".equals(cmd[t][1])){
				if(m[pi][pj-1]=='.')return;
				boolean existC = false;
				int tail = pj;
				while(0 < tail && m[pi][tail-1]!='.'){
					if(m[pi][tail-1]=='C')existC = true;
					tail--;
				}
				if(tail==pj || existC)return;
				for(int j=tail;j<pj;j++)push(pi, j, -1);
			}
			else{
				if(m[pi][pj+1]=='.')return;
				boolean existC = false;
				int tail = pj;
				while(tail <= w && m[pi][tail+1]!='.'){
					if(m[pi][tail+1]=='C')existC = true;
					tail++;
				}
				if(tail==pj || existC)return;
				for(int j=tail;j>pj;j--)push(pi, j, 1);
			}
		}
		//PULL
		else {
			if("LEFT".equals(cmd[t][1])){
				if(pj==1 || m[pi][pj+1]=='.' || m[pi][pj+1]=='C' || m[pi][pj-1]!='.')return;
				m[pi][pj] = m[pi][pj+1];
				m[pi][pj+1] = '.';
				if(Character.isDigit(m[pi+1][pj]))m[pi+1][pj]--;
				pj--;
			}
			else{
				if(pj==w || m[pi][pj-1]=='.' || m[pi][pj-1]=='C' || m[pi][pj+1]!='.')return;
				m[pi][pj] = m[pi][pj-1];
				m[pi][pj-1] = '.';
				if(Character.isDigit(m[pi+1][pj]))m[pi+1][pj]--;
				pj++;
			}
		}
	}
	
	void autofunc(){
		boolean con = true;
		for(int i=1;i<=h;i++)for(int j=1;j<=w;j++)if(m[i][j]=='0')m[i][j]='.';
		while(con){
			con = false;
			for(int i=2;i<=h;i++)for(int j=1;j<=w;j++)if(m[i][j]=='B'&&m[i-1][j]!='.'){
				con = true;
				m[i-1][j] = '.';
			}
			while(m[pi+1][pj]=='.'){
				con = true;
				pi++;
			}
			for(int i=h-1;i>=1;i--)for(int j=1;j<=w;j++)if(m[i][j]!='.'){
				int ni = i;
				while(m[ni+1][j-1]=='.'&&m[ni+1][j]=='.'&&m[ni+1][j+1]=='.'){
					con = true;
					m[ni+1][j] = m[ni][j];
					m[ni][j] = '.';
					ni++;
				}
			}
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			h = sc.nextInt(); w = sc.nextInt();
			if((h|w)==0)break;
			m = new char[h+2][w+2];
			for(int j=0;j<w+2;j++)m[0][j]=m[h+1][j]='C';
			for(int i=1;i<=h;i++)m[i][0]=m[i][w+1]='.';
			for(int i=1;i<=h;i++){
				char[] c = sc.next().toCharArray();
				for(int j=1;j<=w;j++){
					m[i][j]=c[j-1];
					if(c[j-1]=='S'){
						m[i][j]='.'; pi = i; pj = j;
					}
					if(c[j-1]=='G'){
						gi = i; gj = j;
					}
				}
			}
			T = sc.nextInt();
			cmd = new String[T][2];
			for(int i=0;i<T;i++)for(int j=0;j<2;j++)cmd[i][j]=sc.next();
			int res = 5;
			for(t=0;;t++){
				res = check(false);
				if(res!=5)break;
				command();
				res = check(true);
				if(res!=5)break;
				autofunc();
			}
			System.out.println(str[res]);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
