import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;
 
public class Main {
 
    public static void main(String[] args) {
        final int BASE_SIZE = 200;
        Scanner in = new Scanner(System.in);
        int nSize = in.nextInt();
        int qSize = in.nextInt();
        ArrayList<Integer> all = new ArrayList<Integer>();
        int[] layerSize = new int[(nSize - 1) / BASE_SIZE + 1];
        int[] layerMin = new int[(nSize - 1) / BASE_SIZE + 1];
        {
            for (int n = 0; n < nSize; n++) {
                int val = in.nextInt();
                all.add(val);
                int layerIndex = n / BASE_SIZE;
                if (n % BASE_SIZE == 0) {
                    layerMin[layerIndex] = Integer.MAX_VALUE;
                }
                layerSize[layerIndex]++;
                layerMin[layerIndex] = Math.min(layerMin[layerIndex], val);
            }
            // testPrint(all, layerSize, layerMin);
        }
        for (int q = 0; q < qSize; q++) {
            int q0 = in.nextInt();
            int q1 = in.nextInt();
            int q2 = in.nextInt();
            // System.out.println(q0 + ":" + q1 + ":" + q2);
            int[] lLayer = getLayer(q1, layerSize);
            int[] rLayer;
            if (q0 != 2) {
                rLayer = getLayer(q2, layerSize);
            } else {
                rLayer = null;
            }
            switch (q0) {
            case 0: {
                Integer val = all.remove(q2);
                all.add(q1, val);
                int lIndex = lLayer[L_INDEX];
                int rIndex = rLayer[L_INDEX];
                if (lIndex != rIndex) {
                    layerSize[lIndex]++;
                    layerSize[rIndex]--;
                    if (val == layerMin[rIndex]) {
                        int start = rLayer[L_START] + 1;
                        int end = start + layerSize[rIndex];
                        layerMin[rIndex] = Integer.MAX_VALUE;
                        for (int i = start; i < end; i++) {
                            layerMin[rIndex] = Math.min(layerMin[rIndex],
                                    all.get(i));
                        }
                    }
                    if (val < layerMin[lIndex]) {
                        layerMin[lIndex] = val;
                    }
                }
                break;
            }
            case 1: {
                int lIndex = lLayer[L_INDEX];
                int rIndex = rLayer[L_INDEX];
                int min = Integer.MAX_VALUE;
                if (lIndex != rIndex) {
                    int leftMin = all.get(q1);
                    int leftLast = lLayer[L_START] + layerSize[lIndex];
                    for (int i = q1; i < leftLast; i++) {
                        leftMin = Math.min(all.get(i), leftMin);
                    }
                    int rightMin = all.get(q2);
                    for (int i = rLayer[L_START]; i <= q2; i++) {
                        rightMin = Math.min(all.get(i), rightMin);
                    }
                    min = Math.min(leftMin, rightMin);
                    for (int i = lLayer[L_INDEX] + 1; i < rLayer[L_INDEX]; i++) {
                        min = Math.min(min, layerMin[i]);
                    }
                } else {
                    for (int i = q1; i <= q2; i++) {
                        min = Math.min(min, all.get(i));
                    }
                }
                System.out.println(min);
                break;
            }
            case 2: {
                all.set(q1, q2);
                if (layerMin[lLayer[L_INDEX]] >= q2) {
                    layerMin[lLayer[L_INDEX]] = q2;
                } else {
                    int min = q2;
                    for (int i = lLayer[L_START]; i < lLayer[L_START]
                            + layerSize[lLayer[L_INDEX]]; i++) {
                        min = Math.min(min, all.get(i));
                    }
                    layerMin[lLayer[L_INDEX]] = min;
                }
                break;
            }
            }
            // testPrint(all, layerSize, layerMin);
        }
    }
 
    static final int L_INDEX = 0;
    static final int L_START = 1;
 
    static int[] getLayer(int index, int[] layerSize) {
        int[] result = new int[2];
        int start = 0;
        for (int li = 0; true; li++) {
            int end = start + layerSize[li];
            if (end > index) {
                result[L_INDEX] = li;
                result[L_START] = start;
                break;
            }
            start = end;
        }
        return result;
    }
 
    private static void testPrint(LinkedList<Integer> all, int[] layerSize,
            int[] layerMin) {
        System.out.println("------------");
        System.out.println(all);
        System.out.println(Arrays.toString(layerSize));
        System.out.println(Arrays.toString(layerMin));
        System.out.println("------------");
    }
}
