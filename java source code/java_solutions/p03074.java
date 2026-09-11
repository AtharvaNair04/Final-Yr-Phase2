import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        String[] s = sc.next().split("");
        int[] len = new int[n + 3];
        int index = 0;
        boolean is0 = false;
        for (int i = 0; i < s.length; i++) {
            if (s[i].equals("0")) {
                if (!is0)index++;
                is0 = true;
            } else {
                if (is0)index++;
                is0 = false;
            }
            len[index]++;
        }
        int res = 0;
        for(int j=0;j<2*k+1;j++){
            if(j>index)break;
            res += len[j];
        }
        int final_res = res;
        for (int i = 2; i <= index; i += 2) {
            int tmp = 0;
            if(i+2*k-1>index)break;
            tmp = res - len[i-2] - len[i-1] + len[i+2*k] + len[i+2*k-1];
            res = tmp;
            final_res = Math.max(tmp, final_res);
        }
        System.out.println(final_res);
    }
}
