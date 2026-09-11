import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

  
    public static void main(String[] args)  {

    	try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while((line=br.readLine())!=null){if(line.isEmpty())break;
                    String[] spl = line.split(" ");
	            int n1 = Integer.parseInt(spl[0]), n2 = Integer.parseInt(spl[1]);
	            if(n1==0&&n2==0){break;}
                    String yuka[][] = new String[n1][n2];
                    
                    for(int i=0; i<n1; i++){
                        line=br.readLine();
                        char spl2[] =line.toCharArray();
                        for(int j=0; j<n2; j++){
                            yuka[i][j]=spl2[j]+"";
                        }
                    }
                    
                    int[] basyo = {0,0};
                    while(true){
                        String moji = yuka[basyo[0]][basyo[1]];
                        if(yuka[basyo[0]][basyo[1]].equals(".")){
                            System.out.println(basyo[1]+" "+basyo[0]);
                            break;
                        }
                        if(yuka[basyo[0]][basyo[1]].equals("already")){
                            System.out.println("LOOP");
                            break;
                        }
                        int hozon1 = basyo[0], hozon2 = basyo[1];
                        if(yuka[basyo[0]][basyo[1]].equals("<")){
                            basyo[1]--;
                        }
                        if(yuka[basyo[0]][basyo[1]].equals(">")){
                            basyo[1]++;
                        }
                        if(yuka[basyo[0]][basyo[1]].equals("^")){
                            basyo[0]--;
                        }
                        if(yuka[basyo[0]][basyo[1]].equals("v")){
                            basyo[0]++;
                        }
                        yuka[hozon1][hozon2]="already";

                    }
                    
                    
            }//End WHILE


        }catch(Exception e){e.printStackTrace();}

    }


}
