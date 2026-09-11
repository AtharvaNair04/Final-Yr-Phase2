import java.util.*;

public class Main {
    private static Integer N;
    private static List<List<List<Integer>>> map = new ArrayList<>();

    public static boolean judge(Integer bit) {

        for (int i = 0; i < N; ++i) {
            // i人目が「不親切」だったら、証言は無意味
            if ((bit & (1 << i)) == 0) continue;

            List<List<Integer>> xys = map.get(i);
            // それぞれ確認
            for (List<Integer> xy : xys) {
                int x = xy.get(0);
                int y = xy.get(1);

                if (y == 1 && ((bit & (1 << x)) == 0)) return false;
                if (y == 0 && ((bit & (1 << x)) != 0)) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        N = Integer.parseInt(scanner.next());

        // ここで初期化
        for (int i = 0; i < N; i++) {
            int A = Integer.parseInt(scanner.next());
            List<List<Integer>> xys = new ArrayList<>();

            for (int j = 0; j < A; ++j) {
                List<Integer> xy = new ArrayList<>();
                xy.add(Integer.parseInt(scanner.next()) - 1);
                xy.add(Integer.parseInt(scanner.next()));
                xys.add(xy);
            }
            map.add(xys);
        }

        int res = 0;
        for (int bit = 0; bit < (1 << N); ++bit) {
            if (judge(bit)) {
                int count = 0;
                for (int i = 0; i < N; ++i) {
                    if ((bit & (1 << i)) != 0) ++count;
                }
                res = Math.max(res, count);
            }
        }

        System.out.println(res);
    }
}
