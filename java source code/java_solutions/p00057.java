import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);
    int[] lines = new int[10001];

    void run(){
	part();
	while(sc.hasNext()){
	    int n = sc.nextInt();
	    System.out.println(lines[n]);
	}
    }

    void part(){
	lines[0] = 1;
	lines[1] = 2;
	lines[2] = 4;
	lines[3] = 7;
	for(int i=4; i<lines.length; i++){
	    lines[i] = lines[i-1]+i;
	}
    }
}
