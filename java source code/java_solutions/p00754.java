import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    String s;
    char[] c;
    LinkedList<Character> st;
    boolean ans;

    void run(){
	while(sc.hasNext()){
	    s = sc.nextLine();
	    if(s.length()==1 && s.equals(".")) break;

	    ans = true;
	    st = new LinkedList<Character>();
	    c = s.toCharArray();
	    for(int i=0; i<c.length && ans; i++){
		if(c[i]=='(' || c[i]=='[')
		    st.push(c[i]);
		else if(c[i]==')'){
		    if(st.size()==0) ans = false;
		    else{
			char x = st.pop();
			if(x!='(') ans = false;
		    }
		}
		else if(c[i]==']'){
		    if(st.size()==0) ans = false;
		    else{
			char x = st.pop();
			if(x!='[') ans = false;
		    }
		}
	    }
	    if(st.size()!=0) ans=false;


	    System.out.println(ans ? "yes" : "no");
	}
    }
}
