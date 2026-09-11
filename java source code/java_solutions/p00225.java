import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int n, start, end, ls, le;
    String s;
    int[] words;
    boolean[][] w;
    boolean[] use;

    void run(){
	while(sc.hasNext()){
	    n = sc.nextInt();
	    if(n==0) break;

	    words = new int[26]; // start-end テ」ツ?ョテァツ崢クテ・ツッツセテヲツャツ。テヲツ閉ー
	    w = new boolean[26][26]; // start-end テ」ツ?ョティツセツコテ」ツ?古」ツ?づ」ツつ凝」ツ??
	    use = new boolean[26];

	    for(int i=0; i<n; i++){
		s = sc.next();
		char[] c = s.toCharArray();
		
		// テ」ツつづ」ツ?ョテ」ツ?凖」ツ?氾」ツ?湘ゥツ鳴禿ヲツ??」ツ?」テ」ツ?禿」ツ?療」ツ??」ツ?禿」ツ?ィテ」ツつ津」ツ?療」ツ?ヲテ」ツ??」ツ?淌」ツ?づ、ツスツ陛ヲツ閉?0'ティツカツウテ」ツ?療」ツ?淌」ツ?療」ツ??
		start = c[0]-'a';
		end = c[c.length-1]-'a';

		words[start]++;
		words[end]--;
		w[start][end] = true;
		use[start] = use[end] = true;		
	    }
	 

	    //showW();
	    System.out.println(solve() ? "OK" : "NG");
	}
    }

    boolean solve(){
	/*
	  テァツ崢クテ・ツッツセテヲツャツ。テヲツ閉ーテ」ツ?ョテ・ツ?ヲテァツ青?」ツ?ッテ」ツつウテ」ツつウテ」ツ??
	*/
	for(int i=0; i<26; i++){
	    if(!use[i]) continue;
	    if(words[i]!=0) return false; 
	}

	/*
	  hテ」ツ?ァテ・ツァツ凝」ツ?セテ」ツつ格テ」ツ?ァテァツオツづ」ツつ湘」ツつ凝・ツ債佚ィツェツ榲」ツ?古」ツ?づ」ツつ凝」ツ?凝ッツシツ湘・ツ按・テ」ツ?ョテヲツ鳴?・ツュツ楊テ」ツつ津、ツサツ凝」ツ?療」ツ?ヲhテ・ツァツ凝」ツ?セテ」ツつ格テァツオツづ」ツつ湘」ツつ甘」ツ?ョテ・ツ債佚ィツェツ榲」ツ?ィテ」ツ?ァテ」ツ?催」ツつ凝」ツ??
	  (iテ」ツ?ィjテ」ツ?ッティツ?ェテ・ツ按?」ツ?ァテ」ツつ暗」ツ?湘ゥツ鳴禿ゥツ?陛」ツ?暗」ツつ凝」ツ?ョテ」ツ?ァテ」ツ?）テ」ツ?ッテ、ツスツソテ」ツつ湘」ツ?ェテ」ツ??ヲツ鳴ケテ・ツ青妥」ツ?ァティツ。ツ古」ツ?催」ツ?セテ」ツ??
	*/
	for(int k=0; k<26; k++)
	    for(int h=0; h<26; h++)
		for(int i=0; i<26; i++)
		    w[h][i] = w[h][i] || (w[h][k]&&w[k][i]);
	
	/*
	  テヲツ慊?・ツセツ古」ツ?ォテ・ツ?・テ・ツ環崚」ツ?療」ツ?淌・ツ債佚ィツェツ榲」ツ?ョテヲツ慊?・ツ按敕」ツ?ョテヲツ鳴?・ツュツ耀tartテ」ツ?凝」ツつ嘉、ツスツソテァツ板ィテ」ツ?療」ツ?ヲテ」ツ??」ツつ?
	  テ・ツ?ィテ」ツ?ヲテ」ツ?ョテ」ツつ「テ」ツδォテ」ツδ陛」ツつ。テ」ツδ凖」ツδε」ツδ暗」ツ?ォテ」ツ?淌」ツ?ゥテ」ツつ甘ァツ敖?」ツ?妥」ツつ凝」ツ?凝」ツ??
	  テ、ツスツソテァツ板ィテ」ツ?療」ツ?淌・ツ?ィテ」ツ?ヲテ」ツ?ョテヲツ鳴?・ツュツ療」ツ?凝」ツつ嘉ヲツ慊?・ツセツ古」ツ?ョテヲツ鳴?・ツュツ容ndテ」ツ?クテ」ツ?淌」ツ?ゥテ」ツつ甘ァツ敖?」ツ?妥」ツつ凝」ツ??
	 */
	for(int i=0; i<26; i++)
	    if(use[i]){
		if(!w[start][i]) return false;
		if(!w[i][end]) return false;
	    }
	return true;
    }

    void showWords(){
	for(int i=0; i<26; i++)
	    System.out.print(words[i]+" ");
	System.out.println();
	System.out.println("----------");
    } 

    void showW(){
	for(int i=0; i<26; i++){
	    for(int k=0; k<26; k++)
		System.out.print(w[i][k]+" ");
	    System.out.println();
	}
	System.out.println("----------");
    }
}
