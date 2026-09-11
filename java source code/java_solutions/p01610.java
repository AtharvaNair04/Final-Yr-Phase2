import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
 
 
public class Main{
	
	public static class Cube{
		char[][] up, front, left, right, back, down;
		
		public Cube(){
			up    = new char[][]{{'r', 'r'}, {'r', 'r'}};
			front = new char[][]{{'y', 'y'}, {'y', 'y'}};
			left  = new char[][]{{'g', 'g'}, {'g', 'g'}};
			right = new char[][]{{'b', 'b'}, {'b', 'b'}};
			back  = new char[][]{{'w', 'w'}, {'w', 'w'}};
			down  = new char[][]{{'o', 'o'}, {'o', 'o'}};
		}
		
		private void rotate(char[][] rot){
			char tmp = rot[0][0];
			rot[0][0] = rot[1][0];
			rot[1][0] = rot[1][1];
			rot[1][1] = rot[0][1];
			rot[0][1] = tmp;
		}
		
		public void R(){
			char tmp_0 = front[0][1];
			char tmp_1 = front[1][1];
			
			front[0][1] = down[0][1];
			front[1][1] = down[1][1];
			down[0][1] = back[1][0];
			down[1][1] = back[0][0];
			back[0][0] = up[1][1];
			back[1][0] = up[0][1];
			up[0][1] = tmp_0;
			up[1][1] = tmp_1;
			
			rotate(right);
		}
		
		public void L(){
			char tmp_0 = front[0][0];
			char tmp_1 = front[1][0];
			
			front[0][0] = up[0][0];
			front[1][0] = up[1][0];
			up[0][0] = back[1][1];
			up[1][0] = back[0][1];
			back[0][1] = down[1][0];
			back[1][1] = down[0][0];
			down[0][0] = tmp_0;
			down[1][0] = tmp_1;
		
			rotate(left);
		}
		
		public void U(){
			char tmp_0 = front[0][0];
			char tmp_1 = front[0][1];
			
			front[0][0] = right[0][0];
			front[0][1] = right[0][1];
			right[0][0] = back[0][0];
			right[0][1] = back[0][1];
			back[0][0] = left[0][0];
			back[0][1] = left[0][1];
			left[0][0] = tmp_0;
			left[0][1] = tmp_1;
		
			rotate(up);
		}
		
		public void D(){
			char tmp_0 = front[1][0];
			char tmp_1 = front[1][1];
			
			front[1][0] = left[1][0];
			front[1][1] = left[1][1];
			left[1][0] = back[1][0];
			left[1][1] = back[1][1];
			back[1][0] = right[1][0];
			back[1][1] = right[1][1];
			right[1][0] = tmp_0;
			right[1][1] = tmp_1;
		
			rotate(down);
		}
		
		public void F(){
			char tmp_0 = up[1][0];
			char tmp_1 = up[1][1];
			
			up[1][0] = left[1][1];
			up[1][1] = left[0][1];
			left[0][1] = down[0][0];
			left[1][1] = down[0][1];
			down[0][0] = right[1][0];
			down[0][1] = right[0][0];
			right[0][0] = tmp_0;
			right[1][0] = tmp_1;
		
			rotate(front);
		}
		
		public void B(){
			char tmp_0 = up[0][0];
			char tmp_1 = up[0][1];
			
			up[0][0] = right[0][1];
			up[0][1] = right[1][1];
			right[0][1] = down[1][1];
			right[1][1] = down[1][0];
			down[1][1] = left[1][0];
			down[1][0] = left[0][0];
			left[0][0] = tmp_1;
			left[1][0] = tmp_0;
			
			rotate(back);
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append("..");
			sb.append(up[0][0]);
			sb.append(up[0][1]);
			sb.append("..");
			sb.append("..");
			sb.append("\n");
			sb.append("..");
			sb.append(up[1][0]);
			sb.append(up[1][1]);
			sb.append("..");
			sb.append("..");
			sb.append("\n");
			
			sb.append(left[0][0]);
			sb.append(left[0][1]);
			sb.append(front[0][0]);
			sb.append(front[0][1]);
			sb.append(right[0][0]);
			sb.append(right[0][1]);
			sb.append(back[0][0]);
			sb.append(back[0][1]);
			sb.append("\n");
			
			sb.append(left[1][0]);
			sb.append(left[1][1]);
			sb.append(front[1][0]);
			sb.append(front[1][1]);
			sb.append(right[1][0]);
			sb.append(right[1][1]);
			sb.append(back[1][0]);
			sb.append(back[1][1]);
			sb.append("\n");
			
			sb.append("..");
			sb.append(down[0][0]);
			sb.append(down[0][1]);
			sb.append("..");
			sb.append("..");
			sb.append("\n");
			sb.append("..");
			sb.append(down[1][0]);
			sb.append(down[1][1]);
			sb.append("..");
			sb.append("..");
			sb.append("\n");
		
			return sb.toString();
		}
	}
	
    public static void main(String[] args) throws IOException {
    	Scanner sc = new Scanner(System.in);
    	
    	char[] input = sc.next().toCharArray();

    	Cube cube = new Cube();
    	for(char c : input){
    		if(c == 'R'){
    			cube.R();
    		}else if(c == 'L'){
    			cube.L();
    		}else if(c == 'U'){
    			cube.U();
    		}else if(c == 'D'){
    			cube.D();
    		}else if(c == 'F'){
    			cube.F();
    		}else if(c == 'B'){
    			cube.B();
    		}
    		
    		//System.out.println(cube);
    	}
    	
    	System.out.print(cube);
    }
     
}
