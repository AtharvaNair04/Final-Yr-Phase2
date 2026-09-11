import java.io.*;
import java.util.*;


public class Main{

   
    public static void main(String[] args) {
    FastReader sc=new FastReader();
    int N=sc.nextInt();
    int[] a=new int[N];
    double sum=0;
    for(int i=0;i<N;i++){
        a[i]=sc.nextInt();
        sum+=a[i];    
    }
  int avg=(int)Math.round(sum/N);
 int mini=0;
 for(int i=0;i<N;i++){
 mini+=Math.pow(a[i]-avg,2);
 }
        System.out.print(mini);
    
    
    }
    
}



class FastReader{
    BufferedReader br ;
    StringTokenizer st;
public FastReader(){
    InputStreamReader inr =new InputStreamReader(System.in);
    br=new BufferedReader(inr);

}

String next(){
    while(st==null || !st.hasMoreElements()){
    try{
    st=new StringTokenizer(br.readLine());
    }
    catch(IOException e){
        e.printStackTrace();
    }}
    return st.nextToken();
}

int nextInt(){
    return Integer.parseInt(next());
}

double nextDouble(){
    return Double.parseDouble(next());

}

long nextLong(){
    return Long.parseLong(next());
}


String nextLine(){
    String str="";
    try{
        str=br.readLine();
    }
    catch(IOException e)
    {
        e.printStackTrace();
    }
    return str;
}
}
