import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        PriorityQueue<Job> q=new PriorityQueue<>();
        Job[] a = new Job[n];
        for(int i = 0; i < n; i++){
            a[i] = new Job(scanner.nextInt(),scanner.nextInt());
            q.add(a[i]);
        }
        Arrays.sort(a);
        long now = 0;
        for(long i = 0; i < n; i++){
            now += q.peek().day;
            if(now > q.poll().deadline){
                System.out.println("No");return;
            }
        }
        System.out.println("Yes");
    }
    static class Job implements Comparable<Job>{
		int day,deadline;
		public Job(int day,int deadline){
			this.deadline=deadline;
			this.day=day;
		}
		@Override
		public int compareTo(Job j){
            if(this.deadline==j.deadline){
                return j.day - this.day;
            }
			return this.deadline-j.deadline;
		}
	}
}
