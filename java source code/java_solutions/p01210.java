import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

//Speed
public class Main{

	class C{
		int suit, rank;
		String str;
		public C(String s) {
			str = s;
			suit = s.charAt(0);
			char c = s.charAt(1);
			rank = c=='A'?1:c=='K'?13:c=='Q'?12:c=='J'?11:c=='X'?10:(c-'0');
		}
		boolean isNeighbor(C c){
			int r1 = rank-1, r2 = c.rank-1;
			return (r1+1)%13==r2 || (r2+1)%13==r1;
		}
		boolean isStrong(C c){
			int r1 = rank, r2 = c.rank;
			if(r1==1)r1=14;
			if(r2==1)r2=14;
			return r1!=r2?r1>r2:suit>c.suit;
		}
		boolean same(C c){
			return suit==c.suit&&rank==c.rank;
		}
		@Override
		public String toString() {
			return str;
		}
	}
	
	final int WAIT = 0, THINK = 1, DECK = 2, PUT = 3, BACK = 4, WIN = 5, DRAW = 6;
	String[] E={"WAIT","THINK","DECK","PUT","BACK","WIN","DRAW"};
	
	class R{
		int id;
		C lastCard, nowCard;
		int pos, to, t, event, num;
		Queue<C> deck;
		boolean leftSide;
		C[] place;
		public R(int id) {
			place = new C[4];
			this.id = id;
			lastCard = nowCard = null;
			deck = new LinkedList<C>();
		}
		void put(){
			num--;
			table[to] = nowCard;
//			System.out.println("Put by:"+id+" Table:"+to+" Card:"+table[to]+" t:"+t);
			nowCard = null;
			lastCard = table[to];
//			if(!deck.isEmpty()){
//				t+=3;
//				draw();
//			}
//			System.out.println("NUM:"+num);
		}
		void putPrepare(){
			int j = choice();
			nowCard = place[j];
//			System.out.println((id==0?"A":"B")+" prepare card: "+nowCard);
//			for(int i=0;i<4;i++)System.out.print(" "+place[i]);
//			System.out.println("\n"+table[0]+" "+table[1]);
			place[j] = null;
			pos = j;
			event = PUT;
			if(table[id].isNeighbor(nowCard)){
				to = id;
				t+=5;
				leftSide = false;
			}
			else{
				to = 1-id;
				t+=7;
				leftSide = true;
			}
		}
		void back(){
//			event = THINK;
//			System.out.println((id==0?"A":"B")+" back card:"+nowCard+" T:"+t);
			place[pos] = nowCard;
		}
		void init(){
			num = Math.min(4, deck.size());
			for(int i=0;i<num;i++)place[i]=deck.poll();
		}
		void restart(){
			if(deck.isEmpty()){
				num--;
				for(int i=0;i<4;i++)if(place[i]!=null){
					table[id] = place[i];
//					System.out.println("Put by:"+id+" Table:"+id+" Card:"+table[id]+" This is restart.");
					lastCard  = place[i];
					place[i] = null;
					break;
				}
			}
			else{
				table[id] = deck.poll();
//				System.out.println("Put by:"+id+" Table:"+id+" Card:"+table[id]+" This is restart.");
				lastCard = table[id];
			}
		}
		void draw(){
			for(int i=0;i<4;i++)if(place[i]==null){
				num++;
				place[i] = deck.poll();
//				System.out.println((id==0?"A":"B")+" Draw card: "+place[i]);
				break;
			}
		}
		int choice(){
			C c = null;
			int r = -1;
			for(int i=0;i<4;i++)if(place[i]!=null){
				if(place[i].isNeighbor(table[id])){
					if(c==null){
						c = place[i]; r = i;
					}
					else if(!place[i].isStrong(c)){
						c = place[i]; r = i;
					}
				}
			}
			if(r!=-1)return r;
			for(int i=0;i<4;i++)if(place[i]!=null){
				if(place[i].isNeighbor(table[1-id])){
					if(c==null){
						c = place[i]; r = i;
					}
					else if(!place[i].isStrong(c)){
						c = place[i]; r = i;
					}
				}
			}
			return r;
		}
		public String toString() {
			return "id: "+id+"\n"+"Event: "+E[event]+"\n"+"LastCard: "+lastCard+"\n"+"NowCard: "+nowCard+"\n"+"Num: "+num+"\n"+" Time:"+t+"\n";
		}
	}
	
