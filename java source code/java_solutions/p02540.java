import java.util.*; import java.io.*; import java.math.*;
public class Main{
	static void solve(){//Here is the main function
		int N = nextInt();
		Node[] list = new Node[N];
		UnionFind uf = new UnionFind(N);
		HashMap<Integer, Integer> useY = new HashMap<>();
		TreeSet<Integer> unused = new TreeSet<>();
		for(int i = 0; i < N; i++){
			unused.add(i + 1);
			ArrayList<Integer> tmp = nextIntArray();
			list[i] = new Node(tmp.get(0), tmp.get(1), i);
			useY.put(tmp.get(1), i);
		}
		Arrays.sort(list, new OriComparator1());
		for(int i = 0; i < N; i++){
			int p = list[i].y;
			unused.remove(p);
			if(uf.getSize(list[i].no) != 1){
				continue;
			}
			SortedSet<Integer> s = unused.tailSet(p);
			Iterator<Integer> it = s.iterator();
			while(it.hasNext()){
				uf.doUnion(list[i].no, useY.get(it.next()));
			}
		}
		for(int i = 0; i < N; i++){
			myout(uf.getSize(i));
		}
	}
	//Method addition frame start

static class Node{
	int x;
	int y;
	int no;
	Node(int x, int y, int no){
		this.x = x;
		this.y = y;
		this.no = no;
	}
	public String toString(){
		return "no : " + no + ", x : " + x + ", y : " + y;
	}
}

static class OriComparator1 implements Comparator<Node>{
	public int compare(Node mae, Node ato){
		return mae.x - ato.x;
	}
}

static class OriComparator2 implements Comparator<Node>{
	public int compare(Node mae, Node ato){
		return mae.y - ato.y;
	}
}


static class UnionFind{
  int[] list;
  UnionFind(int size){
    list = new int[size];
    for(int i = 0; i < size; i++){
      list[i] = -1;
    }
  }
  boolean isSame(int mae, int ato){
    return getRootIndex(mae) == getRootIndex(ato);
  }
  int getRootIndex(int index){
    if(list[index] < 0){
      return index;
    }else{
      list[index] = getRootIndex(list[index]);
      return list[index];
    }
  }
  void doUnion(int mae, int ato){
    int maeRoot = getRootIndex(mae);
    int atoRoot = getRootIndex(ato);
    if(!isSame(maeRoot, atoRoot)){
      if(maeRoot >= atoRoot){
        list[maeRoot] += list[atoRoot];
        list[atoRoot] = maeRoot;
      }else{
        list[atoRoot] += list[maeRoot];
        list[maeRoot] = atoRoot;
      }
    }
  }
  int getSize(int index){
    return -list[getRootIndex(index)];
  }
}

	//Method addition frame end

	//Don't have to see. start------------------------------------------
	static class InputIterator{
		ArrayList<String> inputLine = new ArrayList<String>(1024);
		int index = 0; int max; String read;
		InputIterator(){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try{
				while((read = br.readLine()) != null){
					inputLine.add(read);
				}
			}catch(IOException e){}
			max = inputLine.size();
		}
		boolean hasNext(){return (index < max);}
		String next(){
			if(hasNext()){
				return inputLine.get(index++);
			}else{
				throw new IndexOutOfBoundsException("There is no more input");
			}
		}
	}
	static HashMap<Integer, String> CONVSTR = new HashMap<Integer, String>();
	static InputIterator ii = new InputIterator();//This class cannot be used in reactive problem.
	static PrintWriter out = new PrintWriter(System.out);
	static void flush(){out.flush();}
	static void myout(Object t){out.println(t);}
	static void myerr(Object t){System.err.print("debug:");System.err.println(t);}
	static String next(){return ii.next();}
	static boolean hasNext(){return ii.hasNext();}
	static int nextInt(){return Integer.parseInt(next());}
	static long nextLong(){return Long.parseLong(next());}
	static double nextDouble(){return Double.parseDouble(next());}
	static ArrayList<String> nextStrArray(){return myconv(next(), 8);}
	static ArrayList<String> nextCharArray(){return myconv(next(), 0);}
	static ArrayList<Integer> nextIntArray(){
		ArrayList<String> input = nextStrArray(); ArrayList<Integer> ret = new ArrayList<Integer>(input.size());
		for(int i = 0; i < input.size(); i++){
			ret.add(Integer.parseInt(input.get(i)));
		}
		return ret;
	}
	static ArrayList<Long> nextLongArray(){
		ArrayList<String> input = nextStrArray(); ArrayList<Long> ret = new ArrayList<Long>(input.size());
		for(int i = 0; i < input.size(); i++){
			ret.add(Long.parseLong(input.get(i)));
		}
		return ret;
	}
	@SuppressWarnings("unchecked")
	static String myconv(Object list, int no){//only join
		String joinString = CONVSTR.get(no);
		if(list instanceof String[]){
			return String.join(joinString, (String[])list);
		}else if(list instanceof ArrayList){
			return String.join(joinString, (ArrayList)list);
		}else{
			throw new ClassCastException("Don't join");
		}
	}
	static ArrayList<String> myconv(String str, int no){//only split
		String splitString = CONVSTR.get(no);
		return new ArrayList<String>(Arrays.asList(str.split(splitString)));
	}
	public static void main(String[] args){
		CONVSTR.put(8, " "); CONVSTR.put(9, "\n"); CONVSTR.put(0, "");
		solve();flush();
	}
	//Don't have to see. end------------------------------------------
}
