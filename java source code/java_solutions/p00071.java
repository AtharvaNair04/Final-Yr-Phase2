//Volume0-0071
import java.util.Scanner;

class Cell{
	public boolean bomb;
	public boolean xpl;
}

public class Main {

	private static final int      L     = 8;
	private static       int      i,j,x,y;
    private static       Cell[][] board = new Cell[L][L];

	public static void main(String[] args) {

		//declare
		String   s;
		int      n,No;

		//initialize
		init();

        //input
        Scanner sc = new Scanner(System.in);
        No = n = Integer.parseInt(sc.nextLine());
        while(n-- > 0){
        	sc.nextLine();
        	for(i=0;i<L;i++){
        		s = sc.nextLine();
        		for(j=0;j<L;j++){
        			if(s.charAt(j) == '1'){
        				board[i][j].bomb = true;
        			} else {
        				board[i][j].bomb = false;
        			}
        			board[i][j].xpl = false;
        		}
        	}

        	x = Integer.parseInt(sc.nextLine()) - 1;
        	y = Integer.parseInt(sc.nextLine()) - 1;

        	//calculate
        	do {
        		xpl();
        		check();
        	} while (x != -1);

        	//output
        	System.out.println("Data "+(No-n)+":");
        	for(i=0;i<L;i++){
        		for(j=0;j<L;j++){
        			if(board[i][j].bomb){
        				System.out.print(1);
        			} else {
        				System.out.print(0);
        			}
        		}
        		System.out.println();
        	}
        }
	}

	private static void init(){
		for(i=0;i<L;i++){
			for(j=0;j<L;j++){
				board[i][j] = new Cell();
			}
		}
	}

	private static void xpl(){

		board[y][x].bomb = false;

		for(i=x-3;i<=x+3;i++){
			if(0 <= i && i < L && i != x){
				board[y][i].xpl = true;
			}
		}
		for(i=y-3;i<=y+3;i++){
			if(0 <= i && i < L && i != y){
				board[i][x].xpl = true;
			}
		}
	}

	private static void check(){
		x = -1;
		out:for(i=0;i<L;i++){
			for(j=0;j<L;j++){
				if(board[i][j].bomb && board[i][j].xpl){
					x = j; y = i;
					break out;
				}
			}
		}
	}
}
