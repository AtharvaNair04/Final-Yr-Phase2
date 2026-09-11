import java.util.Scanner;

class  Main{
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        while (true){
            int n=scan.nextInt();
            if(n==0){
                break;
            }
            String[] str=new String[n];
            int[] format={5,7,5,7,7};
            int count=0,ans=0;
            for(int i=0;i<n;i++){
                str[i]=scan.next();
            }
            int temp=0,now=0;
            while (true){
                int len=0;
                temp++;
                for(int i=0+now;i<temp+now;i++){
                    len=len+str[i].length();
                }
                if(len>format[count]){
                    count=0;
                    temp=0;
                    now=ans+1;
                    ans++;
                }
                else if(format[count]==len){
                    if(count==0){
                        ans=now;
                    }
                    count++;
                    now=now+temp;
                    temp=0;
                }
                if(count==format.length){
                    break;
                }
            }
            System.out.println(ans+1);
        }
    }
}
