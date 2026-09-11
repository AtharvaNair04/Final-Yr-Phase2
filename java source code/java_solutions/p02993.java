import java.util.Scanner;

 class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        boolean good=true;
        for(int i=1;i<4;i++){
            if(s.charAt(i)==s.charAt(i-1)){
                good=false;
            }
        }
        if(!good)
            System.out.println("Bad");
        else
            System.out.println("Good");
    }
}
