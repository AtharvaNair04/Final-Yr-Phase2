import java.util.*;
public class Main {
	final int INF = 1 << 24;
	HashMap<String, Integer> tonum;
	HashMap<Integer, Integer> timetable;
	ArrayList<D> toT, fromT, toH,fromH;
	ArrayList<ArrayList<C>> pass;
	int Tnum, Hnum, citysize, timesize;

	class C implements Comparable<C>{
		int from, to, begin, end, fare;
		public C(String [] input) {
			this.from = tonum.get(input[0]);
			this.begin = calcTime(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
			this.to = tonum.get(input[3]);
			this.end = calcTime(Integer.parseInt(input[4]), Integer.parseInt(input[5]));
			this.fare = Integer.parseInt(input[6]);
		}

		public String toString() {
			return "C [from=" + from + ", to=" + to + ", begin=" + begin
			           + ", end=" + end + ", fare=" + fare + "]";
		}

		public int compareTo(C o) {
			if(this.begin < o.begin) return -1;
			if(this.begin > o.begin) return 1;
			return 0;
		}

	}

	class D implements Comparable<D>{
		int nowpoint, nowtime, nowfare, starttime;

		public D(int nowpoint, int nowtime, int nowfare, int starttime) {
			this.nowpoint = nowpoint;
			this.nowtime = nowtime;
			this.nowfare = nowfare;
			this.starttime = starttime;
		}

		public int compareTo(D o) {
			if(this.nowtime < o.nowtime) return -1;
			if(this.nowtime > o.nowtime) return 1;
			return 0;
		}

		@Override
		public String toString() {
			return "D [nowpoint=" + nowpoint + ", nowtime=" + nowtime
			           + ", nowfare=" + nowfare + ", starttime=" + starttime + "]";
		}
	}

	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			if(n == 0) break;
			sc.nextLine();
			tonum = new HashMap<String, Integer>();
			timetable = new HashMap<Integer, Integer>();
			C [] inputdata = new C[n];
			pass = new ArrayList<ArrayList<C>>();
			TreeSet<Integer> timedata = new TreeSet<Integer>();
			for(int i = 0; i < n; i++){
				String [] inputline = sc.nextLine().split("[ :]");
				if(! tonum.containsKey(inputline[0])){
					tonum.put(inputline[0], tonum.size());
				}
				if(! tonum.containsKey(inputline[3])){
					tonum.put(inputline[3], tonum.size());
				}
				int h1 = Integer.parseInt(inputline[1]);
				int m1 = Integer.parseInt(inputline[2]);
				int h2 = Integer.parseInt(inputline[4]);
				int m2 = Integer.parseInt(inputline[5]);
				int time1 = calcTime(h1, m1);
				int time2 = calcTime(h2, m2);
				timedata.add(time1);
				timedata.add(time2);
				inputdata[i] = new C(inputline);
			}

			Arrays.sort(inputdata);
			for(int num: timedata){
				timetable.put(num, timetable.size());
			}

//			//debug
//			for(int key: timetable.keySet()){
//				System.out.println(key + " " + timetable.get(key) );
//			}

			citysize = tonum.size();
			timesize = timetable.size();
			for(int i = 0; i < citysize; i++){
				pass.add(new ArrayList<C>());
			}
			for(int i = 0; i < n; i++){
				if(inputdata[i].begin < calcTime(8, 0) || inputdata[i].end > calcTime(18, 0)){
					continue;
				}
				pass.get(inputdata[i].from).add(inputdata[i]);
			}
			Tnum = tonum.get("Tokyo");
			Hnum = tonum.get("Hakodate");

