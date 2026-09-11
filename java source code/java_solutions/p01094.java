import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            int n = sc.nextInt();

            if (n == 0)
                break;

            SortMap[] list = new SortMap[26];
            for (int i = 'A'; i <= 'Z'; i++) {
                list[i - 65] = new SortMap((char)i);
            }

            int i = 0;
            for (i = 0; i < n; i++) {
                int a = (int) sc.next().charAt(0);
                for (SortMap m: list) {
                    if (m.i == a) {
                        m.n ++;
                        break;
                    }
                }
                Arrays.sort(list);
                SortMap sa = list[0];
                SortMap sb = list[1];
                // System.out.println(sa);
                // System.out.println(sb);
                if (sa.n > sb.n + (n - i - 1)) {
                    for (int j = 0; j < n - i - 1; j++) {
                        sc.next();
                    }
                    break;
                }
            }

            Arrays.sort(list);
            SortMap s1 = list[0];
            SortMap s2 = list[1];
            if (s1.n == s2.n) {
                System.out.println("TIE");
            } else {
                System.out.println(((char) s1.i) + " " + ((int)i + 1));
            }

//            for (int i = 0; i < n; i++) {
//                int tmp = (int)sc.next().charAt(0) -65;
//
//                nums[tmp]++;
//
//                if (first != tmp) {
//                    if (nums[second] < nums[tmp]) {
//                        if (nums[first] < nums[tmp]) {
//                            second = first;
//                            first = tmp;
//                        }
//                        else {
//                            if (second != tmp) {
//                                second = tmp;
//                            }
//                        }
//                    }
//                }
//
//                if (nums[first] > nums[second] + (n - i - 1)) {
//                    for (int j = 0; j < n - i - 1; j++) {
//                        sc.next();
//                    }
//                    break;
//                }
//            }
//
//            if (nums[first] == nums[second])
//                System.out.println("TIE");
//            else
//                System.out.println((char)(first+65) + " " + nums[first]);
        }
    }

    static class SortMap implements Comparable<SortMap> {
        public char i;
        public int n;

        public SortMap(char i) {
            this.i = i;
            this.n = 0;
        }

        @Override
        public String toString() {
            return "SortMap{" +
                    "i=" + i +
                    ", n=" + n +
                    '}';
        }
        @Override
        public int compareTo(SortMap o) {
            return o.n - this.n;
        }
    }
}
