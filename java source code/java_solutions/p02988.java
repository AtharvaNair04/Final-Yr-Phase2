import java.util.Scanner;
public class Main {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int x=sc.nextInt();
        int[] array=new int[x];
        for(int i=0;i<x;i++){
            array[i]=sc.nextInt();
        }
        int count=0;
        for(int i=1;i<x-1;i++){
            if((array[i-1]>array[i] && array[i]>array[i+1]) || (array[i+1]>array[i] && array[i]>array[i-1]))
                count++;
        }
        System.out.println(count);
    }
}
