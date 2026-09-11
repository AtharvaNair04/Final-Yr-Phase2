import java.util.Scanner;

class Main{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        for(int i=0;i<n;i++) {
           String[] s=sc.next().split(":");
           double a=Double.parseDouble(s[0])*60;
           double b=Double.parseDouble(s[1]);
           double d=Math.min(Math.abs((a+b)/2-b*6),Math.abs(b*6-(a+b)/2));
           if(d>=180) d=360-d;
           System.out.println(0<=d&&d<30?"alert":90<=d&&d<=180?"safe":"warning");
        }
    }
}
