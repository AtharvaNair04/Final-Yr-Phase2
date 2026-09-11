import java.util.*;

class Main{
    int INF=1<<28;
    int H,W;
    int[][] k;
    char[][] s;

    void run(){
	Scanner sc=new Scanner(System.in);
	int tt=sc.nextInt();
	while(tt-->0){
	    H=sc.nextInt();W=sc.nextInt();
	    k=new int[200][200];
	    s=new char[H+2][W+2];

	    for(int i=0;i<s.length;i++)
		for(int j=0;j<s[0].length;j++)
		    s[i][j]='.';
			
	    for(int i=1;i<H+1;i++){
		String ss=sc.next();
		for(int j=1;j<W+1;j++){
		    s[i][j]=ss.charAt(j-1);
		}
	    }

	    System.out.println(check()?"SAFE":"SUSPICIOUS");
	}
    }

    boolean check(){

	for(char c='A';c<='Z';c++){
	    int xmin=INF,ymin=INF;
	    int xmax=-1,ymax=-1;

	    for(int i=0;i<H+2;i++){
		for(int j=0;j<W+2;j++){
		    if(s[i][j]==c){
			xmin=Math.min(xmin,j);
			xmax=Math.max(xmax,j);
			ymin=Math.min(ymin,i);
			ymax=Math.max(ymax,i);
		    }
		}
	    }

	    for(int i=ymin;i<=ymax;i++){
		for(int j=xmin;j<=xmax;j++){
		    if(s[i][j]=='.')return false;
		    else if(s[i][j]!=c){
			char cc=s[i][j];
			if(k[c][cc]==1)return false;
			k[c][cc]=-1;k[cc][c]=1;
		    }
		}
	    }
	}

	    //テ、ツスツ催ァツスツョテゥツ鳴「テ、ツソツづ」ツ?古ァツ淞崚ァツ崢セテ」ツ?療」ツ?ヲテ」ツ??」ツ?ェテ」ツ??」ツ??
	    for(int z=0;z<10;z++){ //テゥツォツ佚」ツ??テァツィツョテゥツ。ツ?
		for(char i='A';i<='Z';i++){
		    for(char j='A';j<='Z';j++){
			if(k[i][j]==1 || k[j][i]==-1){
			    //テ」ツつづ」ツ?擁テ」ツ?桂テ」ツつ暗」ツつ甘、ツクツ甘」ツ?ォテ」ツ?づ」ツつ古」ツ?ー
			    for(char t='A';t<='Z';t++){
				if(k[t][i]==1 || k[i][t]==-1){
				if(k[t][j]==-1 || k[j][t]==1)return false;
				k[t][j]=1;k[j][t]=-1;
				}
			    }
			}
		    }
		}
	    }
	    return true;
    }

    public static void main(String[] args){
	new Main().run();
    }
}