			int ans = INF;
			for(int i = 0; i < citysize; i++){
				toT = new ArrayList<D>();
				fromT = new ArrayList<D>();
				toH = new ArrayList<D>();
				fromH = new ArrayList<D>();

				if(i == Tnum){
					fromT.add(new D(Tnum, calcTime(8, 0), 0, calcTime(18, 0)));
					toT.add(new D(Tnum, calcTime(8, 0), 0, calcTime(18, 0)));
				}
				else if(i == Hnum){
					fromH.add(new D(Hnum, calcTime(8, 0), 0, calcTime(18, 0)));
					toH.add(new D(Hnum, calcTime(8, 0), 0, calcTime(18, 0)));
				}

				//from tokyo
				fromTermination(Tnum,i);
				//to tokyo
				toTermination(i, Tnum);
				//from hako
				fromTermination(Hnum,i);
				//to hako
				toTermination(i, Hnum);
				//calc fare
				for(int fromTInd = 0; fromTInd < fromT.size(); fromTInd++){
					for(int toTInd = 0; toTInd < toT.size(); toTInd++){
						for(int fromHInd = 0; fromHInd < fromH.size(); fromHInd++){
							for(int toHInd = 0; toHInd < toH.size(); toHInd++){
								int lastArrive = Math.max(fromT.get(fromTInd).nowtime, fromH.get(fromHInd).nowtime);
								int fastleave = Math.min(toT.get(toTInd).starttime, toH.get(toHInd).starttime);
								int fare = fromT.get(fromTInd).nowfare + fromH.get(fromHInd).nowfare + toT.get(toTInd).nowfare + toH.get(toHInd).nowfare;
//								if(fare == 11090){
//									System.out.println(fromT.toString());
//									System.out.println(toT.toString());
//									System.out.println(fromH.toString());
//									System.out.println(toH.toString());
//								}
								if(fastleave - lastArrive >= 30){
									ans = Math.min(ans, fare);

								}
							}
						}
					}
				}
			}
			System.out.println(ans == INF ? 0 : ans);

		}
	}

	private void fromTermination(int from, int termination){
		PriorityQueue<D> open = new PriorityQueue<D>();
		open.add(new D(from,-1, 0, -1));

		int [][] close = new int[citysize][timesize];
		for(int i = 0; i < citysize; i++){
			Arrays.fill(close[i], INF);
		}
		for(int i = 0; i < timesize; i++){
			close[from][i] = 0;
		}

		while(! open.isEmpty()){
			D now = open.poll();
			for(int i = 0; i < pass.get(now.nowpoint).size(); i++){
				C toobj = pass.get(now.nowpoint).get(i);
				boolean cond1;
				cond1 = (now.nowtime <= toobj.begin);
				if(cond1){
					int nextpoint = toobj.to;
					int nexttime = toobj.end;
					int nextfare = now.nowfare + toobj.fare;
					int nextstarttime = now.starttime;
					if(nextstarttime == -1){
						nextstarttime = toobj.begin;
					}
					D next = new D(nextpoint, nexttime, nextfare, nextstarttime);
					int nexttimeInd = timetable.get(nexttime);
					if(close[nextpoint][nexttimeInd] <= nextfare) continue;
					for(int j = nexttimeInd; j < timesize; j++){
						close[nextpoint][j] = Math.min(close[nextpoint][j], nextfare);
					}

					if(nextpoint == termination){
						if(from == Tnum) fromT.add(next);
						else fromH.add(next);
						continue;
					}
					open.add(next);
				}
			}
		}
	}

	private void toTermination(int from, int termination){
		int endtime = calcTime(18, 0);
		PriorityQueue<D> open = new PriorityQueue<D>();
		open.add(new D(from,endtime -1, 0, -1));

		int [][] close = new int[citysize][timesize];
		for(int i = 0; i < citysize; i++){
			Arrays.fill(close[i], INF);
		}
		for(int i = 0; i < timesize; i++){
			close[from][i] = 0;
		}

		while(! open.isEmpty()){
			D now = open.poll();
			for(int i = pass.get(now.nowpoint).size() - 1; i >= 0; i--){
				C toobj = pass.get(now.nowpoint).get(i);
				boolean cond1;
				cond1 = ((endtime - now.nowtime) <= toobj.begin);
				if(cond1){
					int nextpoint = toobj.to;
					int nexttime = toobj.end;
					int nextfare = now.nowfare + toobj.fare;
					int nextstarttime = now.starttime;
					if(nextstarttime == -1){
						nextstarttime = toobj.begin;
					}
					D next = new D(nextpoint, endtime - nexttime, nextfare, nextstarttime);
					int nexttimeInd = timetable.get(nexttime);
					if(close[nextpoint][nexttimeInd] <= nextfare) continue;
					for(int j = nexttimeInd; j < timesize; j++){
						close[nextpoint][j] = Math.min(close[nextpoint][j], nextfare);
					}
					if(nextpoint == termination){
						next.nowtime = endtime - next.nowtime;
						if(termination == Tnum) toT.add(next);
						else toH.add(next);
						continue;
					}
					open.add(next);
				}
			}
		}
	}

	private int calcTime(int h, int m){
		return h * 60 + m;
	}

	public static void main(String[] args) {
		Main obj = new Main();
		obj.doit();
	}
}
