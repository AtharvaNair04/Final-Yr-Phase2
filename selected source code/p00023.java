import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        double xa, ya, ra, xb, yb, rb, s;
        while ((line = in.readLine()) != null) {
            int n = Integer.parseInt(line);
            while (n != 0) {
                n--;
                line = in.readLine();
                String[] strs = line.split("\\s+");
                xa = Double.parseDouble(strs[0]);
                ya = Double.parseDouble(strs[1]);
                ra = Double.parseDouble(strs[2]);
                xb = Double.parseDouble(strs[3]);
                yb = Double.parseDouble(strs[4]);
                rb = Double.parseDouble(strs[5]);
                s = Math.sqrt((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb));
                if (s + rb < ra) {
                    System.out.println("2");
                } else if (s + ra < rb) {
                    System.out.println("-2");  
                } else if (s <= ra + rb) {
                    System.out.println("1");
                } else if (s > ra + rb) {
                    System.out.println("0");
                }
            }
        } 
    }
}
