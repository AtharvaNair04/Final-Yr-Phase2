import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int request=sc.nextInt();
		int[][] ary=new int[request][6];
		for(int i=0;i<request;i++) {
			for(int j=0;j<6;j++) {
				ary[i][j]=sc.nextInt();
			}
		}
		//System.out.println(Conbi.conbi(request));
		String YN="";
		for(int i=0;i<request-1;i++) {
			int a=ary[i][0];
			int b=ary[i][1];
			int c=ary[i][2];
			int d=ary[i][3];
			int e=ary[i][4];
			int f=ary[i][5];
			for(int j=i+1;j<request;j++) {
				DiceOri dice=new DiceOri(ary[j][0],ary[j][1],ary[j][2],ary[j][3],ary[j][4],ary[j][5]);
				if(c==dice.Right(a,b) && d==dice.Left(a,b) && e==dice.Back(a,b) && f==dice.Under(a,b)) {
					YN+="N";
					break;
				}else if(e==dice.Right(c,f) && b==dice.Left(c,f) && a==dice.Back(c,f) && d==dice.Under(c,f)){
					YN+="N";
					break;
				}else if(a==dice.Right(e,d) && f==dice.Left(e,d) && c==dice.Back(e,d) && b==dice.Under(e,d)) {
					YN+="N";
					break;
				}else {
					YN+="Y";
				}
			}
		}
		//System.out.println(YN);
		if(YN.indexOf("N")==-1) {
			System.out.println("Yes");
		}else {
			System.out.println("No");
		}
	}
}



class DiceOri{
	int Top;
    int Bottom;
    int Nth;
    int Sth;
    int Wst;
    int Est;
    DiceOri(int a, int b, int c, int d, int e, int f) {
        Top = a;
        Sth = b;
        Est = c;
        Wst = d;
        Nth = e;
        Bottom = f;
    }
	public void TurnN() {
		int empty=Top;
		Top=Sth;
		Sth=Bottom;
		Bottom=Nth;
		Nth=empty;
	}
	public void TurnW() {
		int empty=Top;
		Top=Est;
		Est=Bottom;
		Bottom=Wst;
		Wst=empty;
	}
	public void TurnS() {
		int empty=Top;
		Top=Nth;
		Nth=Bottom;
		Bottom=Sth;
		Sth=empty;
	}
	public void TurnE() {
		int empty=Top;
		Top=Wst;
		Wst=Bottom;
		Bottom=Est;
		Est=empty;
	}
	public int Right(int x,int y) {
		if((x==Top && y==Sth) || (x==Sth && y==Bottom) || (x==Bottom && y==Nth) || (x==Nth && y==Top)) {
			return Est;
		}else if((x==Wst && y==Sth) || (x==Sth && y==Est) || (x==Est && y==Nth) || (x==Nth && y==Wst)) {
			return Top;
		}else if((x==Est && y==Bottom) || (x==Bottom && y==Wst) || (x==Wst && y==Top) || (x==Top && y==Est)) {
			return Nth;
		}else if((x==Sth && y==Top) || (x==Top && y==Nth) || (x==Nth && y==Bottom) || (x==Bottom && y==Sth)) {
			return Wst;
		}else if((x==Est && y==Top) || (x==Top && y==Wst) || (x==Wst && y==Bottom) || (x==Bottom && y==Est)) {
			return Sth;
		}else {
			return Bottom;
		}
	}
	public int Left(int x,int y) {
		if((x==Top && y==Sth) || (x==Sth && y==Bottom) || (x==Bottom && y==Nth) || (x==Nth && y==Top)) {
			return Wst;
		}else if((x==Wst && y==Sth) || (x==Sth && y==Est) || (x==Est && y==Nth) || (x==Nth && y==Wst)) {
			return Bottom;
		}else if((x==Est && y==Bottom) || (x==Bottom && y==Wst) || (x==Wst && y==Top) || (x==Top && y==Est)) {
			return Sth;
		}else if((x==Sth && y==Top) || (x==Top && y==Nth) || (x==Nth && y==Bottom) || (x==Bottom && y==Sth)) {
			return Est;
		}else if((x==Est && y==Top) || (x==Top && y==Wst) || (x==Wst && y==Bottom) || (x==Bottom && y==Est)) {
			return Nth;
		}else {
			return Bottom;
		}
	}
	public int Back(int x,int y) {
		if((x==Top || x==Wst || x==Bottom || x==Est) && y==Sth) {
			return Nth;
		}else if((x==Sth || x==Wst || x==Nth || x==Est) && y==Bottom) {
			return Top;
		}else if((x==Sth || x==Top || x==Nth || x==Bottom) && y==Wst) {
			return Est;
		}else if((x==Sth || x==Bottom || x==Nth || x==Top) && y==Est) {
			return Wst;
		}else if((x==Est || x==Bottom || x==Wst || x==Top) && y==Nth) {
			return Sth;
		}else {
			return Bottom;
		}
	}
	public int Under(int x,int y) {
		if(x==Wst && (y==Top || y==Sth || y==Bottom || y==Nth)) {
			return Est;
		}else if(x==Bottom && (y==Wst || y==Sth || y==Est || y==Nth)) {
			return Top;
		}else if(x==Sth && (y==Wst || y==Top || y==Est || y==Bottom)) {
			return Nth;
		}else if(x==Est && (y==Bottom || y==Sth || y==Top || y==Nth)) {
			return Wst;
		}else if(x==Nth && (y==Top || y==Wst || y==Bottom || y==Est)) {
			return Sth;
		}else {
			return Bottom;
		}
	}
}
