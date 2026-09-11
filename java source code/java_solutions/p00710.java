import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int r = sc.nextInt();

        while(n != 0 || r != 0) {

            int[] card = new int[n + 1];

            card[0] = 0;

            for (int i = n; i > 0; i--)
                card[i] = i;

            for (int i = 0; i < r; i++) {
                int[] suffledCard = new int[n + 1];
                for (int l = n; l > 0; l--)
                    suffledCard[l] = l;
                suffledCard[0] = 0;

                int p = sc.nextInt();
                int c = sc.nextInt();
                int k = n;

                for (int j = n - (p - 1); j > n - (c + p - 1); j--)
                    suffledCard[k--] = card[j];
                for (int j = n; j > n - (p - 1); j--)
                    suffledCard[k--] = card[j];
                for (int j = n - (c + p - 1); j > 0; j--)
                    suffledCard[k--] = card[j];

                card = suffledCard;
            }

            System.out.println(card[n]);

            n = sc.nextInt();
            r = sc.nextInt();
        }

        sc.close();
    }
}
