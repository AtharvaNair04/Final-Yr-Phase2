import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    if(n!=0){
		solve(n);
	    }
	}
    }

    static void solve(int n){
        String[][] group = new String[n][1050];
	visit = new boolean[n];
	int i, j, k=0, g=0;
	String a, p;
	while(g<n){
	    String len = kbd.next();
	    p = "";
	    for(i=0; i<len.length(); i++){
		a = len.substring(i, i+1);
		if(a.equals(":") || a.equals(",") || a.equals(".")){
		    group[g][k++] = p;
		    p = "";
		}
		else {
		    p += a;
		}
	    }
	    k=0;
	    g++;
	}

	int cnt = 0;
	String[] men = new String[1050];
	boolean TF = true;
	boolean[] x = new boolean[n];
	Arrays.fill(x, false);
	x[0] = true;
    

	cGroup(x, group, n, 0);

    

	for(g=0; g<n; g++){
	    for(i=1; group[g][i] != null; i++){
		TF = true;
		p = group[g][i];
		//System.out.println(p);

		// テ」ツつーテ」ツδォテ」ツδシテ」ツδ療・ツ青催」ツ?ィテァツュツ嘉」ツ?療」ツ?妥」ツつ古」ツ?ーテゥツ」ツ崚」ツ?ーテ」ツ??
		for(j=0; j<n; j++){
		    if(x[g] && group[j][0].equals(p)){
			//System.out.println(p);
			TF = false;
		    }
		}

		// テヲツ猟「テ」ツ?ォテヲツ閉ーテ」ツ?暗」ツ?淌」ツ?禿」ツ?ィテ」ツ?ョテ」ツ?づ」ツつ凝・ツ青催・ツ可催」ツ??」ツ?」テ」ツ?淌」ツつ嘉ゥツ」ツ崚」ツ?ーテ」ツ??
		for(j=0; men[j]!=null; j++){
		    if(men[j].equals(p)){
			//System.out.println(p);
			TF = false;
		    }
		}

		// テヲツ閉ーテ」ツ?暗」ツ?ヲテ」ツ??」ツ??」ツつーテ」ツδォテ」ツδシテ」ツδ療」ツ?ァテ」ツ??」ツ?凝」ツ?、テゥツ」ツ崚」ツ?ーテ」ツ?陛」ツつ古」ツ?ヲテ」ツ??」ツ?ェテ」ツ?妥」ツつ古」ツ?ー
		if(TF && x[g]){
		    //System.out.println(p);
		    men[cnt] = p;
		    cnt++;
		}
	    }
	}

	System.out.println(cnt);
    }



    static void cGroup(boolean[] x, String[][] group, int n, int m){
	int i, j;
	for(i=1; group[m][i] != null; i++){
	    String p = group[m][i];
	    //System.out.println(p);
	    
	    //テ」ツ?禿」ツ?ョテ」ツつーテ」ツδォテ」ツδシテ」ツδ療」ツ?ッテヲツ閉ーテ」ツ?暗」ツ?ヲティツ可ッテ」ツ??」ツ?凝ッツシツ?
		for(j=0; j<n; j++){
		    if(x[m] && !x[j] && group[j][0].equals(p)){
			//System.out.println(p);
			x[j] = true;
			cGroup(x, group, n, j);
		    }
		}
	}
    }

    static boolean[] visit;
}
