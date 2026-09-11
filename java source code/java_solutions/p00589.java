import java.util.Scanner;

class Main {
    private static void solve() {
        final Scanner scanner = new Scanner(System.in);
        final char[][] keymap =
                { null,
                  { '\'', ',', '.', '!', '?' },
                  { 'a', 'b', 'c', 'A', 'B', 'C' },
                  { 'd', 'e', 'f', 'D', 'E', 'F' },
                  { 'g', 'h', 'i', 'G', 'H', 'I' },
                  { 'j', 'k', 'l', 'J', 'K', 'L' },
                  { 'm', 'n', 'o', 'M', 'N', 'O' },
                  { 'p', 'q', 'r', 's',  'P', 'Q', 'R', 'S' },
                  { 't', 'u', 'v', 'T', 'U', 'V' },
                  { 'w', 'x', 'y', 'z', 'W', 'X', 'Y' , 'Z' }
                 };
        while (scanner.hasNext()) {
            final char[] line = scanner.next().toCharArray();
            int index = 0;
            while (index != line.length) {
                final char digit = line[index];
                int counter = 1;
                while (++index < line.length && line[index] == digit) {
                    counter++;
                }
                if (keymap[digit - '0'] == null) {
                    for (int i = 1; i < counter; i++) {
                        System.out.print(' ');
                    }
                } else {
                    final int length = keymap[digit - '0'].length;
                    System.out.print(keymap[digit - '0'][(counter - 1) % length]);
                }
            }
            System.out.println();
        }
    }

    public static void main(String... args) {
        solve();
    }
}
