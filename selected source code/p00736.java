import java.util.Arrays; 
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	
	public static interface Fomula{
		int eval(int[] input);
	}
	
	public static class Minus implements Fomula{
		Fomula inner;
		
		public Minus(Fomula in){
			inner = in;
		}

		@Override
		public int eval(int[] input) {
			return 2 - inner.eval(input);
		}
		
		public String toString(){
			return "-" + inner;
		}
	}
	
	public static class Add implements Fomula{
		Fomula left, right;
		
		public Add(Fomula left, Fomula right){
			this.left = left;
			this.right = right;
		}

		@Override
		public int eval(int[] input) {
			return Math.max(left.eval(input), right.eval(input));
		}
		
		public String toString(){
			return "(" + left + "+" + right + ")";
		}
	}
	
	public static class Mul implements Fomula{
		Fomula left, right;
		
		public Mul(Fomula left, Fomula right){
			this.left = left;
			this.right = right;
		}

		@Override
		public int eval(int[] input) {
			return Math.min(left.eval(input), right.eval(input));
		}
		
		public String toString(){
			return "(" + left + "*" + right + ")";
		}
	}
	
	public static class Const implements Fomula{
		int con;
		
		public Const(int con){
			this.con = con;
		}

		@Override
		public int eval(int[] input) {
			return con;
		}
		
		public String toString(){
			return "" + con;
		}
	}
	
	public static class Var implements Fomula{
		int var;
		
		public Var(int v){
			var = v;
		}

		@Override
		public int eval(int[] input) {
			return input[var];
		}
		
		public String toString(){
			return (char)(var + 'P')+"";
		}
	}
	
	public static Fomula parse(char[] input, int start, int end){
		int k_lv = 0;
		int k_s = -1, k_e = -1;
		int m_s = -1;
		int a_p = -1, m_p = -1;
		
		for(int pos = start; pos <= end; pos++){
			if(input[pos] == '('){
				if(k_lv == 0){
					k_s = pos;
				}
				k_lv++;
			}else if(input[pos] == ')'){
				k_lv--;
				if(k_lv == 0){
					k_e = pos;
					break;
				}
			}else if(input[pos] == '-' && k_lv == 0 && m_s == -1){
				m_s = pos;
				break;
			}else if(input[pos] == '*' && k_lv == 1){
				m_p = pos;
			}else if(input[pos] == '+' && k_lv == 1){
				a_p = pos;
			}			
		}
		
		if(k_s != -1 && k_e != -1){
			if(m_p != -1){
				return new Mul(parse(input, k_s + 1, m_p - 1), parse(input, m_p + 1, k_e - 1));
			}else{
				return new Add(parse(input, k_s + 1, a_p - 1), parse(input, a_p + 1, k_e - 1));
			}
		}else if(m_s != -1){
			return new Minus(parse(input, m_s + 1, end));
		}else if('0' <= input[start] && input[start] <= '2'){
			return new Const(input[start] - '0');
		}else {
			return new Var(input[start] - 'P');
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			String str = sc.next();
			
			if(".".equals(str)){
				break;
			}
			
			char[] input = str.toCharArray();
			
			Fomula fomula = parse(input, 0, input.length - 1);
			
			//System.out.println(fomula);
			
			int count = 0;
			int[] array = new int[3];
			for(int p = 0; p <= 2; p++){
				array[0] = p;
				
				for(int q = 0; q <= 2; q++){
					array[1] = q;
					
					for(int r = 0; r <= 2; r++){
						array[2] = r;
						
						if(fomula.eval(array) == 2){
							count++;
						}
					}
				}
			}
			
			System.out.println(count);
		}
		
	}
}
