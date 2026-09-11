import java.util.Scanner;
public class Main{
    int high;
    int low;
    int sa;
    
    public void soleve(){
        Scanner sc = new Scanner(System.in);
        for(int i=1;i<8;i++){
			high = sc.nextInt();
        	low = sc.nextInt();
        	sa = high - low;
			System.out.println(sa);
        }
        
    }
    
    public static void main(String[] args){
        Main obj = new Main();
        obj.soleve();
    }
}
