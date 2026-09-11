import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	static double INF=1e19;
	static double eps=1e-9;
	
	static class State implements Comparable<State>{
		int n1, n2;
		double timer;
		State(int n1, int n2, double timer){
			this.n1=n1;
			this.n2=n2;
			this.timer=timer;
		}
		public int compareTo(State s) {
			if(this.timer>s.timer) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}
	
	static double calc(double A, double B, double C) {
		if(A!=0 && B*B-4*A*C>eps) {
			if((-1*B-Math.sqrt(B*B-4*A*C))/(2*A)>eps) {
				return (-1*B-Math.sqrt(B*B-4*A*C))/(2*A);
			}
			else if((-1*B+Math.sqrt(B*B-4*A*C))/(2*A)>eps) {
				return (-1*B+Math.sqrt(B*B-4*A*C))/(2*A);
			}
		}
		if((Math.abs(A)<eps && B!=0) && -1*C/B>eps) {
			return -1*C/B;
		}
		return INF;
	}
	
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)){
			while(sc.hasNext()) {
				int n=sc.nextInt();
				if(n==0) break;
				
				double[] time=new double[n];//流れ星消滅の時間
				double[] px=new double[n];
				double[] py=new double[n];
				double[] pz=new double[n];
				double[] vx=new double[n];
				double[] vy=new double[n];
				double[] vz=new double[n];
				double[] r=new double[n];
				double[] vr=new double[n];
				
				for(int i=0; i<n; i++) {
					px[i]=sc.nextDouble();
					py[i]=sc.nextDouble();
					pz[i]=sc.nextDouble();
					vx[i]=sc.nextDouble();
					vy[i]=sc.nextDouble();
					vz[i]=sc.nextDouble();
					r[i]=sc.nextDouble();
					vr[i]=sc.nextDouble();
				}
				PriorityQueue<State> pq=new PriorityQueue<>();
				
				for(int i=0; i<n; i++) {
					if(vr[i]!=0) {
						time[i]=r[i]/vr[i];
					}
					else {
						time[i]=INF;
					}
				}
				
				for(int i=0; i<n; i++) {
					for(int j=i+1; j<n; j++) {
						double A=-Math.pow(vr[i]+vr[j], 2)+Math.pow(vx[i]-vx[j], 2)+Math.pow(vy[i]-vy[j], 2)+Math.pow(vz[i]-vz[j], 2);
						double B=2*((vr[i]+vr[j])*(r[i]+r[j])+(vx[j]-vx[i])*(px[j]-px[i])+(vy[j]-vy[i])*(py[j]-py[i])+(vz[j]-vz[i])*(pz[j]-pz[i]));
						double C=-Math.pow(r[i]+r[j], 2)+Math.pow(px[i]-px[j], 2)+Math.pow(py[i]-py[j], 2)+Math.pow(pz[i]-pz[j], 2);
						
						pq.add(new State(i, j, calc(A, B, C)));
						
					}
				}
				while(! pq.isEmpty()) {
					State state=pq.poll();
					if(time[state.n1]<state.timer || time[state.n2]<state.timer) {
						//消滅できない
						continue;
					}
					time[state.n1]=state.timer;
					time[state.n2]=state.timer;
				}
				for(int i=0; i<n; i++) {
					System.out.printf("%.12f\n", time[i]);
				}
			}
		}
	}
}
