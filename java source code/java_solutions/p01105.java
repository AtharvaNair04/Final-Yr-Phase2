import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}

	Scanner sc = new Scanner(System.in);

	void run() {

		for (;;) {
			String str = sc.nextLine();
			if (".".equals(str)) {
				break;
			}

			int destination = toCode(str);

			PriorityQueue<E> queue = new PriorityQueue<E>(
					(e1, e2) -> Integer.compare(e1.str.length(), e2.str.length()));

			int MAX = 10000;
			int cost[] = new int[1 << 16];
			Arrays.fill(cost, MAX);

			for(E e:init()){
				queue.add(e);
			}
			cost[toCode("1")] = 1;
			cost[toCode("0")] = 1;
			cost[toCode("a")] = 1;
			cost[toCode("b")] = 1;
			cost[toCode("c")] = 1;
			cost[toCode("d")] = 1;
			if(cost[destination] == 1){
				System.out.println(1);
				continue;
			}
			for (;;) {
				E now = queue.poll();
				if (cost[now.code] < now.str.length()) {
					continue;
				}
				
				if(now.str.length() >= str.length()){
					cost[destination] = str.length();
					break;
				}
				if (now.code == destination) {
					break;
				}

				for (E neighbor : getNeighbors(now)) {
					if (cost[neighbor.code] > neighbor.str.length()) {
						cost[neighbor.code] = neighbor.str.length();
						queue.add(neighbor);
					}
				}
			}

			System.out.println(cost[destination]);
		}

	}

	E[] init(){
		LinkedList<E> list = new LinkedList<E>();
		{
			E e1 = new E();
			e1.str = "a";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "b";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "c";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "d";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		
		return list.toArray(new E[0]);
	}
	E[] getNeighbors(E e) {
		LinkedList<E> list = new LinkedList<E>();
		{
			E e1 = new E();
			e1.str = "-" + e.str;
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"^a)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"^b)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"^c)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"^d)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*a)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*b)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*c)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*d)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*-a)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*-b)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*-c)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		{
			E e1 = new E();
			e1.str = "("+e.str+"*-d)";
			e1.code = toCode(e1.str);
			list.add(e1);
		}
		
		return list.toArray(new E[0]);
	}

	class E {
		String str;
		int code;
	}

	Map<String, Integer> memo = new HashMap<String, Integer>();
	Map<String, Integer> memoFull = new HashMap<String, Integer>();

	int toCode(String s) {
		if(memoFull.containsKey(s)){
			return memoFull.get(s);
		}
		
		int res = 0;
		for (int i = 0; i < 16; i++) {
			res *= 2;

			int[] values = getValues(i);
			int a = values[0];
			int b = values[1];
			int c = values[2];
			int d = values[3];

			List<String> p = new LinkedList<String>();
			
			String str = s;
			str = str.replace('a', (char) (a + '0'));
			str = str.replace('b', (char) (b + '0'));
			str = str.replace('c', (char) (c + '0'));
			str = str.replace('d', (char) (d + '0'));

			if(memo.containsKey(str)){
				res += memo.get(str);
				continue;
			}
			p.add(str);
			

			boolean flg = false;
			for (; str.length() != 1;) {
				str = str.replaceAll("-0", "1");
				str = str.replaceAll("-1", "0");
				str = str.replaceAll("\\(0\\^0\\)", "0");
				str = str.replaceAll("\\(0\\^1\\)", "1");
				str = str.replaceAll("\\(1\\^0\\)", "1");
				str = str.replaceAll("\\(1\\^1\\)", "0");
				str = str.replaceAll("\\(0\\*0\\)", "0");
				str = str.replaceAll("\\(0\\*1\\)", "0");
				str = str.replaceAll("\\(1\\*0\\)", "0");
				str = str.replaceAll("\\(1\\*1\\)", "1");

				if(memo.containsKey(str)){
					res += memo.get(str);
					for(String ss:p){
						memo.put(ss, memo.get(str));
					}
					flg = true;
					break;
				}
				p.add(str);
			}
			if(flg){
				continue;
			}
			for(String ss:p){
				memo.put(ss, str.charAt(0) - '0');
			}
			res += str.charAt(0) - '0';
		}
		memoFull.put(s, res);
		return res;
	}

	int[] getValues(int i) {
		int a = (i & 1);
		int b = (i & 2) >> 1;
		int c = (i & 4) >> 2;
		int d = (i & 8) >> 3;
		return new int[] { a, b, c, d };
	}
}
