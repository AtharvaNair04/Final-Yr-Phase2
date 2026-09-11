import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Comparator;

class TrackRecord
{
    private int id;
    private float tm;

    public TrackRecord(int id, float tm)
    {
	this.id = id;
	this.tm = tm;
    }

    public int getId()
    {
	return this.id;
    }

    public float getTm()
    {
	return this.tm;
    }

    public int setId(int id)
    {
	return this.id = id;
    }

    public float setTm(float tm)
    {
	return this.tm = tm;
    }
}

public class Main
{

    public static void main(String[] args)
    {
	final int nRec=24;
	Scanner scan = new Scanner(System.in);
	TrackRecord[] rec = new TrackRecord[nRec];

	try{
	    for (int i=0; i<nRec; i++){
		int id = scan.nextInt();
		Float tm = scan.nextFloat();
		try {
		    rec[i] = new TrackRecord(id,tm);
		} catch(NullPointerException e){
		    System.out.println("NPE:"+e+" i="+i);
		}
	    }
	} catch (InputMismatchException e){
	    System.out.println("Wrong Type"+e);
	}
	
	TrackRecord[] g1 = new TrackRecord[8];
	g1 = Arrays.copyOfRange(rec, 0, 8);
	TrackRecord[] g2 = new TrackRecord[8];
	g2 = Arrays.copyOfRange(rec, 8, 16);
	TrackRecord[] g3 = new TrackRecord[8];
	g3 = Arrays.copyOfRange(rec, 16, 24);

        Arrays.sort(g1, new Comparator<TrackRecord>() {
		public int compare(TrackRecord record1, TrackRecord record2) {
		    return (int)(record1.getTm()*100) - (int)(record2.getTm()*100);
		}
	    });

        Arrays.sort(g2, new Comparator<TrackRecord>() {
		public int compare(TrackRecord record1, TrackRecord record2) {
		    return (int)(record1.getTm()*100) - (int)(record2.getTm()*100);
		}
	    });
        Arrays.sort(g3, new Comparator<TrackRecord>() {
		public int compare(TrackRecord record1, TrackRecord record2) {
		    return (int)(record1.getTm()*100) - (int)(record2.getTm()*100);
		}
	    });
	ArrayList<TrackRecord> ans = new ArrayList<TrackRecord>(); 
	ans.add(g1[0]);
	ans.add(g1[1]);
	ans.add(g2[0]);
	ans.add(g2[1]);
	ans.add(g3[0]);
	ans.add(g3[1]);

	TrackRecord[] rec1 = new TrackRecord[nRec-6];
	for (int i=0; i<6; i++){
	    rec1[i] = g1[i+2];
	}
	for (int i=0; i<6; i++){
	    rec1[i+6] = g2[i+2];
	}
	for (int i=0; i<6; i++){
	    rec1[i+12] = g3[i+2];
	}

        Arrays.sort(rec1, new Comparator<TrackRecord>() {
		public int compare(TrackRecord record1, TrackRecord record2) {
		    return(int)(record1.getTm()*100) - (int)(record2.getTm()*100);
		}
	    });

	ans.add(rec1[0]);
	ans.add(rec1[1]);
	for (int i=0; i<ans.size(); i++){
	    System.out.printf("%d %.2f\n", ans.get(i).getId(), ans.get(i).getTm()); 
	}
    }

}
