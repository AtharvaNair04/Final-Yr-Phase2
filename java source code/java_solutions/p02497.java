import java.util.*;
public class Main {
	 public static void main(String[] args){
		         int i,j;
		         Scanner scan = new Scanner(System.in);
		         while(true){
		         int mid = scan.nextInt();
		         int fin = scan.nextInt();
		         int re = scan.nextInt();
		         int n = fin+mid;
	             if(mid==-1&&fin==-1&&re==-1){
	            	 break;
	             }else if(mid==-1||fin==-1){
	            	 System.out.printf("F\n");
	             }else if(n>=65&&n<80){
	            	 System.out.printf("B\n");
	             }else if(n>=50&&n<65){
	            	 System.out.printf("C\n");
	             }else if(n>=30&&n<50&&re<50){
	            	 System.out.printf("D\n");
	             }else if(n>=30&&n<50&&re>=50){
	            	 System.out.printf("C\n");
	             }else if(mid!=-1&&fin!=-1&&n<30){
	            	 System.out.printf("F\n");
	             }else if(n>=80){
	            	 System.out.printf("A\n");
	             }
		         }
		         
	 }
}
