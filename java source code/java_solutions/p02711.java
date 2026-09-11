import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int h = scanner.nextInt();
        String s = String.valueOf(h);
        for(int i = 0;i < s.length(); i++){
            if(s.charAt(i) == '7'){System.out.println("Yes");return;}
        }
        System.out.println("No");
    }
}
