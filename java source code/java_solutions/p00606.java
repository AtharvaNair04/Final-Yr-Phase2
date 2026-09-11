import java.util.*;

class Main{

    static int n;
    static int [][] t = new int[3][3];
    static int dy[] = {-1,0,1,0};
    static int dx[] = {0,1,0,-1};
    static int sx,sy,gx,gy,kx,ky;

    static int change(String ch){
	if(ch.equals("A"))return 0;
	if(ch.equals("B"))return 1;
	if(ch.equals("C"))return 2;
	if(ch.equals("D"))return 3;
	if(ch.equals("E"))return 4;
	if(ch.equals("F"))return 5;
	if(ch.equals("G"))return 6;
	if(ch.equals("H"))return 7;
	if(ch.equals("I"))return 8;
	else return -1;
    }

    static boolean inField(int y,int x){
	if(y<0||x<0)return false;
	if(y>2||x>2)return false;
	return true;
    }
    
    static double [][][] mem = new double[3][3][16];

    static double solve(int y,int x,int cnt){
	if(mem[y][x][cnt]!=200.0)return mem[y][x][cnt];
	if(cnt==n){
	    if(t[y][x]==1)return 1.0;
	    else return 0.0;
	}
	
	int ny,nx;
	double sum=0.0;
	for(int i=0;i<4;i++){
	    ny=y+dy[i];
	    nx=x+dx[i];
	    if(inField(ny,nx)==false||t[ny][nx]==-1){
		sum+=solve(y,x,cnt+1);
	    }else{
		sum+=solve(ny,nx,cnt+1);
	    }
	}
	sum=sum/4.0;
	mem[y][x][cnt]=sum;
	return sum;
    }

    public static void main(String[] args){
	Scanner scan = new Scanner(System.in);
	String ch;
	while(true){
	    n=scan.nextInt();

	    if(n==0)break;
	    for(int i=0;i<3;i++){
		for(int j=0;j<3;j++){
		    for(int k=0;k<16;k++)mem[i][j][k]=200.0;
		    t[i][j]=0;
		}
	    }

	    ch=scan.next();
	    sy=change(ch)/3;
	    sx=change(ch)%3;

	    ch=scan.next();
	    gy=change(ch)/3;
	    gx=change(ch)%3;

	    ch=scan.next();
	    ky=change(ch)/3;
	    kx=change(ch)%3;

	    /*
	    System.out.println(sy+" "+sx);
	    System.out.println(gy+" "+gx);
	    System.out.println(ky+" "+kx);
	    */
	    t[gy][gx]=1;
	    t[ky][kx]=-1;
	    System.out.println(solve(sy,sx,0));

	}
    }
}
