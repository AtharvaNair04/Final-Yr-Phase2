import java.util.*;
public class Main {
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int m = kbd.nextInt();
	    int n = kbd.nextInt();
	    if(m!=0 && n!=0){
		int c;
		int max=0;
		for(int i=0; i<m; i++){
			c=0;
			for(int j=0; j<n; j++){
			    int x =kbd.nextInt();
			    if(x==1) c++;
			}
			if(c>max) max = c;
		}
		System.out.println(max);
	    }
	}
    }
}
