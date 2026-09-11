import java.util.Scanner;

class Main{
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            int n = scan.nextInt();
            if (n == 0) {
                break;
            }
            System.out.println(hurui(n));
        }
    }

    private static int hurui(int num) {
        int[] data = new int[num * 2+1];
        for(int i=1;i<=num*2;i++){
            data[i]=i;
        }
        for(int i=2;i<=Math.sqrt(data.length-1);i++){
            if(data[i]==i){
                for(int j=i+1;j<data.length;j++){
                    if (j%i==0){
                        data[j]=0;
                    }
                }
            }
        }
        int count=0;
        for(int i=num+1;i<data.length;i++){
            if(data[i]==i){
                count++;
            }
        }
        return count;
    }
}
