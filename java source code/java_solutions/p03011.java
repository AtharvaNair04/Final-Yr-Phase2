import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String[] line1 = line.split(" ");
        int p = Integer.parseInt(line1[0]);
        int q = Integer.parseInt(line1[1]);
        int r = Integer.parseInt(line1[2]);
        int min = p + q;
        if(p + r < min)
        min = p + r;
        if(q + r < min)
        min = q + r;
        System.out.println(min);
    }
}
