import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int[] cards;
    int n, m;

    void run(){
	//while(sc.hasNext()){
	    n = sc.nextInt();
	    cards = new int[2*n];
	    for(int i=0; i<cards.length; i++)
		cards[i] = i+1;
	    m = sc.nextInt();

	    for(int i=0; i<m; i++){
		//show();
		int k = sc.nextInt();
		if(k==0) rif();
		else cut(k);
	    }

	    for(int i=0; i<cards.length; i++)
		System.out.println(cards[i]);
	    //}
    }

    void rif(){
	int[] s = new int[2*n];
	for(int i=0; i<2*n; i++)
	    s[i] = cards[i];

	int a = 0;
	for(int i=0; i<2*n; i+=2){
	    cards[i] = s[a];
	    a++;
	}
	for(int i=1; i<2*n; i+=2){
	    cards[i] = s[a];
	    a++;
	}
    }

    void cut(int k){
	int[] s = new int[2*n];
	for(int i=0; i<2*n; i++)
	    s[i] = cards[i];

	int a = k;
	for(int i=0; i<2*n; i++){
	    cards[i] = s[a];
	    a++;
	    if(a==2*n) a = 0;
	}
    }

    void show(){
	for(int i=0; i<cards.length; i++)
	    System.out.println(cards[i]);
	System.out.println("------------");
    }
}
