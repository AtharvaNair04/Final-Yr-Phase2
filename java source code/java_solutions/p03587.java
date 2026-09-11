import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
	Main m = new Main();
	m.answer();
    }

    private Scanner scan = new Scanner(System.in);
    private String S;

    public Main() {
	S = scan.next();
	scan.close();
    }

    public final void answer() {
	long count = IntStream.range(0, S.length())
	    .filter(i -> S.charAt(i) == '1')
	    .count();
	System.out.println(count);
    }
}
