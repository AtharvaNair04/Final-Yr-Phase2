import java.util.Scanner;


class Main
{ 
	int st[]; 
	int minVal(int x, int y) { 
		return (x < y) ? x : y; 
	} 

	 
	int getMid(int s, int e) { 
		return s + (e - s) / 2; 
	} 

	
	int RMQUtil(int ss, int se, int qs, int qe, int index) 
	{ 
		 
		if (qs <= ss && qe >= se) 
			return st[index]; 

		
		if (se < qs || ss > qe) 
			return Integer.MAX_VALUE; 

		 
		int mid = getMid(ss, se); 
		return minVal(RMQUtil(ss, mid, qs, qe, 2 * index + 1), 
				RMQUtil(mid + 1, se, qs, qe, 2 * index + 2)); 
	} 

	
	int RMQ(int n, int qs, int qe) 
	{ 
		
		if (qs < 0 || qe > n - 1 || qs > qe) { 
			System.out.println("Invalid Input"); 
			return -1; 
		} 

		return RMQUtil(0, n - 1, qs, qe, 0); 
	} 

	
	int constructSTUtil(int arr[], int ss, int se, int si) 
	{ 
		
		if (ss == se) { 
			st[si] = arr[ss]; 
			return arr[ss]; 
		} 

		
		int mid = getMid(ss, se); 
		st[si] = minVal(constructSTUtil(arr, ss, mid, si * 2 + 1), 
				constructSTUtil(arr, mid + 1, se, si * 2 + 2)); 
		return st[si]; 
	} 

	
	void constructST(int arr[], int n) 
	{ 
		 
		int x = (int) (Math.ceil(Math.log(n) / Math.log(2))); 

		
		int max_size = 2 * (int) Math.pow(2, x) - 1; 
		st = new int[max_size]; 

		 
		constructSTUtil(arr, 0, n - 1, 0); 
	} 

	 
	public static void main(String args[]) 
	{ 
		
		
		Scanner sc = new Scanner(System.in);
		String N = sc.next(); 
		String slide = sc.next();
		Main tree = new Main();
		int arr[] = new int[Integer.parseInt(N)];
		for(int i=0; i<Integer.parseInt(N); i++)
		{
			arr[i] = Integer.parseInt(sc.next());
		}
		
		tree.constructST(arr, Integer.parseInt(N));
		
		for(int i=0;i<=Integer.parseInt(N) - Integer.parseInt(slide);i++) {
			int result=tree.RMQ(Integer.parseInt(N), i, i+Integer.parseInt(slide)-1);
			if(i != (Integer.parseInt(N) - Integer.parseInt(slide))){
					System.out.print(result+" ");
				}else{
					System.out.println(result);
				}
		}
	} 
}
