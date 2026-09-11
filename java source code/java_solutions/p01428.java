import java.util.*;
import static java.lang.System.*;
import static java.lang.Math.*;

class Main {
    public static Scanner sc = new Scanner(in);
    public static Random rand=new Random();


    public int getCount(int x,int y,char my,char enemy){
    	int D=0;
    	L:for(int i=0;i<8;i++){
			int ds=0;
			int px=x,py=y;
			px+=d[i][0];
			py+=d[i][1];
			if(!(0<= px && px<8 && 0<= py && py<8))
				continue L;

			while(map[py][px]==enemy){
				ds++;
				px+=d[i][0];
				py+=d[i][1];
				if(!(0<= px && px<8 && 0<= py && py<8))
					continue L;
			}
			if(map[py][px]==my)
				D+=ds;
		}
    	return D;
    }

    public void setCookie(int x,int y,char my ,char enemy){
    	map[y][x]=my;
    	D:for(int i=0;i<8;i++){
			int px=x,py=y;
			px+=d[i][0];
			py+=d[i][1];
			if(!(0<= px && px<8 && 0<= py && py<8))
				continue D;
			while(map[py][px]==enemy){
				px+=d[i][0];
				py+=d[i][1];
				if(!(0<= px && px<8 && 0<= py && py<8))
					continue D;
			}
			if(map[py][px]==my){
				px-=d[i][0];
				py-=d[i][1];
				while(!(px==x && py==y)){
					map[py][px]=my;
					px-=d[i][0];
					py-=d[i][1];
				}
			}
		}
    }

    char[][] map;
	int[][] d;
    public void run() {
    	map=new char[8][8];
    	for(int i=0;i<8;i++){
    		map[i]=sc.next().toCharArray();
    	}

    	boolean mami=true;
    	d=new int[][]{
    			{1,0},{1,1},
    			{0,1},{-1,1},
    			{-1,0},{-1,-1},
    			{0,-1},{1,-1}
    	};

    	boolean prevpass=false;

    	while(true){
    		int max=0,resx=-1,resy=-1;
    		for(int y=0;y<8;y++)for(int x=0;x<8;x++){
    			if(map[y][x]!='.')continue;
    			int D=getCount(x,y,mami?'o':'x',mami?'x':'o');
    			if(mami){
	    			if(max<D){
	    				max=D;
	    				resx=x;
	    				resy=y;
	    			}
    			}else{
    				if(max<=D){
	    				max=D;
	    				resx=x;
	    				resy=y;
	    			}
    			}
    		}

    		if(max>0){
    			setCookie(resx,resy,mami?'o':'x',mami?'x':'o');
        		prevpass=false;
    		}else{
    			if(!prevpass)
    				prevpass=true;
    			else{
    				for(int y=0;y<8;y++){
        				for(int x=0;x<8;x++)
        					pr(map[y][x]);

        				ln();
    				}
    				return;
    			}
    		}

    		mami=!mami;
    	}
   }
    public static void main(String[] args) {
        new Main().run();
    }

    public int[] nextIntArray(int n){
        int[] res=new int[n];
        for(int i=0;i<n;i++){
            res[i]=sc.nextInt();
        }
        return res;
    }
    public static void pr(Object o) {
        out.print(o);
    }
    public static void ln(Object o) {
        out.println(o);
    }
    public static void ln() {
        out.println();
    }
}
