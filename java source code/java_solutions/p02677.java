import java.util.Scanner;

class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int A = sc.nextInt(); //A cm 時針
    int B = sc.nextInt(); //B cm 分針
    double H = sc.nextInt(); //時
    double M = sc.nextInt(); //分
    
    //時針の0時からの傾き
    double Y = 360 * (H / 12)+30*(M/60);
    //分針の0時からの傾き
    double X = 360 * (M / 60);
    //求める角度
    double angle = X - Y;
    //求める長さ
    double C = 0.00000000000000;
    C = A*A + B*B - 2*A*B*Math.cos(Math.toRadians(angle));
    
    //System.out.println(angle);
    System.out.println(Math.sqrt(C));
    
   
  }
}
