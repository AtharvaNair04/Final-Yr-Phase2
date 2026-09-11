import java.util.*;

class Main{

    void solve(){
        Scanner sc = new Scanner(System.in);

        while(true){
            int[] set = new int[3];
            set[0] = sc.nextInt();
            set[1] = sc.nextInt();
            set[2] = sc.nextInt();
            set[0] += sc.nextInt();
            set[1] += sc.nextInt();
            set[2] += sc.nextInt();

            if(set[0]==0 && set[1]==0 && set[2]==0) break;

            int max = 0;
            for(int i=0; i<3; i++){
                for(int j=0; j<3; j++){
                    for(int k=0; k<3; k++){
                        int num = (set[0]-i)/3 + (set[1]-j)/3 + (set[2]-k)/3;
                        int min = set[0] - (set[0]-i)/3*3;
                        min = Math.min(min, set[1] - (set[1]-j)/3*3);
                        min = Math.min(min, set[2] - (set[2]-k)/3*3);
                        max = Math.max(max, num + min);
                    }
                }
            }

            System.out.println(max);
        }
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
