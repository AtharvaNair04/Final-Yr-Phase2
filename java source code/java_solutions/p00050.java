import java.util.*;
public class Main{
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args){
	new Main().run();
    }

    void run(){
	while(sc.hasNext()){
	    String sen = sc.nextLine();
	    sen = sen.replaceAll("apple", "!!!");
	    sen = sen.replaceAll("peach", "apple");
	    sen = sen.replaceAll("!!!", "peach");
	    System.out.println(sen);
	}
    }
}
