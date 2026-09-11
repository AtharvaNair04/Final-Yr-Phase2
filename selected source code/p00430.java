import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

	public static class Prob implements Comparable<Prob>{
		int[] array;

		public Prob(int[] array){
			this.array = array;
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof Prob){
				Prob ano = (Prob) o;
				
				for(int i = 0; i < array.length; i++){
					if(ano.array[i] != this.array[i]){
						return true;
					}
				}
				
				return false;
			}
			
			return false;
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			
			for(int i = 0; i < array.length; i++){
				if(array[i] == 0){
					break;
				}
				
				sb.append(i == 0 ? "" + array[i] : " " + array[i]);
			}
			
			return sb.toString();
		}
		
		@Override
		public int hashCode(){
			int sum = 0, val = 1;
			
			for(int i = 0; i < array.length; i++ , val *= array.length){
				sum += array[i] * val;
			}
			
			return sum;
		}
		
		@Override
		public int compareTo(Prob arg0) {
			
			for(int i = 0; i < this.array.length; i++){
				if(this.array[i] > arg0.array[i]){
					return -1;
				}else if(this.array[i] < arg0.array[i]){
					return 1;
				}
			}
			
			return 0;
		}
		
		
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			final int n = sc.nextInt();

			if (n == 0) {
				break;
			}
			
			PriorityQueue<Prob> queue = new PriorityQueue<Prob>();
			TreeSet<Prob> tree = new TreeSet<Prob>();
			int[] arr = new int[n];
			arr[0] = n;
			queue.add(new Prob(arr));
			
			while(!queue.isEmpty()){
				Prob pro = queue.poll();
				
				if(tree.contains(pro)){
					continue;
				}else{
					tree.add(pro);
				}
				
				int pre_p = -1;
				for(int i = n - 1; i >= 0; i--){
					if(pro.array[i] <= 1 || pre_p == pro.array[i]){
						continue;
					}
					
					int pre = -1;
					for(int j = i + 1; j < n; j++){
						if(pro.array[j] == pre){
							continue;
						}
						
						if(pro.array[i] - pro.array[j] >= 2){
							int[] tmp = pro.array.clone();
							tmp[i]--;
							tmp[j]++;
							queue.add(new Prob(tmp));
						}
						
						pre = pro.array[j];
					}
					
					pre_p = pro.array[i];
				}
			}
			
			for(Prob p : tree){
				System.out.println(p);
			}
			
		}

	}

}
