import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int num = Integer.parseInt(in.nextLine());
        List<Integer> bits = new ArrayList<>();
        List<Boolean> isPut = new ArrayList<>();
        for (int i = 0; i < num; i++) {
             bits.add(in.nextInt());
             isPut.add(false);
        }
        in.close();
        int snukeWork = 0;
        for (int i = num - 1; i >= 0; i--) {
            int count = 0;
            for(int j = 1; i + (i + 1) * j < num; j++) {
                if(isPut.get(i + (i + 1) * j) ) {
                    count++;
                }
            }
            isPut.set(i, count % 2 != bits.get(i));
            if (isPut.get(i)) {
                snukeWork++;
            }
        }
        System.out.println(snukeWork);
        StringBuilder sb = new StringBuilder(snukeWork * 5);
        for (int i = 0; i < num; i++) {
            if (isPut.get(i)) {
                sb.append(i + 1);
                sb.append(" ");
            }
        }
        if (snukeWork != 0) System.out.println(sb.substring(0, sb.length()-1));

    }

}
