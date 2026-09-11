import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;



 class Main{
     public static void main(String args[]){
         Scanner sc = new Scanner(System.in);
         String line;
         while(sc.hasNext()){
             line=sc.nextLine();
             if(line.isEmpty())break;
             String[] split=line.split(",");
             int[] ints = new int[5]; for(int i=0;i<5;i++)ints[i]=Integer.parseInt(split[i]);
             if(!_4c(ints)){//System.out.println("aa");
                 if(!fullH(ints)){//System.out.println("bb");
                     if(!st(ints)){//System.out.println("cc");
                         if(!_3c(ints)){//System.out.println("dd");
                             if(!_2p(ints)){//System.out.println("dd");
                                 //System.out.println(_1p(ints));
                                 if(!_1p(ints)){//System.out.println("ee");
                 System.out.println("null");}}}}}}
         }
         
         
     }
     static boolean _4c(int[] ints){
         if(ints[1]==ints[2]&&ints[1]==ints[3]&&ints[1]==ints[4]){System.out.println("four card");
         return true;}
         int count=0; for(int x : ints) if(x==ints[0])count++;
         if(count==4){System.out.println("four card");return true;}
         return false;
     }
     static boolean fullH(int[] ints){
         HashSet<Integer>hs=new HashSet<Integer>();
         for(int x:ints) hs.add(x);
         if(hs.size()==2){System.out.println("full house"); return true;}
         return false;
     }
     static boolean st(int[] ints){
         TreeSet<Integer>hs=new TreeSet<Integer>();
         
         for(int x:ints) hs.add(x);
         if(hs.size()!=5)return false;
         if(hs.last()-hs.first()==4){System.out.println("straight");return true;}
         if(hs.first()==1){hs.remove(1);
             if(hs.first()==10){System.out.println("straight");return true;}
        }return false;
     }
     static boolean _3c(int[] ints){
        for(int i=0; i<5;i++){
            int count=0;
            for(int j=i+1;j<5;j++){
                if(ints[i]==ints[j])count++;
            }
            if(count==2){System.out.println("three card");return true;}
        }
        return false;
     }
     static boolean _2p(int[] ints){
         TreeMap<Integer,Integer>tm=new TreeMap<Integer,Integer>();
         for(int in:ints){
             if(tm.containsKey(in))tm.put(in, tm.get(in)+1);
             else tm.put(in, 1);
         }
         if(tm.keySet().size()==3){System.out.println("two pair");return true;}
         return false;
     }
     static boolean _1p(int[] ints){
         for(int i=0; i<5;i++){
             for(int j=i+1;j<5;j++){
                 if(ints[i]==ints[j]){System.out.println("one pair");return true;}
         }
        }return false;
     }
 }
