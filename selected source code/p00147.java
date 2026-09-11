import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        List<Integer> output = new ArrayList<Integer>();

        // ??¨??????\???
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;

        while (true) {
            input = br.readLine();
            if (input == null || input.isEmpty()) {
                break;
            }
            int inputNum = Integer.parseInt(input);
            int time = 0;
            boolean result = true;
            int[] seat = new int[17];
            List<Integer> waitGuest = new ArrayList<Integer>();

            while (result) {
                exitFukushimaken(seat, time);

                if (time % 5 == 0) {
                    waitGuest.add(time / 5);
                }

                while (waitGuest.size() > 0)  {
                    if (isWaitGuest(waitGuest)) {
                        int guest = guestOfNumber(waitGuest.get(0));
                        int seatNum = confirmEmptySeat(seat, guest);

                        if (seatNum != 99) {
                            enterFukushimaken(waitGuest.get(0), guest, seatNum, seat, time);
                            if (inputNum == waitGuest.get(0)) {
                                output.add(time - (waitGuest.get(0) * 5));
                                result = false;
                                break;
                            }
                            waitGuest.remove(0);
                        } else {
                            break;
                        }
                    }
                }
                ++time;
            }
        }
        for (int outputNum : output) {
            System.out.println(outputNum);
        }
    }



    private static boolean isWaitGuest(List<Integer> waitGuest){

        if (waitGuest.size() != 0) {
            return true;
        }
        return false;
    }

    // i????????????°??????????????????°?????????
    private static int guestOfNumber(int number) {
        if (number % 5 == 1) {
            return 5;
        } else {
            return 2;
        }
    }

    // ???§??????????????????????????????
    // i = ???§?????????´????????????????????????99??????§?????????
    private static int confirmEmptySeat(int[] seat, int guest) {
        for (int i = 0; i <= seat.length - guest; ++i) {
            int count = 0;
            for (int j = 0; j < guest; ++j) {
                if (seat[i + j] != 0) {
                    break;
                } else {
                    ++count;
                    if (guest == count) {
                        return i;
                    }
                }
            }
        }
        return 99;
    }

    // ????????????
    private static void enterFukushimaken(int groupNum, int guest, int seatNum,
            int[] seat, int time) {
        int ateTime = time + ateFoodTime(groupNum);
        for (int i = 0; i < guest; ++i) {
            seat[seatNum + i] = ateTime;
        }
    }

    // ??£?????????????£??????¢??????????????¢??????????
    private static void exitFukushimaken(int[] seat, int time) {
        for (int i = 0; i < seat.length; ++i) {
            if (time == seat[i]) {
                seat[i] = 0;
            }
        }
    }

    // i????????????°??????????????£?????????????
    private static int ateFoodTime(int number) {
        int a = (number % 2) * 17;
        int b = (number % 3) * 3;
        return a + b + 19;
    }

}
