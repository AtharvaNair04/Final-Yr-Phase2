import java.util.*;

public class Main {
	String str;
	int pos;
	int [] data;
	
	private TreeMap<Integer, Integer> fact() {
		char c = str.charAt(pos++);
		TreeMap<Integer, Integer> res = new TreeMap<Integer, Integer>();
		if(Character.isDigit(c)){
			//res.add(c - '0');
			int ind = (c - '0') - 1;
			int num = data[ind];
			//System.out.println("ind= "+(ind + 1));
			res.put(num, 1);
			return res;
		}
		else if(c == '('){
			//System.out.println("pos= " + pos);
			TreeMap<Integer, Integer> right = fact();
			pos++;
			TreeMap<Integer, Integer> left = fact();
			pos++;
			//System.out.println("test1 = " +right.toString() + " " + left.toString());
			//calc
			for(int r: right.keySet()){
				for(int l: left.keySet()){
					for(int i=0; i < 3; i++){
						int res2 = -1;
						switch(i){
						case 0:
							res2 = r & l;
							break;
						case 1:
							res2 = r | l;
							break;
						case 2:
							res2 = r ^ l;
							break;
						}
						if(res2 != -1){
							int sum = right.get(r) * left.get(l);
							if(res.containsKey(res2)){
								int count = res.get(res2) + sum;
								res.put(res2, count);
							}
							else{
								res.put(res2, sum);
							}
						}
					}
				}
			}
			//System.out.println("res= " + res.toString() + " r= " + right.toString() + " l= " + left.toString());
			return res;
		}
		return null;
	}
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			str = sc.nextLine();
			if(str.equals("END")) break;
			str += "#";
			pos = 0;
			int n = sc.nextInt();
			data = new int[n];
			for(int i=0; i < n; i++){
				int num = 0;
				for(int j = 0; j < 4; j++){
					int temp = sc.nextInt();
					num = num * 2 + temp;
				}
				data[i] = num;
			}
			sc.nextLine();
			TreeMap<Integer, Integer> res = new TreeMap<Integer, Integer>();
			res = fact();
			int lastind = res.lastKey();
			//System.out.println(res.toString());
			if(lastind == 15){
				System.out.println(res.get(lastind));
			}
			else{
				System.out.println(0);
			}
		}
	}
	
	public static void main(String[] args) {
		Main obj = new Main();
		obj.doit();
	}
}
