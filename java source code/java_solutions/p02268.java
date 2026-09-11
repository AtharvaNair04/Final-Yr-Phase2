import javax.sound.midi.Soundbank;
import java.util.Scanner;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.lang.System.setOut;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //探索対象を取得
        int size = (scanner.nextInt());
        int [] S= new int[size];
        for (int i = 0; i < size; i++) {
            S[i] = scanner.nextInt();
        }

        //探索する値を取得
        int ansSize = (scanner.nextInt());
        int[] T = new int[ansSize];
        for (int i = 0; i < ansSize; i++) {
            T[i] = scanner.nextInt();
        }

        //System.out.println("読み込み完了！");
        double roopCount = 0;
        int count = 0;
        //探索用のアルゴリズム
        for (int i = 0; i < ansSize; i++) {
            if(isContain(T[i],S)){
                count++;
            }
        }

        System.out.println(count);
    }

    static  boolean isContain (int num , int[] arr){
        int head = 0;
        int tail = arr.length-1;
        int mid  = (head+tail)/2;

        while (mid != head){
            if(arr[mid] == num){
                return  true;
            }
            else if(arr[mid]< num){
                head = mid;
            }
            else if(arr[mid] > num){
                tail = mid;
            }
            mid = (tail+head)/2;
        }
        return arr[mid]==num || arr[tail]==num;

    }
}
