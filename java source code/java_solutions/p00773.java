import java.util.Scanner;

class Main {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            int x = scan.nextInt();
            int y = scan.nextInt();
            int s = scan.nextInt();
            if (x == 0 && y == 0 && s == 0) {
                break;
            }
            int[] data = {x + 100, y + 100, s};
            System.out.println(doMoney(data));
        }
    }

    private static int doMoney(int[] data) {
        int max = -1;
        for (int i = 1; i < data[2]; i++) {
            for (int j = 1; j < data[2]; j++) {
                if(i*data[0]/100+j*data[0]/100==data[2]){
                    int temp=i*data[1]/100+j*data[1]/100;
                    if (temp>max){
                        max=temp;
                    }
                }
            }
        }
        return max;
    }
}
