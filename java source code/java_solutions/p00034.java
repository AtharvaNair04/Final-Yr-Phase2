import java.util.Scanner;
 
//Railway Lines
public class Main{
 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String[] s = sc.next().split(",");
            int[] a = new int[11];
            int sum = 0;
            for(int i=0;i<10;i++){
                a[i]=Integer.parseInt(s[i]);
                sum += a[i];
            }
            double v1 = Double.parseDouble(s[10]);
            double v2 = Double.parseDouble(s[11]);
            int d = 0;
            for(int i=0;i<10;i++){
                d += a[i];
                double t = d/v1;
                if(d + t*v2 >= sum){
                    System.out.println(i+1);
                    break;
                }
            }
        }
    }
}
