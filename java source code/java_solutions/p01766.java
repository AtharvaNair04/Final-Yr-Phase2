import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	Scanner in = new Scanner(System.in);
	public static void main(String[] args) {
		new Main();
	}
	public Main() {
		new RitProA().doIt();
	}
	class RitProA{
		void doIt(){
			int n = in.nextInt();
			ArrayList<Frame> list = new ArrayList<Frame>();
			for(int i=0;i<n;i++)list.add(new Frame(in.nextInt(),in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt()));
			Collections.sort(list);
			
			double maxKyori1 = -1.000000;
			int minTime1 = Integer.MAX_VALUE/2;
			double maxKyori2 = -1.000000;
			int minTime2 = Integer.MAX_VALUE/2;
			for(int i=0;i<n-1;i++){
				Frame f1 = list.get(i);
				Frame f2 = list.get(i+1);
				Point2D p1 = new Point2D.Double(f1.x,f1.y);
				Point2D p2 = new Point2D.Double(f2.x,f2.y);
				if(f1.t!=f2.t)continue;
				if(f1.t == 0){
					if(p1.distance(p2) > maxKyori1 && f1.a != f2.a){
						maxKyori1 = p1.distance(p2)*1.000000;
						minTime1 = f2.f - f1.f;
					}else if(Math.abs(p1.distance(p2) - maxKyori1) < 0.00000000001){
						maxKyori1 = p1.distance(p2)*1.000000;
						minTime1 = f2.f - f1.f;
					}
				}else{
					if(p1.distance(p2) >= maxKyori2 && f1.a != f2.a){
						maxKyori2 = p1.distance(p2)*1.000000;
						minTime2 = f2.f - f1.f;
					}else if(Math.abs(p1.distance(p2) - maxKyori2) < 0.00000000001){
							maxKyori2 = p1.distance(p2)*1.000000;
							minTime2 = f2.f - f1.f;
					}
				}
			}
			if(maxKyori1 == -1.0){
				System.out.println("-1 -1");
			}else System.out.printf("%.5f %.5f\n",maxKyori1,minTime1/60.0);
			if(maxKyori2 == -1.0)System.out.println("-1 -1");
			else System.out.printf("%.5f %.5f\n",maxKyori2,minTime2/60.0);
		}
	}
	
	class Frame implements Comparable<Frame>{
		int a,t,x,y,f;
		public Frame(int _f,int _a,int _t,int _x,int _y){
			this.f = _f;
			this.x = _x;
			this.y = _y;
			this.a = _a;
			this.t = _t;
		}
		public int compareTo(Frame f) {
			return this.f - f.f;
		}
		
		
	}
}
