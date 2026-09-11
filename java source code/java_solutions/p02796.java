import java.util.*;

public class Main {

    public static class Arm {
        long x;
        long l;
        long min;
        long max;
        long distance;
        public Arm( long x , long l ){
            this.x = x;
            this.l = l;
            this.min = x - l;
            this.max = x + l;
            this.distance = this.max - this.min;
        }
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt( in.nextLine() );
        List<Arm> arms = new ArrayList<>();
        for( int i = 0 ; i < n ; i++ ){
            String[] params = in.nextLine().split(" ");
            arms.add( new Arm( Long.parseLong( params[0] ) , Long.parseLong( params[1] ) ) );
        }

        arms.sort( (o1, o2) -> {
            if( o1.min < o2.min ){
                return -1;
            }
            if( o1.min == o2.min ){
                return 0;
            }
            return 1;
        } );

        for( int i = 0 ; i < arms.size() - 1 ; i++ ) {
            Arm iArm = arms.get(i);
            Arm jArm = arms.get(i + 1);
            if (iArm.max <= jArm.min) {
                continue;
            }
            if (iArm.max <= jArm.max) {
                arms.remove(i+1);
                i--;
                n--;
                continue;
            }
            arms.remove(i);
            i--;
            n--;
        }

        System.out.println( n );

    }

}
