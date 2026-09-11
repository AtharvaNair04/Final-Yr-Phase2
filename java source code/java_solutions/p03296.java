import java.util.Arrays;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
    sc.nextLine();
        String input = sc.nextLine();
//System.out.println(input);
       System.out.println(execute(input));

    }

    public static int execute(String input) {

       long[] list = Arrays.asList(input.split(" ")).stream().mapToLong(Long::parseLong).toArray();
        int sum = 0;

        for (int i = 1; i <= list.length - 1; i++) {
            if (list[i-1] == list[i]) {
                i++;
                sum++;
            }
        }
        return sum;

    }
}
