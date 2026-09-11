import java.io.*;
public class Main{
        public static void main(String[] args) throws IOException {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String is; 
                while((is = in.readLine()) != null){
                        int n = Integer.parseInt(is);
                        if(n == 0){
                                System.out.println(0);
                                continue;
                        } else if(n == 1){
                                System.out.println(1);
                                continue;
                        } else if(n == 2){
                                System.out.println(2);
                                continue;
                        }
                        int c = 2;
                        int d = 0;
                        for(int i = 2;i < n; ++i){
                                if(i % 2 == 0){
                                        d = d * 3 + 1;
                                }
                                c += d * 2 + 1;
                        }
                        System.out.println(c);
                }   
        }   
}
