import java.util.*;

public class Main {
    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt();
        int d = sc.nextInt();
        int x2000 = sc.nextInt();

        int x2001 = r*x2000 - d;
        int x2002 = r*x2001 - d;
        int x2003 = r*x2002 - d;
        int x2004 = r*x2003 - d;
        int x2005 = r*x2004 - d;
        int x2006 = r*x2005 - d;
        int x2007 = r*x2006 - d;
        int x2008 = r*x2007 - d;
        int x2009 = r*x2008 - d;
        int x2010 = r*x2009 - d;
        System.out.println(x2001);
        System.out.println(x2002);
        System.out.println(x2003);
        System.out.println(x2004);
        System.out.println(x2005);
        System.out.println(x2006);
        System.out.println(x2007);
        System.out.println(x2008);
        System.out.println(x2009);
        System.out.println(x2010);
    }
}
