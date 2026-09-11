import java.util.*;
public class Main {
	int n;
	char [] work;
	ArrayList<ArrayList<Character>> list;
	char [] op = {'+', '-','*', '(', ')', '0', '1'};
	boolean isvalid;
	StringBuilder s;
	int pos;
	char end = '#';
	//2040 start
	//2215 WA
	//2220 modi single operation -> WA
	//2235 modi () in exist operation WA
	//2242 modi single operation  -> WA

	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			s = new StringBuilder(sc.next() + "#");
			n = 0;
			isvalid = true;
			ArrayList<Integer> indl = new ArrayList<Integer>();
			for(int i = 0 ; i < s.length(); i++){
				if(s.charAt(i) == '.'){
					n++;
					indl.add(i);
				}
			}

			if(n == 0){
				//評価する
				pos = 0;
				isvalid = true;
				if(! check()) isvalid = false;
				int ans = eval();

				if(! isvalid){
					ans = -1;
				}
				System.out.println(Math.max(ans, -1));
				continue;
			}
			work = new char[n];
			list = new ArrayList<ArrayList<Character>>();
			dfs(0);
			int len = list.size();
			int ans = -1;
			for(int i = 0 ; i < len; i++){
				isvalid = true;
				pos = 0;
				for(int j = 0 ; j < n;j++){
					s.setCharAt(indl.get(j), list.get(i).get(j));
				}
				//System.out.println("i = " + i + " " + s.toString());
				//check
				if(! check()) isvalid = false;

				//評価する
				int now = eval();
				if(! isvalid){
					now = -1;
				}
				ans = Math.max(ans, now);

			}
			System.out.println(ans);
		}
	}

	private boolean isOK(int num){
		if(0<= num && num < 1024){
			return true;
		}
		return false;
	}


	private boolean check() {
		StringBuilder str = new StringBuilder(s.toString());
		LinkedList<Character> stack = new LinkedList<Character>();
		for(int i = 0 ; i < str.length(); i++){
			char c = str.charAt(i);
			if(c == '('){
				stack.addLast(c);
			}
			else if(c == ')'){
				boolean f = false;
				while((! stack.isEmpty()) && (stack.peekLast() != '(')){
					stack.removeLast();
					f = true;
				}
				if(! f) return false;
				if(stack.isEmpty()){
					return false;
				}
				stack.removeLast();
			}
			else if(c == '+' || c == '-' || c == '*'){
				stack.addLast(c);
			}
		}
		return true;
	}


	private int eval() {
		int res = exp();
		if(pos != s.length() - 1){
			isvalid = false;
		}
		return res;
	}


	private int exp() {
		int res = term();
		if(! isvalid) return -1;
		while(true){
			char op = s.charAt(pos);
			if((op == '+') || (op == '-')){
				int old = res;
				pos++;
				res = term();
				if(! isvalid) return -1;
				switch(op){
				case '+':
					res = old + res;
					break;
				case '-':
					res = old - res;
					break;
				}
				if(! isOK(res)){
					isvalid = false; return -1;
				}
			}
			else{
				break;
			}
		}
		return res;
	}

	private int term() {
		if(! isvalid) return -1;
		int res = fact();
		if(! isvalid) return -1;
		while(true){
			char op = s.charAt(pos);
			if((op == '*')){
				int old = res;
				pos++;
				res = fact();
				if(! isvalid) return -1;
				res = old * res;
				if(! isOK(res)){
					isvalid = false; return -1;
				}
			}
			else {
				break;
			}
		}
		return res;
	}


	private int fact() {
		if(! isvalid) return -1;
		if(Character.isDigit(s.charAt(pos))){
			StringBuilder sb = new StringBuilder();
			sb.append(s.charAt(pos++));
			while(Character.isDigit(s.charAt(pos))){
				sb.append(s.charAt(pos++));
			}
			int res = Integer.parseInt(sb.toString(), 2);
			if(! isOK(res)){
				isvalid = false; return -1;
			}
			return res;
		}
		else if(s.charAt(pos) == '('){
			pos++;
			int res = exp();
			if(! isvalid) return -1;
			if(s.charAt(pos) != ')'){
				isvalid = false;
			}
			pos++;
			return res;
		}
		else{
			isvalid = false;
		}
		return 0;
	}


	private void dfs(int deep) {
		if(deep == n){
			ArrayList<Character> a = new ArrayList<Character>();
			for(int i = 0 ; i < n; i++){
				a.add(work[i]);
			}
			list.add(a);
			return ;
		}

		for(int i = 0 ; i < op.length;i++){
			work[deep] = op[i];
			dfs(deep + 1);
		}
	}


	public static void main(String [] args){
		new Main().doit();
	}
}
