import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int height[] = new int[n];

        for (int i = 0; i < n; i++)
            height[i]=scanner.nextInt();

        int max=0;
        int heightGreaterCount[] = new int[n];
        heightGreaterCount[n-1] = 0;
        for (int i = n-2; i >= 0; i--) {
            if(height[i]>=height[i+1]) {
                heightGreaterCount[i] = heightGreaterCount[i + 1] + 1;
                max = Math.max(heightGreaterCount[i],max);
            }
        }


        System.out.println(max);
    }
}
