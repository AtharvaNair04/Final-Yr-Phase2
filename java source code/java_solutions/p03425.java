import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long[] name = new long[5];
        for(int i = 0; i < n; i++){
            String str = scanner.next();
            char f = str.charAt(0);
            if(f == 'M'){name[0]++;}
            else if(f == 'A'){name[1]++;}
            else if(f == 'R'){name[2]++;}
            else if(f == 'C'){name[3]++;}
            else if(f == 'H'){name[4]++;}
        }
        long count = 0;
        for(int i = 0; i < 5; i++){
            if(name[i] != 0){count++;}
        }
        if(count < 2){System.out.println(0);return;}
        long sum = 0;
        for(int i = 0; i < 3; i++){
            for(int j = i+1; j < 4; j++){
                for(int k = j+1; k < 5; k++){
                    if(name[i] != 0 && name[j] != 0 && name[k] != 0){
                        sum += name[i] * name[j] * name[k];
                    }  
                }
            }
        }
        System.out.println(sum);
    }
}
