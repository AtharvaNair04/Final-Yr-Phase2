import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

//指定された頭文字からはじまる文字列とそれの出現回数を
//扱う、クラス。sort用に比較も用意。
class Moji implements Comparable<Moji>{
	String str="";
	int count=0;
	Moji(String a,int b){
		str=a;
		count=b;
	}
	@Override
	public int compareTo(Moji o) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.count<o.count){
			return 1;
		}
		else if (this.count>o.count) {
			return -1;
		}
		return this.str.compareTo(o.str);
	}
	
}

/*出現回数順をどうするかわからなかった。
 * 試したこと
 * map
 * set
 * 
 * 結局hashmapとMojiクラスで解決
*/
public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		new Main().run();
	}
	void run(){
		Scanner sc=new Scanner(System.in);
		String str[][];
		char ch;
		int n,count;
		HashMap<String, Integer> hm=new HashMap<String, Integer>();
		Moji m[]=new Moji[200];
		
		while(true){
			n=sc.nextInt();
			if(n==0) break;
			hm.clear();
			count=0;
			str=new String[n][];
			sc.nextLine();
			for(int i=0;i<n;i++){
				str[i]=sc.nextLine().split(" ");
			}
			ch=sc.next().charAt(0);
			for(int i=0;i<n;i++){
				for(int j=0;j<str[i].length;j++){
//					System.out.println(str[i][j]);
					if(ch==str[i][j].charAt(0)){
						if(hm.containsKey(str[i][j])){
							int t=hm.get(str[i][j]);
							m[t].count++;
						}
						else{
							hm.put(str[i][j], count);
							m[count]=new Moji(str[i][j], 1);
							count++;
						}
					}
				}
			}
			
			Arrays.sort(m,0,count);
			if(hm.isEmpty()){
				System.out.print("NA");
			}
			
			else{
				System.out.print(m[0].str);
				for(int i=1;i<5;i++){
					if(i>=count) break;
					System.out.print(" "+m[i].str);
				}
			}
			System.out.println();
		}

	}//run

}
