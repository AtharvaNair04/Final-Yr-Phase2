import java.util.*;
import java.io.*;
import java.math.*;
public class Main {
    public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		PrintWriter ou = new PrintWriter(System.out);
		int n = Integer.parseInt(sc.next());
		int x , y;
		int za = (int)Math.pow(10 , 9);
		int zb = -(int)Math.pow(10 , 9);
		int wa = (int)Math.pow(10 , 9);
		int wb = -(int)Math.pow(10 , 9);
		for(int i = 0 ; i < n ; i++){
			x = Integer.parseInt(sc.next());
			y = Integer.parseInt(sc.next());
			za = Math.min(x + y , za);
			zb = Math.max(x + y , zb);
			wa = Math.min(x - y , wa);
			wb = Math.max(x - y , wb);
		}
		sc.close();
		ou.println(Math.max(zb - za , wb - wa));
		ou.flush();
	}
}
