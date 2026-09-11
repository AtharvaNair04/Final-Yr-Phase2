import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        int totalRow = console.nextInt();
        int totalCol = console.nextInt();

        console.nextLine();

        int row = console.nextInt();
        int col = console.nextInt();

        int total = totalRow * totalCol;
        int black = (row * totalCol) + (col * totalRow) - (row * col);

        System.out.println(total - black);
    }
}
