import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    if(n != 0){
		int change = 1000 - n;
		int cnt = 0;
		while(change >= 500){
		    change -= 500;
		    cnt++;
		}
		while(change >= 100){
		    change -= 100;
		    cnt++;
		}
		while(change >= 50){
		    change -= 50;
		    cnt++;
		}
		while(change >= 10){
		    change -= 10;
		    cnt++;
		}
		while(change >= 5){
		    change -= 5;
		    cnt++;
		}
		while(change >= 1){
		    change -= 1;
		    cnt++;
		}
		System.out.println(cnt);
	    }
	}
    }
}
