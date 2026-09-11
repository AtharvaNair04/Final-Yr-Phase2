import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        String str;
        int num;
        boolean[][] x=new boolean[4][13];
        for(int i=0;i<n;i++){
            str=sc.next();
            num=sc.nextInt();
            if(str.equals("S"))x[0][num-1]=true;
            if(str.equals("H"))x[1][num-1]=true;
            if(str.equals("C"))x[2][num-1]=true;
            if(str.equals("D"))x[3][num-1]=true;
        }
        for(int i=0;i<13;i++)if(!x[0][i])System.out.println("S "+(i+1));
        for(int i=0;i<13;i++)if(!x[1][i])System.out.println("H "+(i+1));
        for(int i=0;i<13;i++)if(!x[2][i])System.out.println("C "+(i+1));
        for(int i=0;i<13;i++)if(!x[3][i])System.out.println("D "+(i+1));
    }
}
