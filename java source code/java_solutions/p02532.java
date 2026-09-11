import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String args[]){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String order=br.readLine();
            order=br.readLine();
            char[][] dq = new char[101][1000];
            int[] numDq = new int[101];
            ArrayList<Character> al = new ArrayList<Character>();
            while(!order.equals("quit")){
                String[] splited = order.split(" ");                
                if(splited[0].equals("push")){
                    int dqN=Integer.parseInt(splited[1]);
                    dq[dqN][numDq[dqN]]=splited[2].charAt(0);
                    numDq[dqN]++;
                }
                else if(splited[0].equals("pop")){
                    int dqN=Integer.parseInt(splited[1]);
                    char pulled =dq[dqN][numDq[dqN]-1];
                    numDq[dqN]--;
                    al.add(pulled);
                }
                else{//moved
                    int dqN=Integer.parseInt(splited[1]);
                    char pulled =dq[dqN][numDq[dqN]-1];
                    numDq[dqN]--;
                    int dqN2=Integer.parseInt(splited[2]);
                    dq[dqN2][numDq[dqN2]]=pulled;
                    numDq[dqN2]++;
                }                
                order=br.readLine();
            }
            for(char ch : al)
                System.out.println(ch);
        }catch(Exception e){e.printStackTrace();}
    }
}
