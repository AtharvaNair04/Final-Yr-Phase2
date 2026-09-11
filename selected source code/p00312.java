import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int D = scan.nextInt();
        int L = scan.nextInt();
        int x;
        int y;
        x = D/L;
        y = D%L;
        System.out.println(x+y);

    }

}
