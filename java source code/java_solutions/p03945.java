import java.util.Scanner;
/**
 * Write a description of class password here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Main
{
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        String s = sc.next();
        s += sc.nextLine();
        char current;
        int count = 0;
        for(int i = 0;i<s.length()-1;i++)
        {
            current = s.charAt(i);
            if(current != s.charAt(i+1))
            {
                count++;
            }
        }
        System.out.println(count);
    }
}
