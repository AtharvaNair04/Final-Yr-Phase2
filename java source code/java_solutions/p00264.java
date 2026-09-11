import java.util.*;
public class Main {
	
	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		while(true) {
			int H = stdIn.nextInt();
			int R = stdIn.nextInt();
			
			if(H == 0 && R == 0) {
				break;
			}
			
			int[] hx = new int[H];
			int[] hy = new int[H];
			for(int i = 0; i < H; i++) {
				hx[i] = stdIn.nextInt();
				hy[i] = stdIn.nextInt();
			}
			
			int U = stdIn.nextInt();
			int M = stdIn.nextInt();
			int S = stdIn.nextInt();
			int du = stdIn.nextInt();
			int dm = stdIn.nextInt();
			int ds = stdIn.nextInt();
			
			int[] ux = new int[U];
			int[] uy = new int[U];
			for(int i = 0; i < U; i++) {
				ux[i] = stdIn.nextInt();
				uy[i] = stdIn.nextInt();
			}
			
			int[] mx = new int[M];
			int[] my = new int[M];
			for(int i = 0; i < M; i++) {
				mx[i] = stdIn.nextInt();
				my[i] = stdIn.nextInt();
			}
			
			int[] sx = new int[S];
			int[] sy = new int[S];
			
			for(int i = 0; i < S; i++) {
				sx[i] = stdIn.nextInt();
				sy[i] = stdIn.nextInt();
			}
			
			int[] w = new int[R];
			int[] a = new int[R];
			for(int i = 0; i < R; i++) {
				w[i] = stdIn.nextInt();
				a[i] = stdIn.nextInt();
			}
			
			//---処理ここから---//
			int[] day = new int[H];
			for(int i = 0; i < R; i++) { //風
				IN:for(int j = 0; j < H; j++) { //家
					//---私の桜が届くか---//
					if(!ReachWind(0,0,hx[j],hy[j],w[i],du,a[i])) {
						continue;
					}
					//---梅---//
					for(int k = 0; k < U; k++) {
						if(ReachWind(ux[k],uy[k],hx[j],hy[j],w[i],du,a[i])) {
							continue IN;
						}
					}
					//---桃---//
					for(int k = 0; k < M; k++) {
						if(ReachWind(mx[k],my[k],hx[j],hy[j],w[i],dm,a[i])) {
							continue IN;
						}
					}
					//---桜---//
					for(int k = 0; k < S; k++) {
						if(ReachWind(sx[k],sy[k],hx[j],hy[j],w[i],ds,a[i])) {
							continue IN;
						}
					}
					day[j]++;
					
					
					
					
				}
			}
			int max = 0;
			for(int i = 0; i < H; i++) {
				if(max < day[i]) {
					max = day[i];
				}
			}
			if(max != 0) {
				boolean tmp = false;
				for(int i = 0; i < H; i++) {
					if(!tmp && max == day[i]) {
						System.out.print((i + 1));
						tmp = true;
					}
					else if(max == day[i]) {
						System.out.print(" " + (i+1));
					}
				}
				System.out.println();
			}
			else {
				System.out.println("NA");
			}
		}
	}
	
	public static boolean ReachWind(int x0, int y0, int x1, int y1, int w, int d, int a) {
		//---長さ---//
		if(Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1)*(y0 - y1)) > a) {
			return false;
		}
		
		//---角度(x軸から何度か)---//
		int xlen = x1 - x0;
		int ylen = y1 - y0;
		double angle = Math.atan2(ylen, xlen);
		angle = Math.toDegrees(angle);
		double ue = w + d/2.0;
		double shita = w - d/2.0;
		if(angle >= shita && angle <= ue) {
			return true;
		}
		angle += 360;
		if(angle >= shita && angle <= ue) {
			return true;
		}
		angle -= 1080;
		if(angle >= shita && angle <= ue) {
			return true;
		}
		return false;
		
	}
}
