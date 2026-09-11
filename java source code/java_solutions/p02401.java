import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		String[] strArray;
        int a, b;
        String op;

		while(true) {
			strArray = br.readLine().split(" ");
	        a = Integer.parseInt(strArray[0]);
	        b = Integer.parseInt(strArray[2]);
	        op = strArray[1];

	        if(op.equals("?")) {
	        	break;
	        }

	        switch(op) {
	        case "+":
	        	sb.append(a+b + "\n");
	        	break;
	        case "-":
	        	sb.append(a-b + "\n");
	        	break;
	        case "*":
	        	sb.append(a*b + "\n");
	        	break;
	        case "/":
	        	if(a==0) {
	        		sb.append("0" + "\n");
	        		break;
	        		}
	        	sb.append(a/b + "\n");
	        	break;
	        }
		}
        System.out.print(sb);
	}
}
