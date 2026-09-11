import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    int m = kbd.nextInt();
	    if(n!=0 && m!=0){
		int[] date = new int[101];

		while(n!=0){
		    int s = kbd.nextInt();
		    while(s!=0){
			int a = kbd.nextInt();
			date[a]++;
			s--;
		    }
		    n--;
		}

		int ans = 0;
		for(int i=0; i<date.length; i++){
		    if(date[i]>=m && date[ans]<date[i])
			ans = i;
		}

		System.out.println(ans);
	    }
	}
    }
}
