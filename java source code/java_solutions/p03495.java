import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        int[] num = new int[3000000];
        int count = 0;
        for (int i = 0; i < n; i++) {
            num[a[i]]++;
            if (num[a[i]] == 1) {
                count++;
            }
        }
        int[] shurui = new int[count];
        Arrays.sort(num);
        for (int i = 0; i < count; i++) {
            shurui[i] = num[3000000 - 1 - i];
        }
        Arrays.sort(shurui);
        int ansnum = count - k;
        int ans = 0;
        for (int i = 0; i < ansnum; i++) {
            ans += shurui[i];
        }
        System.out.println(ans);
    }
}
