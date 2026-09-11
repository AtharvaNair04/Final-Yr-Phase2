import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }

    Scanner kbd = new Scanner(System.in);
    int i, j, l;

    void run(){
	while(kbd.hasNext()){
	    int cnt = 0;
	    int n = kbd.nextInt(); // テ」ツつヲテ」ツつオテ」ツつョテ」ツ?ョテヲツ閉ー
	    boolean[] rab = new boolean[n+1]; // テヲツーツ療・ツ按?」ツつ津ヲツつェテ」ツ?湘」ツ?療」ツ?ヲテ」ツ??」ツ?淌」ツつ映alse, 0テヲツ慊ェテ、ツスツソテァツ板ィ
	    Arrays.fill(rab, true);
	    int k = kbd.nextInt(); // テ」ツδ愿」ツδシテ」ツδ暗」ツ?ョテヲツ閉ー
	    int [][] boat = new int[k][n+1]; // [テ」ツδ愿」ツδシテ」ツδ暗ァツ閉ェテ・ツ渉キ][テ」ツつヲテ」ツつオテ」ツつョテァツ閉ェテ・ツ渉キ]

	    for(i=0; i<k; i++){
		j = kbd.nextInt();
		while(j>0){
		    boat[i][j] = kbd.nextInt();
		    j--;
		}
	    }

	    int r = kbd.nextInt(); // テ、ツサツイテヲツつェテ」ツ??」ツδ堙」ツつ「
	    boolean[][] bad = new boolean[n+1][n+1]; // false テ」ツ?ェテ」ツつ嘉、ツサツイテヲツつェテ」ツ??
	    for(i=0; i<n+1; i++){
		Arrays.fill(bad[i], true);
	    }
	
	    for(i=0; i<r; i++){
		int a = kbd.nextInt();
		int b = kbd.nextInt();
		bad[a][b] = false;
		bad[b][a] = false;
		//System.out.println("*"+a+" "+b);
	    }



	    // テ・ツ崢榲ァツュツ氾」ツ?禿」ツ?禿」ツ?凝」ツつ?
	    for(i=0; i<k; i++){  // テ」ツδ愿」ツδシテ」ツδ暗ァツ閉ェテ・ツ渉キ
		for(j=0; j<n; j++){ // テ」ツつヲテ」ツつオテ」ツつョ1
		    for(l=j+1; l<=n; l++){ // テ」ツつヲテ」ツつオテ」ツつョ2
			int rab1 = boat[i][j];
			int rab2 = boat[i][l];
			if(!bad[rab1][rab2]){
			    if(rab[rab1]){
				rab[rab1] = false;
				cnt++;
			    }
			    if(rab[rab2]){
				rab[rab2] = false;
				cnt++;
			    }
			    //System.out.println(rab1+" "+rab2);
			}
		    }
		}
	    }

	    /*
	    for(i=0; i<rab.length; i++){
		if(!rab[i]){
		    cnt++;
		}
	    }
	    */

	    System.out.println(cnt);
	}
    }
}
