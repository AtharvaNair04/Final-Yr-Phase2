import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner a =  new Scanner(System.in);
        int W = a.nextInt(), H = a.nextInt(), x = a.nextInt(), y = a.nextInt(), r = a.nextInt();
        String b;
  
        if(x + r <= W && x - r  >=0 ){
            if(y + r <= H && y - r >= 0){
                b = "Yes";
            }else {
                    b = "No";
	}
	}else {
            b = "No";
        }
         System.out.println(b);
    }
    
}
