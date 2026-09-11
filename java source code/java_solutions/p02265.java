import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {

	public static void main(String[] args) throws Exception {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(reader.readLine());

		Deque<Integer> deque = new ArrayDeque<Integer>(2000000);
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            if(line.charAt(0) == 'i')
                deque.push(Integer.parseInt(line.substring(7)));
            else if(line.charAt(6)=='F')
                deque.removeFirst();
            else if(line.charAt(6)=='L')
                deque.removeLast();
            else
                deque.remove(Integer.parseInt(line.substring(7)));
        }

		builder.append(deque.poll());
		for(int num : deque)
			builder.append(" ").append(num);
		System.out.println(builder);
	}

}
