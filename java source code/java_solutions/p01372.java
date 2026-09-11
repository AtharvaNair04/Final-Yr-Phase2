import java.util.*;

@SuppressWarnings("unchecked")
class Main{

    char[] in;
    boolean[][] checked;
    HashSet[][] memo;

    void solve(){
        Scanner sc = new Scanner(System.in);

        while(true){
            String s = sc.next();
            if(s.equals("#")) break;

            in = s.toCharArray();
            int n = in.length+1;
            checked = new boolean[n][n];
            memo = new HashSet[n][n];
            System.out.println(expr(0, in.length).size());
        }
    }

    HashSet<Integer> expr(int l, int r){
        if(checked[l][r]) return memo[l][r];

        boolean kakko_num = true;
        int depth = 0;
        HashSet<Integer> res = new HashSet<Integer>();
        for(int i=l; i<r; i++){
            if(in[i]=='(') depth++;
            if(in[i]==')') depth--;
            if((in[i]=='*' || in[i]=='/' || in[i]=='+' || in[i]=='-') && depth==0){
                kakko_num = false;
                HashSet<Integer> left = expr(l, i);
                HashSet<Integer> right = expr(i+1, r);

                for(Iterator j=left.iterator(); j.hasNext();){
                    int a = (int)(Integer)j.next();
                    for(Iterator k=right.iterator(); k.hasNext();){
                        int b = (int)(Integer)k.next();
                        if(in[i]=='*') res.add(new Integer(a * b));
                        if(in[i]=='/' && b!=0) res.add(new Integer(a / b));
                        if(in[i]=='+') res.add(new Integer(a + b));
                        if(in[i]=='-') res.add(new Integer(a - b));
                    }
                }
            }
        }

        if(kakko_num){
            //digit?
            boolean num = true;
            for(int i=l; i<r; i++) if(!Character.isDigit(in[i])) num = false;
            if(num) res.add(new Integer(digit(l, r)));
            //(gonyo)?
            int depth2 = 0;
            for(int i=l+1; i<r-1; i++){
                if(in[i]=='(') depth2++;
                if(in[i]==')') depth2--;
            }
            if(depth2==0 && in[l]=='(' && in[r-1]==')') res = expr(l+1, r-1);
        }

        checked[l][r] = true;
        return memo[l][r] = res;
    }

    int digit(int l, int r){
        int res = 0;
        int ten = r-l-1;
        for(int i=l; i<r; i++){
            res += (in[i]-'0') * (int)Math.pow(10, ten--);
        }
        return res;
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
