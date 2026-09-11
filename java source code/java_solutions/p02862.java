import java.util.*;

public class Main{
  static int mod = 1000000007;
  public static long frac(int n){//n!(mod)の導出

    long ans = 1;
    for(int i=2;i <=n; i++){
      ans *= i;
      ans %= mod;
        }

    return ans;
  }

  public static long modpow(long a,long n, long mod){
    //a^n modの計算
    long res = 1;
    while(n>0){
      if(n%2==1){res = res * a % mod;}
      a = a * a % mod;
      n>>=1;//右に１ビット論理シフト
    }
    return res;
  }

  static long modinv(long a,long mod){
    //a^(-1)modの計算（逆元の計算）
    return modpow(a,mod-2,mod);
  }

  public static void main(String[] args){

    Scanner sc = new Scanner(System.in);

    int x = sc.nextInt();
    int y = sc.nextInt();

    double a = (2*(double)x-(double)y)/3;
    double b = (2*(double)y-(double)x)/3;

    if(((2*x-y)%3==0&&(2*y-x)%3==0)&&(2*x-y>=0&&2*y-x>=0)){
      int aa = (int)a;
      int bb = (int)b;
      int cc = aa + bb;

      //(aa+bb)Caaの組み合わせ解のmodの導出
      long ans1 = frac(aa+bb); //(aa+bb)!のmod
      long ans2 = modinv(frac(aa),mod);
      long ans3 = modinv(frac(bb),mod);
      long ans4 = ((ans1*ans2)%mod*ans3)%mod;
      System.out.println(ans4);




    }else{
      System.out.println("0");
    }

  }
}
