import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	static int plen,cnum,width,cspace;
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			plen=Integer.parseInt(br.readLine());
			if(plen==0){
				break;
			}
			cnum=Integer.parseInt(br.readLine());
			width=Integer.parseInt(br.readLine());
			cspace=Integer.parseInt(br.readLine());
			String space="";
			String empty="";
			for(int i = 0;i<width;i++){
				empty=empty+".";
			}
			for(int i = 0;i<cspace;i++){
				space=space+".";
				
			}
			ArrayList<MyString> list = new ArrayList<MyString>();
			while(true){
				String s = br.readLine();
				if(s==null){
					s="";
				}
				if(s.equals("?")){
					break;
				}
				list.add(new MyString(width,s));
			}
			int cnt=0;
			ArrayList<String> strs=new ArrayList<String>();
			for(int i = 0;i<list.size();i++){
				for(int j = 0;j<list.get(i).list.size();j++){
					strs.add(list.get(i).list.get(j));
				}
				cnt+=list.get(i).list.size();
			}
			int memoNum=((cnt-1)/(cnum*plen))+1;
			cnt=0;
			String[][] ans = new String[memoNum][plen];
			for(int i  =0;i<memoNum;i++){
				for(int j = 0;j<plen;j++){
					ans[i][j]="";
				}
			}
			for(int i  =0;i<memoNum;i++){
				for(int j = 0;j<cnum;j++){
					for(int k = 0;k<plen;k++){
						if(cnt<strs.size()){
							ans[i][k]=ans[i][k]+strs.get(cnt++);
						}
						else{
							ans[i][k]=ans[i][k]+empty;
						}
						if(j!=cnum-1){
							ans[i][k]=ans[i][k]+space;
						}

					}
				}
			}
			String spacespace="";
			for(int i = 0;i<cnum;i++){
				spacespace=spacespace+empty;
			}
			for(int i = 0;i<cnum-1;i++){
				spacespace=spacespace+space;
			}
			for(int i  =0;i<memoNum;i++){
				boolean f=false;
				for(int k = 0;k<plen;k++){
					if(!ans[i][k].equals(spacespace)){
						f=true;
					}
				}
				if(f){
					for(int k = 0;k<plen;k++){
						System.out.println(ans[i][k]);
					}
					System.out.println("#");
				}
			}
			System.out.println("?");
		}
	}
}
class MyString{
	int width;
	ArrayList<String> list = new ArrayList<String>();
	MyString(int w,String s){
		width=w;
		if(s.length()==0){
			for(int i = 0;i<width;i++){
				s=s+".";
			}
			list.add(s);
		}
		else{
			int n=s.length();
			for(int i = 0;i<n;i+=w){
				if(i+w>n){
					String a=s.substring(i,n);
					for(int j = a.length();j<w;j++){
						a=a+".";
					}
					list.add(a);
				}
				else{
					list.add(s.substring(i,i+w));
				}
			}
		}
		for(int i = 0;i<list.size();i++){
			//System.out.println(list.get(i));
		}
	}
}
