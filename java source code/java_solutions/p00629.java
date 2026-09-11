import java.util.Arrays;
import java.util.Scanner;

class Team{
	int id;
	int af;
	int solved;
	int pena;
	Team(int nid,int naf,int ns,int np){
		this.id=nid;
		this.af=naf;
		this.solved=ns;
		this.pena=np;
	}
}

public class Main {
	
	Team[] sort(Team t[],int n){
		int max;
		for(int i=0;i<n;i++){
			max=i;
			for(int j=i;j<n;j++){
				if(t[max].solved<t[j].solved){
					max=j;
				}
				else if(t[max].solved==t[j].solved){
					if(t[j].pena<t[max].pena){
						max=j;
					}
					else if(t[j].pena==t[max].pena){
						if(t[j].id<t[max].id)
							max=j;
					}
				}
			}
			Team temp=t[i];
			t[i]=t[max];
			t[max]=temp;
		}
		return t;
	}
	void select(Team[] t,int n){
		int count=0;
		int p[]=new int[1001];
		Arrays.fill(p, 0);
		Team s[]=new Team[26];
		for(int i=0;i<n;i++){
			if(count<10){
				if(p[t[i].af]<3){
					s[count]=t[i];
					count++;
					p[t[i].af]++;
				}
			}
			else if(count<20){
				if(p[t[i].af]<2){
					s[count]=t[i];
					count++;
					p[t[i].af]++;
				}
			}
			else if(count<26){
				if(p[t[i].af]<1){
					s[count]=t[i];
					count++;
					p[t[i].af]++;
				}
			}
		}
		for(int i=0;i<count;i++){
			System.out.println(s[i].id);
		}
	}
	void run(){
		Scanner sc=new Scanner(System.in);
		int n;
		while(true){
			n=sc.nextInt();
			if(n==0) break;
			Team t[]=new Team[n];
			for(int i=0;i<n;i++){
				t[i]=new Team(sc.nextInt(),sc.nextInt(),sc.nextInt(),sc.nextInt());
			}
			t=sort(t,n);
			select(t,n);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}

}
