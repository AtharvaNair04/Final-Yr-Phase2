import java.util.Scanner;

public class Main {
	
	public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
       int ar[]=new int[] {0,0,0,0};
       for(int i=0;i<3;i++)
       {
           int a=sc.nextInt();
           int b=sc.nextInt();
           ar[a-1]+=1;
           ar[b-1]+=1;
       }
       
       for(int i = 0; i < 4; i++) {
			if(ar[i] < 1 || ar[i] > 2) {
				System.out.println("NO");
				return;
			}
		}
		
		System.out.println("YES");
       
//       for(int i:ar)
//       {
//           if(i>2)
//           {
//               System.out.println("NO");
//               System.exit(0);
//           }
//       }
//       System.out.println("YES");
    }
}
