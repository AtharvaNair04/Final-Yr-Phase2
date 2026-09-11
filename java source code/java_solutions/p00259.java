import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static final int SIZE = 100000;
	public static int[] queue = new int[SIZE + 1];
	public static int tp = 0;
	public static int bp = 0;

	public static int next_index(int index) {
		return (index + 1) % SIZE;
	}

	public static int prev_index(int index) {
		return (SIZE + index - 1) % SIZE;
	}

	public static void push(int x) {
		queue[tp] = x;
		tp = next_index(tp);
	}

	public static int pop() {
		int old = bp;
		bp = next_index(bp);
		return queue[old];
	}

	public static int peek() {
		return queue[bp];
	}

	public static void del() {
		bp = next_index(bp);
	}

	public static int size() {
		return tp - bp;
	}

	public static boolean is_empty() {
		return tp == bp;
	}

	public static void clean() {
		tp = bp = 0;
	}

	public static final int ADD = -1;
	public static final int SUB = -2;
	public static final int MUL = -3;
	public static final int DIV = -4;
	public static final int KAKKO_L = -5;
	public static final int KAKKO_R = -6;

	public static int mod_pow(int x, int p, int mod) {
		if (p == 1) {
			return x % mod;
		} else if (p == 0) {
			return x == 1 ? 1 : 0;
		}

		if (p % 2 == 0) {
			int ret = mod_pow(x, p / 2, mod);

			return (ret * ret) % mod;
		} else {
			return (mod_pow(x, p - 1, mod) * x) % mod;
		}
	}

	// +, -
	public static int calc_parse_1(int mod) {
		int left = calc_parse_2(mod);

		if (left < 0) {
			return Integer.MIN_VALUE;
		}
		// System.out.println("t");

		while (!is_empty()) {
			if (!(peek() == ADD) && !(peek() == SUB)) {
				return left;
			}

			int type = pop();
			int right = calc_parse_2(mod);

			if(left < 0 || right < 0){
				return Integer.MIN_VALUE;
				//left = Integer.MIN_VALUE;
			}else if (type == ADD) {
				left = (left + right) % mod;
			} else {
				left = (mod + left - right) % mod;
			}
		}
		return left;
	}

	// *, /
	public static int calc_parse_2(int mod) {
		int left = calc_parse_3(mod);
		
		if (left < 0) {
			return Integer.MIN_VALUE;
		}

		while (!is_empty()) {
			if (!(peek() == MUL) && !(peek() == DIV)) {
				return left;
			}

			int type = pop();
			int right = calc_parse_3(mod);
			
			if(left < 0 || right < 0){
				return Integer.MIN_VALUE;
				//left = Integer.MIN_VALUE;
			}else if(type == MUL) {
				left = (left * right) % mod;
			}else if(right == 0){
				return Integer.MIN_VALUE;
				//left = Integer.MIN_VALUE;
			}else {
				final int inv = mod_pow(right, mod - 2, mod);
				
				if(inv == 0){
					return Integer.MIN_VALUE;
					//left = Integer.MIN_VALUE;
				}else{
					left = (left * inv) % mod;
				}
			}
		}
		return left;
	}

	// primitive, ()
	public static int calc_parse_3(int mod) {
		if (is_empty()) {
			return Integer.MIN_VALUE;
		} else if (peek() >= 0) {
			int number = pop();
			return number;
		} else if (peek() == KAKKO_L) {
			del();
			int expr = calc_parse_1(mod);

			if (peek() == KAKKO_R) {
				del();
				return expr;
			} else {
				return Integer.MIN_VALUE;
			}
		}

		return Integer.MIN_VALUE;
	}
	
	public static void add_stack(String line) {
		clean();
		char[] input = line.toCharArray();

		int count = -1;
		for (char c : input) {
			if ('0' <= c && c <= '9') {
				if (count == -1) {
					count = 0;
				}

				count *= 10;
				count += c - '0';
			} else {
				if (count != -1) {
					push(count);
				}
				count = -1;
			}

			switch (c) {
			case '+':
				push(ADD);
				break;
			case '-':
				push(SUB);
				break;
			case '*':
				push(MUL);
				break;
			case '/':
				push(DIV);
				break;
			case '(':
				push(KAKKO_L);
				break;
			case ')':
				push(KAKKO_R);
				break;
			}

			// System.out.println(stack);
		}

		if (count != -1) {
			push(count);
		}
	}

	public static void main(String[] args) throws IOException {
		// Scanner sc = new Scanner(System.in);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//int count = 0;

		while (true) {
			String input = br.readLine();

			if ("0:".equals(input)) {
				break;
			}
			// count++;

			// if(count > 4){
			// count = 0;
			// System.gc();
			// }
			//System.gc();

			String[] in = input.split(":");
			// String[] tokens = in[1].split(" ");

			// System.out.println(Arrays.toString(tokens));

			// stack.clear();
			/*
			 * for(int i = 0; i < tokens.length; i++){ if(tokens[i].equals("")
			 * || tokens[i].equals(" ")){ continue; }
			 * 
			 * add_stack(tokens[i], stack); }
			 */

			add_stack(in[1]);
			// System.out.println(stack);

			final int mod = Integer.parseInt(in[0]);

			// Expr node = parse_1();

			//System.out.println(disp_parse_1());
			
			int result = calc_parse_1(mod);
			
			if(result < 0){
				System.out.println("NG");
			}else{
				System.out.println(in[1].replaceAll(" ", "") + " = " + result + " (mod " + mod + ")");
			}
			
		}

		br.close();
	}
}
