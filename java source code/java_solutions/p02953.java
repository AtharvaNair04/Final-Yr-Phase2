import java.util.*;
public class Main{
	public static void main(String sp[]){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		ArrayList<Integer> al = new ArrayList<>();
		for(int i=0;i<n;i++)
			al.add(sc.nextInt());
		
		for(int i=n-1;i>=1;i--){
			if(al.get(i-1)>al.get(i)){
				al.set(i-1,al.get(i-1)-1);
			}
		}
		boolean b=true;
		for(int i=0;i<n-1;i++){
			if(al.get(i+1)<al.get(i)){
				System.out.println("No");
				return;
			}
		}
		System.out.println("Yes");
	}
}
