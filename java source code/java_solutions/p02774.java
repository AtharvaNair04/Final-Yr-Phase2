import java.util.*;

public class Main {


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        long k = sc.nextLong();
//        int[] arr = new int[n];
        long[] arr = new long[n];

        int positive = 0;
        int negative = 0;
        long zero = 0;

        for (int i=0; i<n; i++){
            long tmp = sc.nextLong();
            if (tmp<0) negative++;
            else if (0<tmp) positive++;
            else zero++;
            arr[i] = tmp;
        }
        Arrays.sort(arr);

//        for (int i=0; i<n; i++) System.out.println(arr[i]);


//        for (int i=0; i<n; i++) System.out.println(arr[i]);

        long[] plus = new long[positive];
        long[] minus = new long[negative];

        int p = 0;
        int q = 0;

        for (int i=0; i<n; i++){
            if (arr[i]<0) minus[q++] = arr[i];
            else if (0<arr[i]) plus[p++] = arr[i];
        }

//        for (int i=0; i<plus.length; i++)System.out.println(plus[i]);
//        for (int i=0; i<minus.length; i++)System.out.println(minus[i]);


        long left = -(long)1e18-1;
        long right = (long)1e18+1;

//        while (right-left>1){
//        long mid = (right+left)/2;
//        if (count(plus, minus, zero, mid)<k){
//            left = mid;
//        }else {
//            right = mid;
//        }
//    }

        int cnt = 0;

        long mid = 0;

//        System.out.println(right);
        while (left<right){

            mid = (left+right)/2;
            if (mid<0 || (mid==0 && left==-1)) mid-=1;
//            System.out.println(mid);
//            System.out.println(count(plus, minus, zero, mid));
//            if (mid>=0){
            if (count(plus, minus, zero, mid)<k){
                left = mid+1;
            }else {
                right = mid;
            }
//            }else {
//                if (count(plus, minus, zero, mid)<k){
//                    left = mid+1;
//                }else {
//                    right = mid;
//                    if (left+1==right && cnt==3){
//                        break;
//                    }
//                }
//                if (left+1==right) cnt++;
//            }
//            System.out.println(left);
//            System.out.println(right);

//            if (left+1==right && count(plus, minus, zero, mid)>=k) break;

        }

        if (mid>=0) System.out.println(left-1);
        else System.out.println(right-1);

//        for (int i=0; i<n; i++)System.out.println(arr[i]);


    }

    public static long count(long[] posi, long[] nega, long z, long mid){
        long res = 0;
        int t = posi.length-1;
        for (int i=0; i<posi.length-1; i++){
            while (t>=0 && posi[i]*posi[t]>=mid) --t;
            res += Math.max(0, t-i);
        }

        t = 0;
        for (int i=nega.length-1; i>=0; i--){
            while (t<nega.length && nega[i]*nega[t]>=mid) ++t;
            res += Math.max(0, i-t);
        }

        t = 0;
        for (int i=0; i<posi.length; i++){
            while (t<nega.length && posi[i]*nega[t]<mid) ++t;
            res += Math.max(0, t);
        }

        if (0<mid) res += z*(posi.length+nega.length)+z*(z-1)/2;
        return res;
    }
}
