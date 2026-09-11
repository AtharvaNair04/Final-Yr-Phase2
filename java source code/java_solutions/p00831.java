import java.util.*;

public class Main{
	
	//0000 start
	int n;
	String [] data;
	ArrayList<HashSet<String>> data2;
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			n= sc.nextInt();
			if(n == 0) break;
			int d = sc.nextInt();
			data = new String[n];
			HashSet<String> set = new HashSet<String>();
			for(int i = 0; i < n; i++){
				data[i] = sc.next();
				set.add(data[i]);
			}
			Arrays.sort(data);
			if(d == 0){
				System.out.println(0);
				continue;
			}
			
			data2 = new ArrayList<HashSet<String>>();
			for(int i = 0; i < n; i++){
				data2.add(new HashSet<String>());
				data2.get(i).add(data[i]);
				solve(i);
			}
			int count = 0;
			if(d == 1){
				for(int i = 0; i < n; i++){
					for(int j = i + 1; j < n; j++){
						if(data2.get(i).contains(data[j]) || data2.get(j).contains(data[i])){
							System.out.println(data[i] + "," + data[j]);
							count++;
						}
					}
				}
			}
			else{
				for(int i = 0; i < n; i++){
					for(int j = i + 1; j < n; j++){
						for(String k : data2.get(i)){
							if(data2.get(j).contains(k)){
								System.out.println(data[i] + ","+data[j]);
								count++;
								break;
							}
						}
					}
				}
			}
			System.out.println(count);
		}
	}

	private void solve(int ind) {
		StringBuilder sb = new StringBuilder(data[ind]);
		int len = sb.length();
		//delete
		for(int i = 0; i < len; i++){
			char c = sb.charAt(i);
			sb.deleteCharAt(i);
			data2.get(ind).add(sb.toString());
			sb.insert(i, c);
		}
		
		//insert
		for(int i = 0; i <= len; i++){
			for(int j = 'a'; j <= 'z'; j++){
				sb.insert(i, (char)j);
				data2.get(ind).add(sb.toString());
				sb.deleteCharAt(i);
			}
		}
		//replace
		for(int i = 0; i < len; i++){
			for(int j = 'a'; j <= 'z'; j++){
				char c = sb.charAt(i);
				sb.setCharAt(i, (char)j);
				data2.get(ind).add(sb.toString());
				sb.setCharAt(i, c);
			}
		}
		
		//swap
		for(int i = 1; i < len; i++){
			swap(sb, i-1, i);
			data2.get(ind).add(sb.toString());
			swap(sb, i-1, i);
		}
	}

	private void swap(StringBuilder sb, int i, int j) {
		char c = sb.charAt(i);
		sb.setCharAt(i, sb.charAt(j));
		sb.setCharAt(j, c);
	}

	public static void main(String[] args) {
		new Main().doit();
	}
}
