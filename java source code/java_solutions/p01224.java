import java.util.*;

class Main{
        public static void main(String[] args){
                Scanner sc = new Scanner(System.in);
                String[] outs = {"deficient number", "perfect number", "abundant number"};
                int n = sc.nextInt();
                //ツ篠ゥツ身ツづーツ甘慊づ楪姪アツ青板づ個和
                while(n != 0){ 
                        n = n-getS(n);
                        String out = ""; 
                        if(n < 0){ 
                                out = outs[2];
                        }else if(n == 0){ 
                                out = outs[1];
                        }else{
                                out = outs[0];
                        }   
                        System.out.println(out);
                        n = sc.nextInt();
                }   
        }   
        static int getS(int n){
                int sum = 0;
                for(int i = 1 ; i*i <= n ; i++){
                        if(n%i==0){
                                sum += i;
                                if(i*i != n){ 
                                        sum += n/i;
                                }   
                        }     
                }   
                sum -= n;
                return sum;
        }   
}
