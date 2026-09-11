import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class Main{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		ArrayList<Data> list = new ArrayList<Data>();
		while(true){
			String str=sc.next();
			String[] d=str.split(",");
			int ids=Integer.parseInt(d[0]);
			int anss=Integer.parseInt(d[1]);
			if(ids+anss==0)break;
			list.add(new Data(ids, anss));
		}
		Collections.sort(list);
		while(sc.hasNext()){
			int num=sc.nextInt();
			int cnt=1;
			for(int k=1;k<list.size();k++){
				if(list.get(k).ans!=list.get(k-1).ans){
					cnt++;
				}
				if(list.get(k).id==num)break;
			}		
			if(list.get(0).id==num)cnt=1;
			System.out.println(cnt);
		}
	}
}
class Data implements Comparable<Data>{
	int id;
	int ans;
	public Data(int id, int ans) {
		this.id=id;
		this.ans=ans;
	}
	public int compareTo(Data o) {
		if(this.ans>o.ans)return -1;
		else if(this.ans<o.ans)return 1;
		return 0;
	}
}
