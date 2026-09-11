// 問題URL
//

import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) {
        //コード
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.next());
        int gohyaku =0, hyaku = 0, gozyu = 0, zyu = 0, go = 0, ichi = 0;
        int sum = 0;
        int uresisa = 0;

        gohyaku = n / 500;
        sum += gohyaku * 500;

        hyaku = (n - sum) / 100;
        sum += (hyaku * 100);

        gozyu = (n - sum) / 50;
        sum += gozyu * 50;

        zyu = (n-sum) / 10;
        sum += zyu * 10;

        go = (n-sum) / 5;
        sum += go * 5;

        ichi = n - sum;
        sum += ichi;


        uresisa += gohyaku * 1000;
        uresisa += hyaku * 100;
        uresisa += gozyu * 50;
        uresisa += zyu * 10;
        uresisa += go * 5;
        //uresisa += ichi;



        System.out.println(uresisa);
    }
}
