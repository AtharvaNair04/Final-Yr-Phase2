import java.util.*;
public class Main 
{
    public static void main(String[] args) 
    {   
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Integer> S = new ArrayList<Integer>();
        for(int i = 0; i < n; ++i)
        {
            S.add(sc.nextInt());
        }
        int ans = 0;
        int q = sc.nextInt();
        for(int i = 0; i < q; ++i)
        {
            int T = sc.nextInt();
            for(int j = 0; j < n; ++j)
            {
                if(T == S.get(j))
                {
                    ++ans;
                    break;
                }
            }
        }
        System.out.println(ans);
    }
}
