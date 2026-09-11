import java.util.*;


class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while(true){
        int n = scanner.nextInt();
        if(n == 0){
            System.exit(0);
        }
        int[] a = new int[n];
        int sub = 1000000000;
        for(int i = 0;i<n;i++){
            a[i] = scanner.nextInt();
        }
        for(int i = 0; i < n-1 ;i++){
            for(int j = i+1; j<n;j++){
                if(sub>Math.abs(a[i]-a[j])){
                    sub = Math.abs(a[i]-a[j]);
                }
            }
        }
        System.out.println(sub);
    }
    }
}
