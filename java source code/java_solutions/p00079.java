import java.util.Scanner;

public class Main {
	static double dx[]=new double[19];
	static double dy[]=new double[19];
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		double x[]=new double[20];
		double y[]=new double[20];
		
		int i,k=0;
		double sum=0;
		while(sc.hasNext()){
			String str[]=sc.next().split(",");
			x[k]=Double.parseDouble(str[0]);
			y[k]=Double.parseDouble(str[1]);
			k++;
		}
		for(i=1;i<k;i++){
			dx[i-1]=x[i]-x[0];
			dy[i-1]=y[i]-y[0];
		}
		for(i=0;i+1<k-1;i++){
			sum+=area(dx[i],dy[i],dx[i+1],dy[i+1]);
		}
		System.out.printf("%.6f\n",sum);
	}
	public static double area(double a,double b,double c,double d){
		double s=a*d-b*c;
		if(s<0) s=s*(-1);
		return s/2;
	}
}
