import java.io.*;

class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while((line = br.readLine()) != null){
            		String[] lineList = line.split(" ", 0);
                int x = Integer.parseInt(lineList[0]);
                int y = Integer.parseInt(lineList[1]);
                if(x < y) {
	    	    			int tmp = x;
	    	    			x = y;
	    	    			y = tmp;
	    	    		}
                System.out.println(gcd(x, y));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param line
     *            ??´??°x,y?????\???
     * @return x,y????????§??¬?´???°
     */
    private static long gcd(int x, int y) {
    		if(y == 0) {
    			return x;
    		}else{
    			return gcd(y, x % y);
    		}
    }
}
