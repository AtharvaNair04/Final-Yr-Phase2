import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        String str = sc.next();
        int q = sc.nextInt();

        for(int i = 0; i < q; i++){
            String cmd = sc.next();

            if(cmd.equals("replace")){
                int a = sc.nextInt();
                int b = sc.nextInt();
                String p = sc.next();
                String replaced;
                if(a == 0 && b == str.length() - 1)
                   replaced = p;
                else if(a == 0)
                    replaced = p + str.substring(b + 1, str.length());
                else if(b == str.length() - 1)
                    replaced = str.substring(0, a) + p;
                else
                    replaced = str.substring(0, a) + p + str.substring(b + 1);
                str = replaced;
            }
            else if(cmd.equals("reverse")){
                int a = sc.nextInt();
                int b = sc.nextInt();
                char[] reversed = new char[str.length()];
                for(int j = 0; j < a; j++)
                    reversed[j] = str.charAt(j);
                for(int j = a; j <= b; j++)
                    reversed[j] = str.charAt(b - (j - a));
                for(int j = b + 1; j < str.length(); j++)
                    reversed[j] = str.charAt(j);
                str = String.valueOf(reversed);
            }
            else{
                int a = sc.nextInt();
                int b = sc.nextInt();
                System.out.println(str.substring(a, b + 1));
            }
        }
        sc.close();
    }
}
