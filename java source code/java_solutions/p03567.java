import java.util.*;

public class Main {
    public static boolean find(String s){
        for (int i=0;i<s.length()-1;i++){
            if (s.substring(i, i + 1).equals("A") && s.substring(i+1,i+2).equals("C")){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner re=new Scanner(System.in);
        String str=re.nextLine();
        if(find(str))
            System.out.println("Yes");
        else
            System.out.println("No");
    }
}
