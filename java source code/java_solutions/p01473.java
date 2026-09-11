import java.util.*;
 
class Main{
 
    long[] kaijo;  
    int[] a;
    String s;
    int len;
         
 
    void init(){
    kaijo=new long[21];
    kaijo[0]=1;
    for(int i=1;i<kaijo.length;i++){
        kaijo[i]=kaijo[i-1]*i;
    }
 
    }
 
    void run(){
    init();
    Scanner sc=new Scanner(System.in);
    a=new int[256];
    s=sc.next();
    len=s.length();
    for(int i=0;i<len;i++)a[s.charAt(i)]++;
    System.out.println(count(s));
    }
    long count(String s){
    long res=kaijo[len/2];
    if(len%2==0){
        for(int i=0;i<a.length;i++)if(a[i]%2==1)return 0L;
        for(int i=0;i<a.length;i++){
        if(a[i]>=2)res/=kaijo[a[i]/2];
        }
    }else{
        int k=0;char c='#';
        for(int i=0;i<a.length;i++){
        if(a[i]%2==1){
            c=(char)i;k++;
        }}
        if(k!=1)return 0L;
        a[c]--;
        for(int i=0;i<a.length;i++){
        if(a[i]>=2)res/=kaijo[a[i]/2];
        }
    }
    return res;
    }
 
    public static void main(String[] args){
    new Main().run();
    }
}
