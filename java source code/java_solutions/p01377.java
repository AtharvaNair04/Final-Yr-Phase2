import java.util.*;

public class Main{

    int cnt;

    void solve(){
        Scanner sc = new Scanner(System.in);

        String line = sc.next();
        char[] c = line.toCharArray();
        cnt = 0;
        for(int i=0; i<=c.length/2; i++){
            if(c[i]=='(' && c[c.length-i-1]!=')'){
                cnt++;
                c[c.length-i-1] = ')';
                continue;
            }
            if(c[i]==')' && c[c.length-i-1]!='('){
                cnt++;
                c[c.length-i-1] = '(';
                continue;
            }
            if(c[i]=='w' && c[c.length-i-1]!='w'){
                cnt++;
                c[c.length-i-1] = 'w';
                continue;
            }
            if(c[i]=='i' && c[c.length-i-1]!='i'){
                cnt++;
                c[c.length-i-1] = 'i';
                continue;
            }
        }
        System.out.println(cnt);
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
