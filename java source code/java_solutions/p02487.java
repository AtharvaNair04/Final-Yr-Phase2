import java.util.Scanner;
public class Main {
public static void main(String[] args) {
Scanner sc= new Scanner(System.in);
for(int i = 0; ; i++){
int H = sc.nextInt();
int W = sc.nextInt();
int j;
int k;
if (H == 0 && W == 0) {
break;
}
for(k = 1;k <= H;k++){
/*switch(k){
	case 1:
	for(j=0;j <W;j++){System.out.printf("#");}
	break;
	
	case H:
	for(j=0;j <W;j++){System.out.printf("#");}
	break;
	
	default:
	{System.out.printf("#"); 
		for(j = 0;j<W-2;j++){System.out.printf(".");}
		System.out.printf("#");}*/
if(k==1){for(j=0;j <W;j++){System.out.printf("#");}}
	else if(k==H){for(j=0;j <W;j++){System.out.printf("#");}}
	else{System.out.printf("#"); 
		for(j = 0;j<W-2;j++){System.out.printf(".");}
		System.out.printf("#");}
System.out.println();
}System.out.println();
}
}}
