import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        char[] line1 = line.toCharArray();
        if(line1[0] == line1[1]){
            if(line1[2] != line1[1]){
                if(line1[2] == line1[3])
                System.out.println("Yes");
                else
                System.out.println("No");
            }
            else
            System.out.println("No");
        }
        else if(line1[0] == line1[2]){
            if(line1[0] != line1[1]){
                if(line1[1] == line1[3])
                System.out.println("Yes");
                else
                System.out.println("No");
            }
            else
            System.out.println("No");
        }
        else if(line1[0] == line1[3]){
            if(line1[0] != line1[1]){
                if(line1[1] == line1[2])
                System.out.println("Yes");
                else
                System.out.println("No");
            }
            else
            System.out.println("No");
        }
        else if(line1[1] == line1[2]){
            if(line1[0] != line1[1]){
                if(line1[0] == line1[3])
                System.out.println("Yes");
                else
                System.out.println("No");
            }
            else
            System.out.println("No");
        }
        else if(line1[1] == line1[3]){
            if(line1[0] != line1[1]){
                if(line1[0] == line1[2])
                System.out.println("Yes");
                else
                System.out.println("No");
            }
            else
            System.out.println("No");
        }
        else if(line1[2] == line1[3]){
            if(line1[1] != line1[2]){
                if(line1[0] == line1[1])
                System.out.println("Yes");
                else
                System.out.println("No");
            }
            else
            System.out.println("No");
        }
        else
        System.out.println("No");
    }
}
