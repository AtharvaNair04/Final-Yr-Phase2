import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
 
 
 
 class Main{
      
    static long aa[][];//
    static int n;
    static long max=Long.MIN_VALUE;
    
     public static void main(String args[]){
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         
         try{
             String line;
             line=br.readLine();n=Integer.parseInt(line);
             aa=new long[n][n];//y-x
             for(int i=0; i<n;i++){String spl[] = br.readLine().split(" ");
                for(int j=0;j<n;j++){
                    if(i==0){if(j==0)aa[i][j]=(long)Integer.parseInt(spl[j]);
                        else aa[i][j]=aa[i][j-1]+(long)Integer.parseInt(spl[j]);
                    }else{
                        if(j==0)aa[i][j]=aa[i-1][j]+(long)Integer.parseInt(spl[j]);
                        else aa[i][j]=(long)Integer.parseInt(spl[j])+aa[i-1][j]+aa[i][j-1]-aa[i-1][j-1];
                    }
             }}
             
             /*System.out.println("array");for(long[] ar:aa){
                 for(long k:ar){
                     System.out.print(k+" ");
                 }System.out.println("");
             }*/

            {if(aa[0][0]>max)max=aa[0][0];}if(n==1){System.out.println(max);return;}
            //y==0
            {for(int x=1;x<n;x++){if(aa[0][x]>max)max=aa[0][x];
                for(int wx=0;wx<=x-1;wx++){long cal=aa[0][x]-aa[0][x-wx-1];if(cal>max)max=cal;}
            }}
            {for(int y=1;y<n;y++){if(aa[y][0]>max)max=aa[y][0];
                for(int wy=0;wy<y-1;wy++){long cal=aa[y][0]-aa[y-wy-1][0];if(cal>max)max=cal;}
            }}
             for(int x=1;x<n;x++){
                 for(int y=1;y<n;y++){
                     for(int wX=0;wX<=x;wX++){
                         for(int wY=0;wY<=y;wY++){
                             long cal=0;
                             if(wX==x&&wY==y){cal=aa[y][x];}
                             else if(wX==x){cal=aa[y][x]-aa[y-wY-1][x];}
                             else if(wY==y){cal=aa[y][x]-aa[y][x-wX-1];}
                             else{cal=aa[y][x]-aa[y-wY-1][x]-aa[y][x-wX-1]+aa[y-wY-1][x-wX-1];}
                             if(cal>max)max=cal;
                         }
                     }
                 }
             }
             
             System.out.println(max);
             
         }catch(Exception e){}         
     }
      
    
 }
