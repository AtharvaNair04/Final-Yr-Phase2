import java.util.Scanner;

//Factorization of Quadratic Formula
public class Main{

	long gcd(long a, long b){
		return b==0?a:gcd(b, a%b);
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		while(true){
			int a = sc.nextInt();
			int b = sc.nextInt();
			int c = sc.nextInt();
			if((a|b|c)==0)break;
			double D = b*b-4*a*c;
			if(D<0)System.out.println("Impossible");
			else if(D==0){
				int A = 2*a;
				int B = -b;
				long g = gcd(Math.abs(A), Math.abs(B));
				A/=g;
				B/=g;
				System.out.println(A+" "+(-B)+" "+A+" "+(-B));
			}
			else{
				int k = 1;
				while(k*k<=D){
					if(k*k==D)break;
					k++;
				}
				if(k*k>D){
					System.out.println("Impossible");continue;
				}
				int P = 2*a;
				int Q = -b+k;
				int R = 2*a;
				int S = -b-k;
				long g = gcd(Math.abs(P), Math.abs(Q));
				P/=g;
				Q/=g;
				g = gcd(Math.abs(R), Math.abs(S));
				R/=g;
				S/=g;
				Q=-Q;
				S=-S;
				if(P>R||(P==R&&Q>S))System.out.println(P+" "+Q+" "+R+" "+S);
				else System.out.println(R+" "+S+" "+P+" "+Q);
			}
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
