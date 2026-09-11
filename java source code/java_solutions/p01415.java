import java.util.*;

public class Main{
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			int n = sc.nextInt();
			int k = sc.nextInt();
			int t = sc.nextInt();
			int u = sc.nextInt();
			int v = sc.nextInt();
			int l = sc.nextInt();
			int [] d = new int[n];
			for(int i = 0; i < n; i++){
				d[i] = sc.nextInt();
			}
			int stock = 0, start = 0,end = 0, sum = 0;
			for(int i = 0; i < n; i++){
				if(d[i] <= end){
					if(stock == k){
						end = Math.min(l, d[i] + t * v);
					}
					else{
						stock++;
					}
				}
				else{
					while(stock != 0 && d[i] > end){
						stock--;
						end = Math.min(l, end + t * v);
					}
					if(d[i] <= end){
						stock++;
					}
					else{
						sum += end - start;
						start = d[i];
						end = Math.min(l, d[i] + t * v);
					}
				}
			}
			sum += Math.min(l, (long)end + (long)stock * t * v) - start;
			double res = (double)(l - sum) / u + (double)sum / v;
			System.out.printf("%.7f\n", res);
		}
	}

	private void debug(Object... o) {
		System.out.println("debug = " + Arrays.deepToString(o));
	}

	public static void main(String[] args) {
		new Main().doit();
	}
}
