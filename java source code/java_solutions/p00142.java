import java.util.*;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			int n = sc.nextInt();
			if(n==0) break;
			
			ArrayList<Integer> list = new ArrayList<Integer>();
			int[] b = new int[n];
			int count = 0;
			int p;
			
			for(int i=1;i<n;i++){
				p = (i*i)%n;
				if(list.contains(p)==false){
					list.add(p);
					b[count] = p;
					count++;
				}
			}

			int[] ans = new int[(n-1)/2+1];
			
			for(int i=0;i<count;i++){
				for(int j=0;j<count;j++){
					if(i==j) continue;
					p = b[i]-b[j];
					if(p<0) p = p+n;
					if(p>(n-1)/2) p = n-p;
					ans[p]++;
				}
			}

			for(int i=1;i<=(n-1)/2;i++) System.out.println(ans[i]);
		}	
	}	
}
