import java.io.*;
import java.util.Calendar;

class Main {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            Calendar cal = Calendar.getInstance();
            int y = 2004;
            int m;
            int d;
			while(true) {
            		line = br.readLine();
            		String[] lineList = line.split(" ", 0);
                m = Integer.parseInt(lineList[0]);
                d = Integer.parseInt(lineList[1]);
                if(m == 0) {
                	break;
                }
                System.out.println(getDay(y, m, d));
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
    private static String getDay(int y, int m, int d) {
    		Calendar cal = Calendar.getInstance();
    		cal.set(y, m - 1, d);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY: return "Sunday";
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            case Calendar.SATURDAY: return "Saturday";
        }
        throw new IllegalStateException();
    }
}
