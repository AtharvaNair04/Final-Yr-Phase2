import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        String weather[] = new String[3];
        String s = scanner.next();

        weather[0] = "Sunny";
        weather[1] = "Cloudy";
        weather[2] = "Rainy";

        for (int i=0; i <= weather.length; i++) {
            if (weather[i].equals(s)) {
                System.out.println(i >= 2 ? weather[0] : weather[i+1]);
                break;
            }
        }
    }
}
