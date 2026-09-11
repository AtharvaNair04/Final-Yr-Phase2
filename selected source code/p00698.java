import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int h, w;
    int[][] s;
    int[] ans;
    int next;
    HashMap<Integer, Integer>  Answers;
    int max = Integer.MAX_VALUE;

    void run(){
	int ca = 0;
	while(sc.hasNext()){
	    h = sc.nextInt();
	    if(h!=0){
		if(ca>0)System.out.println();
		put();
		//show();	
		solve();
		ca++;
	    }
	}
    }

    void put(){
	w = sc.nextInt();
	s = new int[h+2][w+2];
	for(int i=1; i<(h+2); i++){
	    for(int k=1; k<(w+2); k++){
		String t = sc.next();
		if(t.equals("?")){
		    s[i][k] = max;
		    s[i][0]++; s[0][k]++;
		    s[0][0]++;
		}
		else s[i][k] = Integer.parseInt(t);
	    }
	    //System.out.println();
	}
	ans = new int[s[0][0]];
	next = 0;
	Answers = new HashMap<Integer, Integer>();
    }

    void solve(){
	boolean con = check();
	boolean did = false;

	while(con && s[0][0]>0){
	    for(int i=1; i<s.length; i++)
		if(s[i][0]==1){
		    int k = 1;
		    while(s[i][k]!=max)k++;
		    calc(i, k, "x"); 
		    did = true;
		}
	    for(int k=1; k<s[h].length; k++)
		if(s[0][k]==1){
		    int i = 1;
		    while(s[i][k]!=max)i++;
		    calc(i, k, "y"); 
		    did = true;
		}
	    con = check();
	    if(!did) con = false;
	}
	Arrays.sort(ans);
	if(s[0][0]==0)
	    for(int i=0; i<ans.length; i++)
		System.out.println(Answers.get(ans[i]));
	else
	    System.out.println("NO");
    }

    void calc(int h, int w, String t){
	int a = 0;
	if(t.equals("y")){
	    for(int i=1; i<s.length; i++){
		if(i==s.length-1) a += s[i][w];
		else if(i!=h) a -= s[i][w];
	    }
	}
	else {
	    for(int k=1; k<s[h].length; k++){
		if(k==s[h].length-1) a += s[h][k];
		else if(k!=w) a -= s[h][k];
	    }
	}
	ans[next++] = (h-1)*s[h].length+w;
	Answers.put((h-1)*s[h].length+w, a);
	s[h][w] = a;
	s[0][w]--; s[h][0]--; s[0][0]--;
	//System.out.println("*"+h+" "+w+" "+t);
    }

    boolean check(){
	int count = 0;
	for(int i=1; i<s.length; i++)
	    if(s[i][0]==1) count++;
	for(int k=1; k<s[h].length; k++)
	    if(s[0][k]==1) count++;
	return count>0 ? true : false;
    }

    void show(){
	for(int i=0; i<s.length; i++){
	    for(int k=0; k<s[i].length; k++)
		System.out.print(s[i][k]+" ");
	    System.out.println();
	}
    }
}
