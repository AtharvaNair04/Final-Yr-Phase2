import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int p = sc.nextInt();
		String[]a=new String[p+1];
		String[] r = {"AC", "WA", "TLE", "RE"};
		int[] x= {0,0,0,0};
		for(int i=0;i<p+1;++i) {
			a[i]= sc.nextLine();


		}
		for(int i=1;i<p+1;i++) {
			for(int j=0;j<4;j++) {
				if(a[i].equals(r[j])) {
					x[j]++;
				}
			}
		}
			for(int i =0;i<4;i++) {
				System.out.println(r[i]+" x "+x[i]);
			}


		}

	}
