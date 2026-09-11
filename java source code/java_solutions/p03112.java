import java.util.*;
public class Main
{
    static int floorSearch(long arr[], int low,
                           int high, long x)
    {
        // If low and high cross each other
        if (low > high)
            return -1;
        // If last element is smaller than x
        if (x >= arr[high])
            return high;

        // Find the middle point
        int mid = (low+high)/2;

        // If middle point is floor.
        if (arr[mid] == x)
            return mid;

        // If x lies between mid-1 and mid
        if (mid > 0 && arr[mid-1] <= x && x <
                arr[mid])
            return mid-1;

        // If x is smaller than mid, floor
        // must be in left half.
        if (x < arr[mid])
            return floorSearch(arr, low,
                    mid - 1, x);

        // If mid-1 is not floor and x is
        // greater than arr[mid],
        return floorSearch(arr, mid + 1, high,
                x);
    }
    static int ceilSearch(long arr[], int low, int high, long x)
    {
        int mid;

      /* If x is smaller than or equal to the
         first element, then return the first element */
        if(x <= arr[low])
            return low;

      /* If x is greater than the last
         element, then return -1 */
        if(x > arr[high])
            return -1;

      /* get the index of middle element
         of arr[low..high]*/
        mid = (low + high)/2;  /* low + (high - low)/2 */

      /* If x is same as middle element,
         then return mid */
        if(arr[mid] == x)
            return mid;

      /* If x is greater than arr[mid], then
         either arr[mid + 1] is ceiling of x or
         ceiling lies in arr[mid+1...high] */
        else if(arr[mid] < x)
        {
            if(mid + 1 <= high && x <= arr[mid+1])
                return mid + 1;
            else
                return ceilSearch(arr, mid+1, high, x);
        }

      /* If x is smaller than arr[mid],
         then either arr[mid] is ceiling of x
         or ceiling lies in arr[mid-1...high] */
        else
        {
            if(mid - 1 >= low && x > arr[mid-1])
                return mid;
            else
                return ceilSearch(arr, low, mid - 1, x);
        }
    }

    public static void main(String args[])
    {
        Scanner sc=new Scanner(System.in);
        int a=sc.nextInt();
        int b=sc.nextInt();
        int q=sc.nextInt();
        long sh[]=new long[a];
        long tem[]=new long[b];
        for(int i=0;i<a;i++)
            sh[i]=sc.nextLong();
        for(int i=0;i<b;i++)
            tem[i]=sc.nextLong();
        Arrays.sort(sh);
        Arrays.sort(tem);
        while(q-->0)
        {
            long x=sc.nextLong();
            int a1=floorSearch(sh,0,a-1,x);
            int b1=ceilSearch(sh,0,a-1,x);
            int a2=floorSearch(tem,0,b-1,x);
            int b2=ceilSearch(tem,0,b-1,x);
            long ans1=Long.MAX_VALUE;
            long ans2=Long.MAX_VALUE;
            long ans3=Long.MAX_VALUE;
            long ans4=Long.MAX_VALUE;
            if(a1>=0&&a2>=0)
            {
                ans1=Math.max(x-sh[a1],x-tem[a2]);
            }
            if(b1>=0&&b2>=0)
            {
                ans2=Math.max(sh[b1]-x,tem[b2]-x);
            }
            if(a1>=0&&b2>=0)
            {
                long dis=tem[b2]-sh[a1];
                long diq=x-sh[a1];
                long diqs=tem[b2]-x;
                long c=Math.min(diq,diqs);
                ans3=dis+c;
            }
            if(a2>=0&&b1>=0)
            {
                long dis=sh[b1]-tem[a2];
                long diq=x-tem[a2];
                long diqs=sh[b1]-x;
                long c=Math.min(diq,diqs);
                ans4=dis+c;
            }
            long ff=Math.min(ans1,ans2);
            long hh=Math.min(ans3,ans4);
            long ans=Math.min(ff,hh);
            System.out.println(ans);
        }
    }
}
