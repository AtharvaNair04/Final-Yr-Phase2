import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int n, k;
    int[] card;
    boolean Joker;

    void run(){
	while(sc.hasNext()){
	    n = sc.nextInt();
	    k = sc.nextInt();
	    if(n==0 && k==0) break;

	    card = new int[k];
	    
	    for(int i=0; i<k; i++)
		card[i] = sc.nextInt();
	    Arrays.sort(card);
	    Joker = card[0]==0;
	    //show();
	    
	    int R = k+1;
	    int L = Joker ? 2:1; 
	    int m = (R+L)/2;
	    while(L+1 != R){
		//System.out.println("*"+L+" "+R+" "+m);
		//System.out.println(m+" "+step(m));
		if(step(m)) L = m;
		else R = m;
		m = (R+L)/2;
	    }

	    System.out.println(L);
	}
    }

    boolean step(int m){
	int s = Joker ? 1:0;
	for(int i=s; i<k; i++){
	    //System.out.println("++"+card[i]);
	    int endN = i+m-1; // 最後のカードの配列番号
	    int endC = card[i]+m-1; //最後のカードにあるべき値
	    if(endC>n || endN>k) continue;
	    
	    if(endN==k)
		if(card[k-1]==endC && Joker) return true;
		else return false;
		
	    if(card[endN]==endC ||
	       (card[endN-1]==endC && Joker))
		return true;
	}
	return false;
    }

    void show(){
	for(int i=0; i<k; i++)
	    System.out.print(card[i]+" ");
	System.out.println();
    }
}
