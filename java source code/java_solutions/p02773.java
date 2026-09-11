import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        Integer N = Integer.parseInt(scanner.next());

        Map<String, Integer> map = new HashMap<String, Integer>();
        Integer max = 0;
        for (int i = 0; i < N; i++) {
            String key = scanner.next();
            Integer count = map.get(key);
            Integer val = (count == null) ? 1 : count + 1;
            map.put(key, val);
            max = Math.max(max, val);
        }


        List<String> moji = new ArrayList<>();
        for (String key : map.keySet()) {
            if((int)map.get(key) == (int)max) {
                moji.add(key);
            }
        }
        moji.stream().sorted().collect(Collectors.toList()).forEach(System.out::println);
    }
}
