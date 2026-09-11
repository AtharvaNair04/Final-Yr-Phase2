import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double r=Double.parseDouble(br.readLine());

        double x=Math.PI;

        double a=r*r*x;
        double b=2*x*r;

        double c = a;
        String menseki=String.format("%.6f",c);
        c = b;
        String gaisyu=String.format("%.6f",c);

        System.out.println(menseki+" "+gaisyu);
	}

}
