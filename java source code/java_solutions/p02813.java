import java.util.*;

public class Main {
    public static List<String> permutation(String list) {
        List<String> ans = new ArrayList<>();
        if (list.length() == 1) {
            return Collections.singletonList(list);
        }
        for (int i = 0; i < list.length(); i++) {
            char pick = list.charAt(i);
            String rest = list.substring(0, i) + list.substring(i + 1);
            for (String rest_permutation : permutation(rest)) {
                ans.add(pick + rest_permutation);
            }
        }
        return ans;
    }
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer N = Integer.parseInt(scanner.next());
        String[][] line = new String[2][1];
        for(int i = 0; i < 2; i++) {
            Arrays.fill(line[i], "");
            for(int j = 0; j < N; j++) {
                line[i][0] += scanner.next();
            }
        }
        String number = "";
        for(int i = 1; i <= N; i++) {
            number += i;
        }

        List<String> p = permutation(number);
        Integer R = 0;

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < p.size(); i++) {
                if(p.get(i).equals(line[j][0])) {
                    if(R==0)R+=i;
                    else R-=i;
                };
            }

        }
        System.out.println(Math.abs(R));
    }
}
