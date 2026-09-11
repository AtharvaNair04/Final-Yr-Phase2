import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int sx = scan.nextInt();
        int sy = scan.nextInt();
        int tx = scan.nextInt();
        int ty = scan.nextInt();

        StringBuilder sb = new StringBuilder();

        sb.append('U');
        search(sb, sx, sy + 1, tx - 1, ty);
        sb.append('R');

        sb.append('D');
        search(sb, tx, ty - 1, sx + 1, sy);
        sb.append('L');

        sb.append('L');
        search(sb, sx - 1, sy, tx, ty + 1);
        sb.append('D');

        sb.append('R');
        search(sb, tx + 1, ty, sx, sy - 1);
        sb.append('U');

        System.out.println(sb.toString());
    }

    public static void search(StringBuilder sb, int x1, int y1, int x2, int y2) {
        if (y1 < y2) {
            for (int i = 0; i < y2 - y1; ++i) {
                sb.append('U');
            }
        } else {
            for (int i = 0; i < y1 - y2; ++i) {
                sb.append('D');
            }
        }

        if (x1 < x2) {
            for (int i = 0; i < x2 - x1; ++i) {
                sb.append('R');
            }
        } else {
            for (int i = 0; i < x1 - x2; ++i) {
                sb.append('L');
            }
        }
    }

}