	R[] robot;
	C[] table;
	
	void run(){
		Scanner sc = new Scanner(System.in);
		robot = new R[2];
		table = new C[2];
		for(;;){
			int n = sc.nextInt();
			if(n==0)break;
//			if(CASE==134){
//				System.out.println(n);
//				for(int i=0;i<n;i++)System.out.print(sc.next()+" ");
//				n = sc.nextInt();
//				System.out.println(n);
//				for(int i=0;i<n;i++)System.out.print(sc.next()+" ");
//				break;
//			}
			robot[0] = new R(0); robot[1] = new R(1);
			table[0] = table[1] = null;
			while(n--!=0)robot[0].deck.add(new C(sc.next()));
			n = sc.nextInt();
			while(n--!=0)robot[1].deck.add(new C(sc.next()));
			robot[0].init(); robot[1].init();
			robot[0].restart(); robot[1].restart();
			boolean winA = false;
			if(robot[0].num==0&&robot[1].num==0){
				winA = robot[0].lastCard.isStrong(robot[1].lastCard);
				System.out.println(winA?"A wins.":"B wins.");
				continue;
			}
			if(robot[0].num==0){
				System.out.println("A wins."); continue;
			}
			if(robot[1].num==0){
				System.out.println("B wins."); continue;
			}
			robot[0].event = THINK; robot[1].event = THINK;
			for(int T=0;;T++){
//				System.out.println();
//				System.out.println("T:"+T);
				R r0 = robot[0], r1 = robot[1];
//				System.out.println(r0);
//				System.out.println(r1);
//				System.out.println("A: "+E[r0.event]);
//				for(int i=0;i<4;i++)System.out.print(" "+r0.place[i]);
//				if(r0.event==PUT)System.out.println("\nLeft:"+r0.leftSide);
//				System.out.println();
//				System.out.println("B: "+E[r1.event]);
//				for(int i=0;i<4;i++)System.out.print(" "+r1.place[i]);
//				if(r1.event==PUT)System.out.println("\nLeft:"+r1.leftSide);
//				System.out.println();
//				System.out.println(table[0]+" "+table[1]);
				//process at same time
				if(r0.t==T&&r1.t==T){
					if(r0.event == WIN && r1.event == WIN){
						winA = r0.lastCard.isStrong(r1.lastCard);
						break;
					}
					if(r0.event == WAIT && r1.event == WAIT){
						r0.restart(); r1.restart();
						r0.t++; r1.t++;
						if(r0.num==0)r0.event = WIN;
						if(r1.num==0)r1.event = WIN;
						if(r0.event==WIN && r1.event==WIN){
							winA = r0.lastCard.isStrong(r1.lastCard); break;
						}
						if(r0.event==WIN){
							winA = true; break;
						}
						if(r1.event==WIN){
							winA = false; break;
						}
						r0.event = THINK; r1.event = THINK;
						if(r0.event == THINK){
							int j = r0.choice();
							if(j==-1){
								r0.event = WAIT;
								r0.t++;
							}
							else{
								r0.putPrepare();
							}
						}
						if(r1.event == THINK){
							int j = r1.choice();
							if(j==-1){
								r1.event = WAIT;
								r1.t++;
							}
							else{
								r1.putPrepare();
							}
						}
						continue;
					}
					if(r0.event == PUT && r1.event == PUT){
						if(r0.leftSide!=r1.leftSide){
							//put Robot A
							if(r0.leftSide){
								r1.t+=5;
								r1.event = BACK;
								r0.put();
								//draw from deck
								if(!r0.deck.isEmpty()){
									r0.t+=3;
									r0.event = DRAW;
								}
								else{
									//win check
									if(r0.num==0){
										winA = true; break;
									}
									//behave as THINK
									else{
										int j = r0.choice();
										if(j==-1){
											r0.event = WAIT;
											r0.t++;
										}
										else{
											r0.putPrepare();
										}
									}
								}
							}
							//put Robot B
							else{
								r0.t+=5;
								r0.event = BACK;
								r1.put();
								if(!r1.deck.isEmpty()){
									r1.t+=3;
									r1.event = DRAW;
								}
								else{
									if(r1.num==0){
										winA = false; break;
									}
									else{
										int j = r1.choice();
										if(j==-1){
											r1.event = WAIT;
											r1.t++;
										}
										else{
											r1.putPrepare();
										}
									}
								}
							}
						}
						else{
							//put Robot A
							r0.put();
							r1.put();
							//draw from deck
							if(!r0.deck.isEmpty()){
								r0.t+=3;
								r0.event = DRAW;
							}
							else{
								//win check
								if(r0.num==0){
									r0.event = WIN;
								}
								//behave as THINK
								else{
									int j = r0.choice();
									if(j==-1){
										r0.event = WAIT;
										r0.t++;
									}
									else{
										r0.putPrepare();
									}
								}
							}
							//put Robot B
							if(!r1.deck.isEmpty()){
								r1.t+=3;
								r1.event = DRAW;
							}
							else{
								if(r1.num==0){
									r1.event = WIN;
								}
								else{
									int j = r1.choice();
									if(j==-1){
										r1.event = WAIT;
										r1.t++;
									}
									else{
										r1.putPrepare();
									}
								}
							}
							//double win check
							if(r0.event == WIN && r1.event == WIN){
								winA = r0.lastCard.isStrong(r1.lastCard);
								break;
							}
							if(r0.event == WIN){
								winA = true; break;
							}
							if(r1.event == WIN){
								winA = false; break;
							}
						}
						continue;
					}
					if(r0.event == PUT){
						r0.put();
						//draw from deck
						if(!r0.deck.isEmpty()){
							r0.t+=3;
							r0.event = DRAW;
						}
						else{
							//win check
							if(r0.num==0){
								if(r1.event==WIN){
									winA = r0.lastCard.isStrong(r1.lastCard); break;
								}
								winA = true; break;
							}
							//behave as THINK
							else{
								int j = r0.choice();
								if(j==-1){
									r0.event = WAIT;
									r0.t++;
								}
								else{
									r0.putPrepare();
								}
							}
						}
						//process Robot B
						if(r1.event == DRAW){
							r1.draw();
							r1.event = THINK;
						}
						if(r1.event == BACK){
							r1.back();
							r1.event = THINK;
						}
						if(r1.event == WAIT){
							r1.event = THINK;
						}
						if(r1.event == WIN){
							winA = false; break;
						}
						if(r1.event == THINK){
							if(r1.num==3 && !r1.deck.isEmpty()){
								r1.t+=3;
								r1.event = DRAW;
							}
							else{
								int j = r1.choice();
								if(j==-1){
									r1.event = WAIT;
									r1.t++;
								}
								else{
									r1.putPrepare();
								}
							}
						}
						continue;
					}
					else if(r1.event == PUT){
						r1.put();
						if(!r1.deck.isEmpty()){
							r1.t+=3;
							r1.event = DRAW;
						}
						else{
							//win check
							if(r1.num==0){
								if(r0.event==WIN){
									winA = r0.lastCard.isStrong(r1.lastCard); break;
								}
								winA = false; break;
							}
							//behave as THINK
							else{
								int j = r1.choice();
								if(j==-1){
									r1.event = WAIT;
									r1.t++;
								}
								else{
									r1.putPrepare();
								}
							}
						}
						//process Robot A
						if(r0.event == DRAW){
							r0.draw();
							r0.event = THINK;
						}
						if(r0.event == BACK){
							r0.back();
							r0.event = THINK;
						}
						if(r0.event == WAIT){
							r0.event = THINK;
						}
						if(r0.event == WIN){
							winA = true; break;
						}
						if(r0.event == THINK){
							if(r0.num==3 && !r0.deck.isEmpty()){
								r0.t+=3;
								r0.event = DRAW;
							}
							else{
								int j = r0.choice();
								if(j==-1){
									r0.event = WAIT;
									r0.t++;
								}
								else{
									r0.putPrepare();
								}
							}
						}
						continue;
					}
				}
				//process only Robot A
				if(r0.t==T){
					if(r0.event == DRAW){
						r0.draw();
						r0.event = THINK;
					}
					if(r0.event == BACK){
						r0.back();
						r0.event = THINK;
					}
					if(r0.event == WAIT){
						r0.t++;
					}
					else if(r0.event == THINK){
						//draw from deck
						if(r0.num==3 && !r0.deck.isEmpty()){
							r0.event = DRAW;
							r0.t+=3;
						}
						else{
							int j = r0.choice();
							if(j==-1){
								r0.event = WAIT;
								r0.t++;
							}
							else{
								r0.putPrepare();
							}
						}
					}
					else if(r0.event == PUT){
						//put same place, so cancel Robot B
						if(r1.event == PUT && r0.leftSide!=r1.leftSide){
							r0.put();
							//draw from deck
							if(!r0.deck.isEmpty()){
								r0.t+=3;
								r0.event = DRAW;
							}
							else{
								//win check
								if(r0.num==0){
									r0.event = WIN;
								}
								//behave as THINK
								else{
									int j = r0.choice();
									if(j==-1){
										r0.event = WAIT;
										r0.t++;
									}
									else{
										r0.putPrepare();
									}
								}
							}
							r1.t = T+5;
							r1.event = BACK;
						}
						else {
							r0.put();
							//draw from deck
							if(!r0.deck.isEmpty()){
								r0.t+=3;
								r0.event = DRAW;
							}
							else{
								//win check
								if(r0.num==0){
									r0.event = WIN;
								}
								//behave as THINK
								else{
									int j = r0.choice();
									if(j==-1){
										r0.event = WAIT;
										r0.t++;
									}
									else{
										r0.putPrepare();
									}
								}
							}
						}
						if(r1.event == WAIT){
							r1.event = THINK;
						}
					}
					if(r0.event == WIN){
						winA = true; break;
					}
				}
				//process only Robot B
				if(r1.t==T){
					if(r1.event == DRAW){
						r1.draw();
						r1.event = THINK;
					}
					if(r1.event == BACK){
						r1.back();
						r1.event = THINK;
					}
					if(r1.event == WAIT){
						r1.t++;
					}
					else if(r1.event == THINK){
						if(r1.num==3 && !r1.deck.isEmpty()){
							r1.t+=3;
							r1.event = DRAW;
						}
						else{
							int j = r1.choice();
							if(j==-1){
								r1.event = WAIT;
								r1.t++;
							}
							else{
								r1.putPrepare();
							}
						}
					}
					else if(r1.event == PUT){
						if(r0.event == PUT && r0.leftSide!=r1.leftSide){
							r1.put();
							if(!r1.deck.isEmpty()){
								r1.t+=3;
								r1.event = DRAW;
							}
							else{
								if(r1.num==0){
									r1.event = WIN;
								}
								else{
									int j = r1.choice();
									if(j==-1){
										r1.event = WAIT;
										r1.t++;
									}
									else{
										r1.putPrepare();
									}
								}
							}
							r0.t = T+5;
							r0.back();
							int j = r0.choice();
							if(j==-1){
								r0.event = WAIT;
								r0.t++;
							}
							else{
								r0.putPrepare();
							}
						}
						else{
							r1.put();
							if(!r1.deck.isEmpty()){
								r1.t+=3;
								r1.event = DRAW;
							}
							else{
								if(r1.num==0){
									r1.event = WIN;
								}
								else{
									int j = r1.choice();
									if(j==-1){
										r1.event = WAIT;
										r1.t++;
									}
									else{
										r1.putPrepare();
									}
								}
							}
						}
						if(r0.event==WAIT){
							r0.event = THINK;
						}
					}
					if(r1.event == WIN){
						winA = false; break;
					}
				}
			}
			System.out.println(winA?"A wins.":"B wins.");
		}
	}
	
	void debug(Object...o){
		System.out.println(Arrays.deepToString(o));
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
