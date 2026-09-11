// 問題URL
// https://atcoder.jp/contests/abc158/tasks/abc158_b

import java.util.Scanner;

public class Main
{
    public static void main(String [] args)
    {
        Scanner scaanner = new Scanner(System.in);
        long num = scaanner.nextLong();
        long blue = scaanner.nextLong();
        long red = scaanner.nextLong();

        // 赤と青を合計することでグループ化する
        // 割り切れる数値
        long divisible_group = (num / (blue+red));
        // 割り切れない数値
        long indivisible_group = (num % (blue+red));
        // 合計値
        long total =  divisible_group * blue;

        if (indivisible_group <= blue) {
            System.out.println(total+ indivisible_group);
        } else {
            System.out.println(total+ blue);
        }

    }
}
