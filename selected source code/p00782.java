import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {
	
	public static class Rec implements Comparable<Rec> {
		double start, end;
		double max, min;
		
		public Rec(double start, double end, double max, double min) {
			super();
			this.start = start;
			this.end = end;
			this.max = max;
			this.min = min;
		}

		@Override
		public int compareTo(Rec arg0) {
			return (this.max - this.min) - (arg0.max - arg0.min) > 0 ? -1 :
				(this.max - this.min) - (arg0.max - arg0.min) < 0 ? 1 : 0;
		}
	}

	public static void main(String[] args) {
		final Scanner sc = new Scanner(System.in);
		
		int count = 1;
		while (true) {
			final int n = sc.nextInt();
			
			if(n == 0){
				break;
			}
			
			ArrayList<Rec> walls = new ArrayList<Rec>(n);
			TreeSet<Double> set = new TreeSet<Double>();
			
			for(int i = 0; i < n; i++){
				final double x = sc.nextDouble();
				final double y = sc.nextDouble();
				final double r = sc.nextDouble();
				
				set.add(x - r);
				set.add(x + r);
				walls.add(new Rec(x - r, x + r, y + r, y - r));
			}
			
			Collections.sort(walls);
			ArrayList<Double> list = new ArrayList<Double>(set);
			
			ArrayList<LinkedList<Double>> min = new ArrayList<LinkedList<Double>>(list.size());
			ArrayList<LinkedList<Double>> max = new ArrayList<LinkedList<Double>>(list.size());
			for(int i = 0; i < list.size(); i++){
				max.add(new LinkedList<Double>());
				min.add(new LinkedList<Double>());
			}
			
			
			for(Rec r : walls){
				
				final int start = Collections.binarySearch(list, r.start);
				final int end = Collections.binarySearch(list, r.end);
				
				for(int i = start; i < end; i++){
					double r_max = r.max;
					double r_min = r.min;
					
					if(max.get(i).size() == 0&& min.get(i).size() == 0){
						max.get(i).add(r_max);
						min.get(i).add(r_min);
					}else{
						ListIterator<Double> max_iter = max.get(i).listIterator();
						ListIterator<Double> min_iter = min.get(i).listIterator();
						
						boolean found = false;
						
						while(max_iter.hasNext() && min_iter.hasNext()){
							double i_max = max_iter.next(), i_min = min_iter.next();
							
							if(i_max >= r_max && i_min <= r_min){
								//System.out.println("a");
								found = true;
								break;
							}else if(i_max < r_max && i_min > r_min){
								max_iter.remove();
								min_iter.remove();
								//max_iter.set(r.max);
								//min_iter.set(r.min);
								//System.out.println("b");
								//found = true;
								//break;
							}else if(i_max < r_max && i_max >= r_min && i_min <= r_min){
								r_min = i_min;
								//max_iter.set(r.max);
								max_iter.remove();
								min_iter.remove();
								//System.out.println("c");
								//found = true;
							}else if(i_min > r_min && i_max >= r_max && i_min <= r_max){
								r_max = i_max;
								//min_iter.set(r.min);
								max_iter.remove();
								min_iter.remove();
								
								//found = true;
								//System.out.println("d");
							}
						}
						
						if(!found){
							//System.out.println("min => " + min.get(i) + " " + r.min);
							//System.out.println("max => " + max.get(i) + " " + r.max);
							
							max.get(i).add(r_max);
							min.get(i).add(r_min);
						}
					}
				}
			}
			
			//for(int i = 0; i < list.size(); i++){
			//	System.out.println(i + "("+ list.get(i) +")" + ": min = " + min.get(i) + " , max = " + max.get(i));
			//}
			
			double sum = 0;
			for(int i = 0; i < list.size() - 1; i++){
				ListIterator<Double> max_iter = max.get(i).listIterator();
				ListIterator<Double> min_iter = min.get(i).listIterator();
				
				while(max_iter.hasNext() && min_iter.hasNext()){
					double i_max = max_iter.next(), i_min = min_iter.next();
					
					sum += (i_max - i_min) * (list.get(i + 1) - list.get(i));
				}
			}
			
			System.out.printf("%d %.2f\n", count, sum);
			count++;
		}
	}

}
