import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int[] l=new int[n];
        for(int i=0;i<n;i++) {
            l[i]=sc.nextInt();
        }
        Arrays.sort(l);
        List<Integer> a=new ArrayList<>();
        for(int i=0;i<Math.min(n, 4);i++) {
            for(int j=0;j<Math.min(n, 4);j++) {
                if(i==j)continue;
                a.add(Integer.parseInt(l[i]+""+l[j]));
            }
        }
        Collections.sort(a);
        System.out.println(a.get(2));
    }
}
