import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.*;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

//    public static void main(String[] args)
////    {
////        System.out.println("Hello World!");
////
//////    ArrayList<int> arr= new ArrayList<int>(1,2,3,4,5);
////        var arr= new ArrayList<Integer>();
////        arr.add(1);
////        arr.add(4);
////        arr.add(3);
////        arr.add(5);
////        arr.add(2);
////        Collections.sort(arr);
////        var max = Collections.max(arr);
////        var min = Collections.min(arr);
////        for (var value:
////             arr) {
////            System.out.println(value);
////        }
////        System.out.println(max);
////        System.out.println(min);
////    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        int x=sc.nextInt();
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (int i=0;i<m;i++){
            arr.add(sc.nextInt());
        }

        int tmp1 = 0, tmp2 = 0;
        for(Integer value : arr){
            if (value < x){
                tmp1++;
            }else{
                tmp2++;
            }
        }
        System.out.println(Math.min(tmp1,tmp2));
    }
}
