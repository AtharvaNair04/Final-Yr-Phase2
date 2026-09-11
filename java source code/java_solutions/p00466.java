/*
  テヲツィツ凖ヲツコツ姪・ツ?・テ・ツ?コテ・ツ環崚」ツつ津ィツ。ツ古」ツ??」ツδ療」ツδュテ」ツつーテ」ツδゥテ」ツδ?」ツつ津、ツスツ愿ヲツ按静」ツ?療」ツ?ヲテ、ツクツ凝」ツ?陛」ツ??)
 */

import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);
    int n;

    void run(){
	while(sc.hasNext()){
	    n = sc.nextInt();
	    if(n!=0)
		solve();
	}
    }

    void solve(){
	for(int i=0; i<9; i++){
	    n -= sc.nextInt();
	}
	System.out.println(n);
    }
}
