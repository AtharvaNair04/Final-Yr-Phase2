import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inputStr = sc.next();
        int input = Integer.parseInt(inputStr);
        
        int oddCount = input % 2 == 0 ? input / 2 : (input / 2) + 1;
        String oddCountStr = Integer.toString(oddCount);
        
        double rate = (Double.parseDouble(oddCountStr) / Double.parseDouble(inputStr));

        System.out.println(rate);
    }
}
