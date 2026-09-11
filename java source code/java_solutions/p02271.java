import java.io.*;
import java.util.*;


//I have to use hashset
public class Main{
    public static void main(String[] args)throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        
        str = br.readLine();
        int n = Integer.parseInt(str);

        str=br.readLine();
        String[] temp = str.split("\\s");
        
        int[] A = new int[temp.length];
        for(int i= 0;i<temp.length;i++){
            A[i] = Integer.parseInt(temp[i]);
        }

        str = br.readLine();
        int q = Integer.parseInt(str);

        str=br.readLine();
        String[] temp2 = str.split("\\s");

        for(int i=0;i<q;i++){
            int m =Integer.parseInt(temp2[i]);
            List<Integer> list = new ArrayList<Integer>();
            if(cal(n,A,list,m)){
                System.out.println("yes");
            }else{
                System.out.println("no");
            }
        }
        
        
       
    }

 
   public static boolean cal(int Len,int[]AA,List<Integer> list,int m){//i,n,A
        int F = AA[0];
        list.add(F);
     
        if(Len == 1){
            if(F == m){
                return true;
            }else{
                int l = list.size();
                
                for(int i=0;i<l-1;i++){
                    if(list.get(i) + list.get(l-1) == m){
                        return true;
                    }
                }

                return false;
            }
        }
    
        int[] temp = new int[AA.length -1];
        
        for(int i=1;i<AA.length;i++){
            temp[i-1] =AA[i];
        }
        
        if(F == m){
            return true;
        }
        
        if(list.size()>1){
            int l = list.size();
            for(int i=0;i<l-1;i++){
                if(list.get(i) + list.get(l-1) == m){
                    return true;
                }else{
                    list.add(list.get(i) + list.get(l-1));
                }
            }
            return cal(temp.length,temp,list,m);
        }else{
            return cal(temp.length,temp,list,m);
        }
   }
}
