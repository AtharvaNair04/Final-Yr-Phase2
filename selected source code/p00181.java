import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)){
			while(sc.hasNext()) {
				int m=sc.nextInt();
				int n=sc.nextInt();
				if(m+n==0) break;
				int[] w=new int[n+1];
				int max=0;
				int sum=0;
				for(int i=0; i<n; i++) {
					w[i]=sc.nextInt();
					max=Math.max(w[i], max);
					sum+=w[i];
				}
				int mini=0;
				for(int i=max; i<=sum; i++) {
					int h=1;
					int[] book=new int[n+1];
					int end=0;
					for(int j=0; j<n; j++) {
						if(book[h]+w[j]>i) {
							h++;
							if(h>m) {
								end=1;
								break;
							}
						}
						book[h]+=w[j];
					}
					mini=i;
					if(end==0) break;
				}
				System.out.println(mini);

			}
		}
	}
}
