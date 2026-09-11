import java.util.Scanner;

public class Main {
	
	public static int[] substract(int[] vec1, int[] vec2){
		int[] ret = new int[vec1.length];
		
		for(int i = 0; i < vec1.length; i++){
			ret[i] = vec1[i] - vec2[i]; 
		}
		
		return ret;
	}
	
	public static double[] substract(double[] vec1, double[] vec2){
		double[] ret = new double[vec1.length];
		
		for(int i = 0; i < vec1.length; i++){
			ret[i] = vec1[i] - vec2[i];
		}
		
		return ret;
	}
	
	public static double calc_dist(int[] vec){
		double ret = 0;
		for(final int v : vec){
			ret += v * v;
		}
		return Math.sqrt(ret);
	}
	
	public static double calc_dist_2(double[] vec){
		double ret = 0;
		for(final double d : vec){
			ret += d * d;
		}
		return ret;
	}
	
	public static double[] normalize(int[] vec){
		final double dist = calc_dist(vec);
		double[] ret = new double[vec.length];
		for(int i = 0; i < vec.length; i++){
			ret[i] = vec[i] / dist; 
		}
		return ret;
	}
	
	public static double[] add(double[] vec1, double[] vec2){
		double[] ret = new double[vec1.length];
		for(int i = 0; i < vec1.length; i++){
			ret[i] = vec1[i] + vec2[i];
		}
		return ret;
	}
	
	public static double[] multify(double[] vec, double d){
		double[] ret = new double[vec.length];
		for(int i = 0; i < vec.length; i++){
			ret[i] = vec[i] * d;
		}
		return ret;
	}
	
	public static double dot(double[] vec1, double[] vec2){
		double ret = 0;
		for(int i = 0; i < vec1.length; i++){
			ret += vec1[i] * vec2[i];
		}
		return ret;
	}
	
	public static double[] toDouble(int[] vec){
		double[] ret = new double[vec.length];
		for(int i = 0; i < vec.length; i++){
			ret[i] = vec[i];
		}
		return ret;
	}
	
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
       
        final int N = sc.nextInt();
        final int Q = sc.nextInt();
        
        int[][] points = new int[N][];
        int[] rs = new int[N];
        long[] ls = new long[N];
        
        for(int i = 0; i < N; i++){
        	points[i] = new int[]{sc.nextInt(), sc.nextInt(), sc.nextInt()};
        	rs[i] = sc.nextInt();
        	ls[i] = sc.nextLong();
        }
        
        for(int q = 0; q < Q; q++){
        	int[] starts = new int[]{sc.nextInt(), sc.nextInt(), sc.nextInt()};
        	int[] ends = new int[]{sc.nextInt(), sc.nextInt(), sc.nextInt()};
        	
        	final double dist = calc_dist(substract(ends, starts));
        	
        	final double[] double_starts = new double[]{starts[0], starts[1], starts[2]};
        	final double[] double_ends = new double[]{ends[0], ends[1], ends[2]};
        	
        	final double[] normal_diffs = normalize(substract(ends, starts));
        	//System.out.println(normal_diffs[0] + " " + normal_diffs[1] + " " + normal_diffs[2]);
        	
        	long answer = 0;
        	for(int i = 0; i < N; i++){
        		final double[] point = {points[i][0], points[i][1], points[i][2]};
        		
        		final double[] point_diffs = toDouble(substract(points[i], starts));
        		//System.out.println(point_diffs[0] + " " + point_diffs[1] + " " + point_diffs[2]);
        		
        		final double dot = dot(normal_diffs, point_diffs);
        		final double[] nearest_point = dot < 0 ? double_starts : dot >= dist ? double_ends : add(double_starts, multify(normal_diffs, dot(normal_diffs, point_diffs)));
        		
        		final double point_to_seg_2 = calc_dist_2(substract(nearest_point, point));
        		final double point_radius_2 = rs[i] * rs[i];
        		
        		//System.out.println(point_to_seg_2 + " " + point_radius_2);
        		if(point_to_seg_2 <= point_radius_2 + 1e-9){
        			answer += ls[i];
        		}
        	}
        	
        	System.out.println(answer);
        }
        
        
    }
}
