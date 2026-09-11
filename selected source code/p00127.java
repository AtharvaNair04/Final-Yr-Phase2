import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] l= "afkpuz:bglqv.:chmrw?:dinsx!:ejoty ".split(":");
        next:while (sc.hasNext()) {
            String m=sc.next();
            if(m.length()%2==1) {
                System.out.println("NA");
                continue;
            }
            String a="";
            for(int i=0;i<m.length();i+=2) {
                int x=m.charAt(i)-'0'-1;
                int y=m.charAt(i+1)-'0'-1;
                if(x<0||x>5||y<0||y>4) {
                    System.out.println("NA");
                    continue next;
                }
                a+=l[y].charAt(x);
            }
            System.out.println(a);
        }
    }
}
