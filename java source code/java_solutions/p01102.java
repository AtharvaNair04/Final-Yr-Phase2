import java.util.*;
 
class Main {
      
    public static void main(String[] args) {
         
       Scanner cin=new Scanner(System.in);
       
       while(true) {
    	   String str1=cin.next();
    	   if(str1.equals("."))break;
    	   String str2=cin.next();
    	   
    	   String[] stra1=str1.split("\"",-1);
    	   String[] stra2=str2.split("\"",-1);
    	   
    	   int n=stra1.length,jd=0;
    	   
    	   if(n == stra2.length) {
    		   
    		   int s1m,s1lm,s2m,s2lm;
    		   if(str1.indexOf("\"")==0)s1m=1; else s1m=0;
    		   if(str2.indexOf("\"")==0)s2m=1; else s2m=0;
    		   if(str1.lastIndexOf("\"")==str1.length()-1)s1lm=1; else s1lm=0;
    		   if(str2.lastIndexOf("\"")==str2.length()-1)s2lm=1; else s2lm=0;
    		   
    		   if(s1m == s2m && s1lm == s2lm) {
    			   
    			   boolean MODE=false;
    			   /*if(s1m==0)MODE=false; else MODE=true;*/
    			   int same=0;
    			   for(int i=0;i<n;i++) {
    				   //System.out.println(MODE+" "+stra1[i]+" "+stra2[i]);
    				   if(!MODE && !stra1[i].equals(stra2[i])) {
    					   same=0;
    					   //System.out.println("MODE0 INCOLLECT");
    					   break;
    				   }
    				   else if(!MODE && stra1[i].equals(stra2[i]))same++;
    				   else if(MODE && stra1[i].equals(stra2[i]))same++;
    				   MODE = !MODE;
    			   }
    			   
    			   //System.out.println(same);
    			   if(same==n)jd=2;
    			   else if(n>=2 && same==n-1)jd=1;
    			   
    		   }
    		   //else System.out.println("MODE ERROR");
    	   }
    	   
    	   if(jd==2)System.out.println("IDENTICAL");
    	   else if(jd==1)System.out.println("CLOSE");
    	   else System.out.println("DIFFERENT");
       }
       
    }
     
 
}
