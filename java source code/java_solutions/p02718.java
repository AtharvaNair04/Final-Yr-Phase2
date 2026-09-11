import java.lang.Math;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);

        String str = scanner.nextLine();
        String str2 = scanner.nextLine();
        String[] params = str.split(" ", 0);
        String[] params2 = str2.split(" ", 0);

        int[] intArray = new int[params.length];
        int[] intArray2 = new int[params2.length];

        for (int i = 0; i < params.length; i++) {
            intArray[i] = Integer.parseInt(params[i]);
        }

        int total_vote = 0;
        for (int j = 0; j < params2.length; j++) {
            intArray2[j] = Integer.parseInt(params2[j]);
            total_vote += Integer.parseInt(params2[j]);
        }

        double test = 1 / (4 * intArray[1]);
        //int minimum_vote = (int)(total_vote * (double)1 / (4 * intArray[1]));
        int minimum_vote = (int)Math.ceil((total_vote * (double)1 / (4 * intArray[1])));
        int chose_product = 0;

        for (int k = 0; k < params2.length; k++) {
            if(intArray2[k] >= minimum_vote) {
                chose_product++;
            }
        }
        //System.out.println(total_vote);
        //System.out.println(minimum_vote);


        if (chose_product >= intArray[1]) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}
