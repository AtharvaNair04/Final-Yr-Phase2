import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		// TODO ?????????????????????????????????????????????
		new Main().start();
	}
	boolean[] b;
	Queue<String> sq = new LinkedList<String>();
	Queue<String> eq = new LinkedList<String>();
	void start(){
		Scanner in = new Scanner(System.in);
		while(true){
			String s = in.next();
			if(s.charAt(0) == '#')break;
			b = new boolean[26];
			count = 0;
			flag = false;
			sq = new LinkedList<String>();
			eq = new LinkedList<String>();
			tansaku(s,"",0);
			if(!flag){
				System.out.println(count);
				print();
			}
			else System.out.println(0);
		}
		in.close();
	}
	int count = 0;
	boolean flag = false;
	void tansaku(String s, String result, int i){
		if(flag){
			return;
		}
		if(s.length() == i){
			count++;
			if(sq.size() >= 5){
				if(eq.size() >= 5){
					eq.poll();
					eq.offer(result);
				}else{
					eq.offer(result);
				}
			}else{
				sq.offer(result);
			}
			return;
		}
		char c = s.charAt(i);
		if(c == 'a'){
			result += c;
			tansaku(s,result,i+1);
			result = trim(result);
			if(!b[1]){
				b[1] = true;	//?????????????????????????????????
				result += (char)(c+1);
				tansaku(s,result,i+1);
				result = trim(result);
				b[1] = false;	//??????????????????????????????
			}
		}
		else if(c == 'z'){
			if(!b[25]){	//z????????????????????????????????????z?????????????????????????????¨?????????
				flag = true;
				return;
			}
			result += c;
			tansaku(s,result,i+1);
		}
		else if(!b[c-'a']){
			if(b[c-'a'+1]){	//
				flag = false;
				return;
			}
			b[c-'a'+1] = true;
			result += (char)(c+1);
			tansaku(s,result,i+1);
			b[c-'a'+1] = false;
		}
		else{
			result += c;
			tansaku(s,result,i+1);
			result = trim(result);

			if(b[c-'a'+1]){	//
				flag = false;
				return;
			}
			b[c-'a'+1] = true;
			result += (char)(c+1);
			tansaku(s,result,i+1);
			result = trim(result);
			b[c-'a' + 1] = false;
		}
	}
	void print(){
		while(!sq.isEmpty()){
			System.out.println(sq.poll());
		}
		while(!eq.isEmpty()){
			System.out.println(eq.poll());
		}
	}
	String trim(String s){
		return s.substring(0,s.length()-1);
	}
	int getCount(int[]a){
		int count = 1;
		for(int aa : a){
			if(aa != 0)count *= kaijou(a[aa]);
		}
		return count;
	}
	int kaijou(int a){
		int b = 1;
		for(int i = 1; i <= a; i++){
			b += i;
		}
		return b;
	}

}
