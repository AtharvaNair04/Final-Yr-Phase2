import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class Main {
    static class Calculator {
        private final Map<Character, Set<Integer>> sets;
        private final char[] input;
        private final Set<Integer> U;
        private int ptr = 0;

        public Calculator(Map<Character, Set<Integer>> sets, char[] expr) {
            this.sets = sets;
            this.input = expr;
            Set<Integer> U = new HashSet<Integer>();
            for (Set<Integer> set: sets.values()) {
                U.addAll(set);
            }
            this.U = U;
        }

        private Set<Integer> expr() {
            Set<Integer> value = term();
            while (ptr < input.length) {
                if (input[ptr] == 'u') {
                    ptr++;
                    value.addAll(term());
                } else if (input[ptr] == 'i') {
                    ptr++;
                    value.retainAll(term());
                } else if (input[ptr] == 'd') {
                    ptr++;
                    value.removeAll(term());
                } else if (input[ptr] == 's') {
                    ptr++;
                    Set<Integer> A = value;
                    Set<Integer> A1 = new HashSet<Integer>(A);
                    Set<Integer> B = term();
                    Set<Integer> B1 = new HashSet<Integer>(B);
                    A1.removeAll(B);
                    B1.removeAll(A);
                    A1.addAll(B1);
                    value = A1;
                } else {
                    break;
                }
            }
            return value;
        }

        private Set<Integer> term() {
            Set<Integer> value;
            if (input[ptr] == 'c') {
                ptr++;
                value = new HashSet<Integer>(U);
                value.removeAll(factor());
            } else {
                value = factor();
            }
            return value;
        }

        private Set<Integer> factor() {
            Set<Integer> value;
            if (input[ptr] == '(') {
                ptr++;
                value = expr();
                assert input[ptr] == ')';
                ptr++;
            } else {
                value = new HashSet<Integer>(sets.get(input[ptr]));
                ptr++;
            }
            return value;
        }

        public Set<Integer> run() {
            return expr();
        }
    }

    private static void solve() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            Map<Character, Set<Integer>> sets = 
                new HashMap<Character, Set<Integer>>();
            Set<Integer> result;
            while (true) {
                char name = scanner.next().charAt(0);
                int n = scanner.nextInt();
                if (name == 'R') {
                    char[] expr = scanner.next().toCharArray();
                    result = new Calculator(sets, expr).run();
                    break;
                } else {
                    Set<Integer> value = new HashSet<Integer>();
                    for (int i = 0; i < n; i++) {
                        value.add(scanner.nextInt());
                    }
                    sets.put(name, value);
                }
            }
            boolean space = false;
            Object[] elements = result.toArray();
            Arrays.sort(elements);
            if (elements.length == 0) {
                System.out.println("NULL");
            } else {
                for (Object e: elements) {
                    if (space) {
                        System.out.print(' ');
                    }
                    System.out.print(e);
                    space = true;
                }
                System.out.println();
            }
        }
    }

    public static void main(String... args) {
        solve();
    }
}
