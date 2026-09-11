import java.util.*;
public class Main {
    public static void main(String[] args) {
	Scanner scan = new Scanner(System.in);
	int[] k = new int[10000];
	while(true) {
	    int n = scan.nextInt();
	    boolean ok = false;
	    if(n == 0) break;
	    int cnt = 0;
	    for(int i = 0; i < n; i++) {
		k[i] = scan.nextInt();
		if(k[i] >= 1) cnt++;
		if(k[i] >= 2) ok = true;
	    }
	    if(ok != true) {
		System.out.println("NA");
	    }else{
		System.out.println(cnt +1);
	    }
	}
    }
}
