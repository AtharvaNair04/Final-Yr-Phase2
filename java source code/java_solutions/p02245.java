import java.util.Scanner;

/**
 * IDA* for 8 or 15 puzzle
 * f(x) = h(x) +depth(g(x))
 *
 */
public class Main {

    private int[] up = {-1,0};
    private int[] down = {1,0};
    private int[] left = {0,-1};
    private int[] right = {0,1};

    private final int UP = 0;
    private final int DOWN = 2;
    private final int LEFT = 1;
    private final int RIGHT = 3;

    private int SIZE;

    /*
    store the position of the each point of the target state
     */
    private int[][] targetPoints;

    /*
    f(x),total cost
     */
    private static int cost;

    //target state
    private static int[][] tState = {{1 ,2 ,3 },{4 ,5 ,6 },{7 ,8, 0}};

    private static int blank_row,blank_column;

    public Main(int[][] state) {
        SIZE = state.length;
        targetPoints = new int[SIZE * SIZE][2];

        /*
        get the position of blank point
         */
        for(int i=0;i<state.length;i++) {
            for(int j=0;j<state[i].length;j++) {
                if(state[i][j] == 0) {
                    blank_row = i;
                    blank_column = j;
                    break;
                }
            }
        }
        /*
        get the position(x,y) by using a two-dimensional array.
        e.g. 2's position is (2,1),
        targetPoints [2][0] = 2-1 =1,targetPoints[2][1]=1-1=0
         */
        for(int i=0;i<tState.length;i++) {
            for(int j=0;j<tState.length;j++) {
                targetPoints[tState[i][j]][0] = i;

                targetPoints[tState[i][j]][1] = j;
            }
        }
    }
    public boolean checkState(int[][] state){
        boolean b =true;
        for(int i = 0;i<SIZE;i++)
            for(int j=0;j<SIZE;j++){
                if(!(state[i][j]==tState[i][j]))
                    b = false;
            }
        return b;
    }

    public static void main(String[] args) {
        int step = 0;
        int[][] sState=new int[3][3];
        Scanner sc = new Scanner(System.in);
        int count = 0;
        String s;
        while(count<3&&(s=sc.nextLine())!=null){
            sState[count][0] = Integer.parseInt(s.split(" ")[0]);
            sState[count][1] = Integer.parseInt(s.split(" ")[1]);
            sState[count][2] = Integer.parseInt(s.split(" ")[2]);
            count++;
        }
        Main idaAlgorithm = new Main(sState);

        if(idaAlgorithm.checkState(sState)){
            System.out.println(step);
        }
        else{
            int j = idaAlgorithm.getHeuristic(sState);
            int i = -1;

            for (cost = j; ; cost++) {
                if (idaAlgorithm.solve(sState
                        , blank_row, blank_column, 0, i, j)) {
                    break;
                }
            }
            System.out.println(cost);
        }
    }
    /**
     *
     * @param state
     * @param blank_row the row-coordinate of 0 in ths state
     * @param blank_column the column-coordinate of 0 in ths state
     * @param dep the depth of the state
     * @param d the direction of the last move
     * @param h the value of h(x)
     * @return
     */
    public boolean solve(int[][] state,int blank_row,int blank_column,
                         int dep,int d,int h) {
        /*
        the value of h(x) after moving
         */
        int h1;

        /*
        make a comparison with the target state
         */
        boolean isSolved = checkState(state);
        if(isSolved) {
            return true;
        }

        if(dep == cost) {
            return false;
        }

        /*
        the position of the blank point after moving
         */
        int blank_row1 ;
        int blank_column1;
        int[][] state2 = new int[SIZE][SIZE];

        for(int direction=0;direction<4;direction++) {
            for(int i=0;i<state.length;i++) {
                for(int j=0;j<state.length;j++) {
                    state2[i][j] = state[i][j];
                }
            }

            /*
            check whether the direction of this movement is opposite of the direction of the last move or not
             */
            if(direction != d && (d%2 == direction%2)) {
                continue;
            }

            if(direction == UP) {
                blank_row1 = blank_row + up[0];
                blank_column1 = blank_column + up[1];
            } else if(direction == DOWN) {
                blank_row1 = blank_row + down[0];
                blank_column1 = blank_column + down[1];
            } else if(direction == LEFT) {
                blank_row1 = blank_row + left[0];
                blank_column1 = blank_column + left[1];
            } else {
                blank_row1 = blank_row + right[0];
                blank_column1 = blank_column + right[1];
            }

            /*
            border check
             */
            if(blank_column1 < 0 || blank_column1 == SIZE
                    || blank_row1 < 0 || blank_row1 == SIZE) {
                continue ;
            }

            /*
            swap position
             */
            state2[blank_row][blank_column] = state2[blank_row1][blank_column1];
            state2[blank_row1][blank_column1] = 0;

            /*
            calculate the h(x)
             */
            if(direction == DOWN && blank_row1
                    > targetPoints[state[blank_row1][blank_column1]][0]) {
                h1 = h - 1;
            } else if(direction == UP && blank_row1
                    < targetPoints[state[blank_row1][blank_column1]][0]){
                h1 = h - 1;
            } else if(direction == RIGHT && blank_column1
                    > targetPoints[state[blank_row1][blank_column1]][1]) {
                h1 = h - 1;
            } else if(direction == LEFT && blank_column1
                    < targetPoints[state[blank_row1][blank_column1]][1]) {
                h1 = h - 1;
            } else {
                h1 = h + 1;
            }

            /*
            if f(x) increases,prune
             */
            if(h1+dep+1> cost) {
                continue;
            }

            /*
            do the iteration with depth increasing
             */
            if(solve(state2, blank_row1, blank_column1, dep+1, direction, h1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get the value of h(x)
     * @param state
     */
    public int getHeuristic(int[][] state) {
        int heuristic = 0;
        for(int i=0;i<state.length;i++) {
            for(int j=0;j<state[i].length;j++) {
                if(state[i][j] != 0) {
                    heuristic = heuristic +
                            Math.abs(targetPoints[state[i][j]][0] - i)
                            + Math.abs(targetPoints[state[i][j]][1] - j);
                }
            }
        }
        return heuristic;
    }
}
