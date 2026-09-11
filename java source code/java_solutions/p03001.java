import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        double w = scanner.nextDouble();
        double h = scanner.nextDouble();
        double x = scanner.nextDouble();
        double y = scanner.nextDouble();

        int count = 0;
        if(w/2 == x && h/2 == y){count++;}
        System.out.println((w*h/2.0) + " " + count);
        
    }
}
