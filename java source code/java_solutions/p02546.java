import java.util.Scanner;

public class Main {
    void run(String[] args){
        Scanner scan=new Scanner(System.in);
        String line=scan.nextLine();
        String str;
        String[] lines=line.split("");
        if(lines[lines.length-1].equals("s")){
            str=line+"es";
        }
        else{
            str=line+"s";
        }
        System.out.println(str);
    }
    public static void main(String args[]){
        Main printer = new Main();
        printer.run(args);
    }
}
