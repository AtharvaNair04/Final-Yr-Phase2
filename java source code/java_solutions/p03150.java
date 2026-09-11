import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.next();
        Pattern[] p = new Pattern[8];
        p[0] = Pattern.compile("^.*keyence$");
        p[1] = Pattern.compile("^k.*eyence$");
        p[2] = Pattern.compile("^ke.*yence$");
        p[3] = Pattern.compile("^key.*ence$");
        p[4] = Pattern.compile("^keye.*nce$");
        p[5] = Pattern.compile("^keyen.*ce$");
        p[6] = Pattern.compile("^keyenc.*e$");
        p[7] = Pattern.compile("^keyence.*$");
        for (int i = 0; i < 8; i++) {
            Matcher m = p[i].matcher(string);
            if (m.find()) {
                System.out.println("YES");
                return;
            }
        }
        System.out.println("NO");
    }

}
