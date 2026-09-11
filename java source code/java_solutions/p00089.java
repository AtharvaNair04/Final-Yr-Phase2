import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] result = new int[100];
        int c = 0;
        while (sc.hasNext()) {
            Integer[] l = Stream.of(sc.nextLine().split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            if (l.length == 1 && c == 0) {
                result[0] = l[0];
            } else if (c < l.length) {
                int[] temp = result.clone();
                for (int i = 0; i < l.length - 1; i++) {
                    result[i] = Math.max(result[i], temp[i] + l[i]);
                    result[i + 1] = Math.max(result[i + 1], temp[i] + l[i + 1]);
                }
            } else {
                for (int i = 0; i < l.length; i++) {
                    result[i] = Math.max(result[i] + l[i], result[i + 1] + l[i]);
                }
            }
            c = l.length;
        }
        System.out.println(result[0]);
    }
}
