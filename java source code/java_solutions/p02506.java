/* 
   10022 大文字小文字を区別せずに単語nを探す
   "大文字と小文字を区別しない"から、辞書順が同じなら同じ単語
*/
import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public  static void main(String[] args){
	solve();
    }
    static void solve(){
	String n = kbd.next();
	int ans = 0;
	while(kbd.hasNext()){
	    String s = kbd.next();
	    if(s.equals("END_OF_TEXT")){
		System.out.println(ans);
	    }
	    else{
		if(n.compareToIgnoreCase(s)==0){ // 辞書順が同じ＝等しい単語
		    ans++;
		}
	    }
	}
    }
}
