import java.util.*;
public class Main{
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
        for(int i=n; i>=0; i--){
            if(Math.sqrt(i)==(int)Math.sqrt(i)){
                System.out.println(i);
                break;
            }
        }
        
	}
}
