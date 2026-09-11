import java.util.Scanner;

public class Main{
    String[] kang = {"ld", "mb", "mp", "nc", "nd", "ng", "nt", "nw", "ps", "qu", "cw", "ts"};

    Main(){
	Scanner sc = new Scanner(System.in);
	while(sc.hasNext()){
	    int n = Integer.parseInt(sc.nextLine());
	    if(n == 0){
		continue;
	    }
	    Wd[] wd = new Wd[38];
	    for(int i = 0; i < 26; i++){
		wd[i] = new Wd(String.valueOf((char)('a'+i)), i);
	    }
	    for(int i = 26; i < 38; i++){
		wd[i] = new Wd(kang[i-26], i);
	    }

	    for(int k = 0; k < n; k++){
		String[] str = (sc.nextLine()).split(" ");
		for(int i = 0; i < str.length; i++){
		    String st = str[i];
		    while(st.length() > 1){
			int t = -1;
			boolean f1 = false, f2 = false;
			String s1;
			if(st.length() > 2){
			    s1 = st.substring(0, 2);
			    for(int j = 26; j < 38; j++){
				if(s1.equals(wd[j].str)){
				    f1 = true;
				    t = j;
				    st = st.substring(2, st.length());
				    break;
				}
			    }
			} else if(st.length() == 2){
			    for(int j  = 26; j < 38; j++){
				if(st.equals(wd[j].str)){
				    f1 = f2 = true;
				    break;
				}
			    }
			    if(f1 && f2){
				break;
			    }
			}
			if(!f1){
			    s1 = st.substring(0, 1);
			    for(int j = 0; j < 26; j+=1){
				if(s1.equals(wd[j].str)){
				    f2 = true;
				    t = j;
				    st = st.substring(1, st.length());
				    break;
				}
			    }   
			}
			
			if(f1 || f2){
			    f1 = false;
			    if(st.length() > 1){
				s1 = st.substring(0, 2);
				for(int j = 26; j < 38; j++){
				    if(s1.equals(wd[j].str)){
					f1 = true;
					wd[t].plusCt(j);
					break;
				    }
				}
			    }
			    if(!f1){
				s1 = st.substring(0, 1);
				for(int j = 0; j < 26; j+=1){
				    if(s1.equals(wd[j].str)){
					wd[t].plusCt(j);
					break;
				    }
				}   
			    }
			}
			// next
		    }
		}
	    }
	    for(int i = 0; i < 38; i++){
		wd[i].getMax();
		wd[i].print(wd[wd[i].mx].str);
	    }
	}
    }

    public static void main(String[] args){
	new Main();
    }
}

class Wd{
    String str;
    int n, mx;
    int[] stlt;
    Wd(){
	this.str = "";
	this.n = 0;
    }


    Wd(String str, int n){
	this.str = str;
	this.n = n;
	stlt = new int[38];
    }

    void getMax(){
	this.mx = 0;
	for(int i = 1; i < 38; i++){
	    if(stlt[i] > stlt[mx]){
		mx = i;
	    }
	}
    }

    void plusCt(int c){
	stlt[c]++;
    }

    void print(String s){
	System.out.println(str+" "+s+" "+Integer.toString(stlt[mx]));
    }
}
