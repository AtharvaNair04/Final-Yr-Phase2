import java.util.*;

public class Main {
    public static int N;
    public static int M;
    public static List<Integer> k;
    public static List<List<Integer>> s;
    public static List<Integer> p;
    public static void main (String args[]){
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        M = scanner.nextInt();
        k = new ArrayList();
        s = new ArrayList();
        p = new ArrayList();
        for(int i=0; i<M; i++){
            int ki = scanner.nextInt();
            k.add(ki);
            List<Integer> si = new ArrayList();
            for(int j=0; j<ki; j++){
                si.add(scanner.nextInt());
            }
            s.add(si);
        }
        for(int i=0; i<M; i++){
            p.add(scanner.nextInt());
        }
        int numOK = 0;
        List<Integer> switches = new ArrayList();
        Queue<List<Integer>> queue = new ArrayDeque();
        List<Integer> states = new ArrayList(Arrays.asList(0,1));
        queue.add(switches);
        while( queue.size()>0 ){
            List<Integer> now = queue.poll();

            if(now.size() == N){
                int numOnBulb = 0;
                for(int i=0; i<M; i++){
                    int numOnSwitch = 0;
                    for(Integer sij:s.get(i)){
                        numOnSwitch = numOnSwitch + now.get(sij-1);
                    }
                    if(numOnSwitch%2 == p.get(i)){
                        numOnBulb++;
                    }
                }
                if(numOnBulb==M){
                    numOK++;
                }
            }
            else{
                for(Integer nextState:states){
                    List<Integer> next = new ArrayList(now);
                    next.add(nextState);
                    if(isOK(next)){
                        queue.offer(next);
                    }
                }
            }
        }
        System.out.println(numOK);
    }

    public static boolean isOK(List<Integer> next){
        // for(int i=0; i<M; i++){
        //     int maxS = Math.max(s.get(i));
        //     if(maxS <= next.size()){
        //         int sum = 0;
        //         for(int state:next){
        //             sum = sum + state;
        //         }
        //         if()
        //         return false;
        //     }
        // }
        return true;
    }
}
