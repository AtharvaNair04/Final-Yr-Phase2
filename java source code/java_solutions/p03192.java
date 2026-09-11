import java.util.*;

public class Main {
	public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
      	int i = sc.nextInt();
        int countTwo = 0;
        while (i != 0) {
            int r = i % 10;
            i /= 10;
            if (r == 2) countTwo++;
        }
		System.out.println(countTwo);
    }
}
