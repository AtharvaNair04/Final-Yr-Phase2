import java.util.*;
class Main
{
    public static void main (String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        long ans = 2;
        for(int i = 0; i < x; i++){
            ans = ans * 2 + 2;
        }
        System.out.println(ans);
    }
}
