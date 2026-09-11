import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        float T = Integer.parseInt(sc.next());
        float X = Integer.parseInt(sc.next());
        out.println(T/X);
        out.flush();
    }
}
