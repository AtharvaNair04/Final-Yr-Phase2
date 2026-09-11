import java.io.*;
import java.util.*;
import java.math.*;
public class Main{
        public static void main(String[] arg) throws IOException {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String is; 
                String[] ia; 

                while((is = in.readLine()) != null){
                        ia = is.split(" ");
                        int n = Integer.parseInt(ia[0]);
                        int r = Integer.parseInt(ia[1]);
                        LinkedList<Integer> a,b,d;
                        d = new LinkedList<Integer>();
                        for(int i = 0;i < n; ++i){
                                d.addFirst(i);
                        }   
                        ia = in.readLine().split(" ");
                        for(int j = 0;j < r; ++j){
                                int c = Integer.parseInt(ia[j]);
                                a = new LinkedList<Integer>();
                                int x = d.size();
                                for(int i = 0;i < x/2.0; ++i){
                                        a.addLast(d.removeFirst());
                                }   
                                b = d;
                                d = new LinkedList<Integer>();
                                while(a.size() > 0 || b.size() > 0){ 
                                        for(int i = 0;i < c && a.size() > 0; ++i){
                                                d.addFirst(a.removeLast());
                                        }   
                                        for(int i = 0;i < c && b.size() > 0; ++i){
                                                d.addFirst(b.removeLast());
                                        }   
                                }   
                        }   
                        System.out.println(d.getFirst());
                }
        }
}
