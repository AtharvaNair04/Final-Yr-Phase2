import java.io.BufferedReader;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

public class Main {
	public static void main(final String[] args){
    
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        List<Integer> list = new ArrayList<>();
        int sum = 0;
        for(int i = 0 ; i < m ; i++){
            list.add(sc.nextInt());
        }

        for(int i = 0 ; i < m ; i++){
            sum += list.get(i);
        }

        if(n < sum){
            System.out.println(-1);
        }else{
            System.out.println(n - sum);
        }

        

            
                


    }
}
