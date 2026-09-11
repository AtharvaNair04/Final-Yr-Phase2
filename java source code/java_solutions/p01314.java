import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    if(n!=0){
		int max = n/2 + 1;
		int min = max -1;
		int cnt = 0;
		int sum = max+min;
		while(min!=0){
		    if(sum == n){
			cnt++;
			min -= 1;
			sum += min;
		    }
		    else if(sum > n){
			sum -= max;
			max -= 1;
		    }
		    else if(sum < n){
			min -= 1;
			sum += min;
		    }
		}
		
		System.out.println(cnt);
	    }
	}
    }
}
