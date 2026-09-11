import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String[] Lary= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] Sary= {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        int[] ansBox=new int[26];

        //ansBox初期化
        for(int i=0;i<26;i++) {
            ansBox[i]=0;
        }

        //文字列を入力
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc=new Scanner(System.in);
        String str;
        //while(true){
        while(sc.hasNext()) {
            str=sc.nextLine();
            //if(str.equals("")){break;}
            //文字数をカウント
            int n=str.length();
            //n回廻す
            for(int i=0;i<n;i++) {
                //strからi個目の文字を切り出す
                char a;
                a=str.charAt(i);

                //それぞれをif文にかけ、アルファベットを判断
                for(int j=0;j<26;j++) {
                    if(Lary[j].equals(String.valueOf(a)) || Sary[j].equals(String.valueOf(a))) {
                        ansBox[j]++;
                    }
                }
            }
        }
        for(int i=0;i<26;i++) {
            System.out.println(Sary[i]+" : "+ansBox[i]);
        }
	}

}
