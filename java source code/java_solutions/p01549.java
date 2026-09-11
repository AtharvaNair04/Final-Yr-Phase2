import java.util.*;

public class Main {
	//0127 cording
	//0148 cording end
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			int m = sc.nextInt();
			HashMap<String, HashSet<Integer>> map = new HashMap<String, HashSet<Integer>>();
			for(int i  = 0 ;  i < m; i++){
				String c = sc.next();
				int first = sc.nextInt();
				int end = sc.nextInt();
				HashSet<Integer> set = new HashSet<Integer>();
				for(int j = first; j <= end; j++){
					set.add(j);
				}
				map.put(c, set);
			}
			
			int n = sc.nextInt();
			LinkedList<HashSet<Integer>> stack = new LinkedList<HashSet<Integer>>();
			String [] data = new String[n];
			boolean isC = true;
			for(int i = 0 ; i < n; i++){
				data[i] = sc.next();
			}
			for(int i = 0 ; i < n; i++){
				String s = data[i];
				if(Character.isDigit(s.charAt(0))){
					HashSet<Integer> se = new HashSet<Integer>();
					se.add(Integer.parseInt(s));
					stack.push(se);
				}
				else{
					char c = s.charAt(0);
					if(c =='+' || c == '-' || c =='/' || c=='*'){
						HashSet<Integer> b = stack.pop();
						HashSet<Integer> a = stack.pop();
						HashSet<Integer> r = new HashSet<Integer>();
						if(c == '+'){
							for(int vb: b){
								for(int va: a){
									int res = (vb + va) % 256;
									r.add(res);
								}
							}
						}
						else if(c == '-'){
							for(int vb: b){
								for(int va: a){
									int res = (va - vb + 256) % 256;
									r.add(res);
								}
							}
						}
						else if(c =='*'){
							for(int vb: b){
								for(int va: a){
									int res = (va * vb) % 256;
									r.add(res);
								}
							}
						}
						else{
							if(b.contains(0)){
								isC = false;
								break;
							}
							for(int vb: b){
								for(int va: a){
									int res = (va / vb) % 256;
									r.add(res);
								}
							}
						}
						stack.push(r);
					}
					else{
						stack.push(map.get(s));
					}
				}
			}
			if(isC){
				System.out.println("correct");
			}
			else{
				System.out.println("error");
				
			}
		}
	}
	
	public static void main(String [] args){
		new Main().doit();
	}
}
