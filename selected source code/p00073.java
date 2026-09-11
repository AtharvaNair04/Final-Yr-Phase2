import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.StringTokenizer;

class Main{

  public static void main(String[] args){

    BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
    String str="";
    StringTokenizer stk;
    int tei,takasa;

    try{

    	while(true) {
    		str=bfr.readLine();
    	      if(str=="") {
    	        System.exit(0);
    	      }
    	      tei=Integer.parseInt(str);

    	      str=bfr.readLine();
    	      if(str=="") {
    	        System.exit(0);
    	      }
    	      takasa=Integer.parseInt(str);

    	      if(tei==0 && takasa==0) {
    	    	  System.exit(0);
    	      }
    	      else {
    	    	  System.out.println(BigDecimal.valueOf(((double) tei)*((double) tei)+((double)tei)*Math.sqrt(((double)tei*tei/4)+((double)takasa*takasa))*2).toPlainString());
    	      }
    	}

    } catch (IOException e) {
      System.exit(0);
    }
    catch(NumberFormatException e) {
      System.exit(0);
    }

    catch(NullPointerException e) {
      System.exit(0);
    }
  }
}
