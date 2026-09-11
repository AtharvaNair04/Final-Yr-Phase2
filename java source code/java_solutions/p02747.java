import java.util.*;

class Main{
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String[] s = sc.next().split("");
        for(int i=0;i<s.length;i++){
            if(i%2==0){
                if(!s[i].equals("h")){
                    System.out.println("No");
                    return;
                }
            }
            else{
                if(!s[i].equals("i")){
                    System.out.println("No");
                    return;
                }
            }
        }
        if(s.length==1 || s.length%2==1){
            System.out.println("No");   
            return; 
        }
        System.out.println("Yes");
    }
}
