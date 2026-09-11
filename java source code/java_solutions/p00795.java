import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//Co-occurrence Search
public class Main{

	void run(){
		Scanner sc = new Scanner(System.in);
		boolean head = true;
		while(sc.hasNext()){
			if(!head)sc.nextLine();
			StringBuilder sb = new StringBuilder();
			for(;;){
				String s = sc.nextLine();
				if("".equals(s))break;
				sb.append(s);
			}
			char[] ch = sb.toString().toCharArray();
			int n = ch.length;
			String key = sc.nextLine();
			int k = key.length();
			Map<Character, Integer> ref = new HashMap<Character, Integer>();
			for(int i=0;i<k;i++)ref.put(key.charAt(i), i);
			int[] c = new int[k];
			int kind = 0;
			int s = 0, t = -1;
			int res = 0, min = n+1;
			String ans = "";
			for(;;){
				if(kind==k){
					int len = t-s+1;
					if(len<min){
						min = len;
						res = 1;
						ans = sb.substring(s, t+1);
					}
					else if(len==min)res++;
					if(ref.containsKey(ch[s])){
						int x = ref.get(ch[s]);
						c[x]--;
						if(c[x]==0)kind--;
					}
					s++;
				}
				else{
					t++;
					if(t==n)break;
					if(ref.containsKey(ch[t])){
						int x = ref.get(ch[t]);
						if(c[x]==0)kind++;
						c[x]++;
					}
				}
			}
			System.out.println(res);
			if(res!=0){
				System.out.println();
				for(int i=0;i<ans.length();i+=72)System.out.println(ans.substring(i, Math.min(ans.length(), i+72)));
			}
			System.out.println();
			head = false;
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
