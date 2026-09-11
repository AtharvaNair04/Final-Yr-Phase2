import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author MS14A
 * @version 2017/03/21
 */
public class Main {

    /** Input：空白 */
    private static final String DELIMITER = " ";

    /**
     * メインメソッド
     *
     * @param args
     */
    public static void main(String[] args) {
        // ローカルテスト用。提出時は"System.in"に修正。
        InputStream input = System.in;
        // File input = new File("D:/Temp/AOJ/0282_ProgrammingContest.txt");

        try (InputStreamReader inputReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputReader)) {
            int firstLine[] = convertToIntArray(bufferedReader.readLine()
                    .split(DELIMITER));
            int numberOfTeam = firstLine[0];
            int numberOfRecord = firstLine[1];
            int timeOfContest = firstLine[2];

            // インデックスでチームIDを表し、得点・テレビに映った時間をそれぞれ配列で管理。
            int[] scores = new int[numberOfTeam];
            int[] times = new int[numberOfTeam];

            int highScoreTeam = 0;
            int now = 0;
            for (int i = 0; i < numberOfRecord; i++) {
                String line = bufferedReader.readLine();
                int[] record = convertToIntArray(line.split(DELIMITER));

                int teamNum = record[0] - 1;
                int gettingScore = record[2];
                scores[teamNum] += gettingScore;
                times[highScoreTeam] += (record[1] - now);

                if (0 < gettingScore) {
                    if (scores[highScoreTeam] < scores[teamNum]
                            || (scores[highScoreTeam] == scores[teamNum] && teamNum < highScoreTeam)) {
                        highScoreTeam = teamNum;
                    }
                } else if (teamNum == highScoreTeam) {
                    highScoreTeam = 0;
                    for (int j = 0; j < numberOfTeam; j++) {
                        if (scores[highScoreTeam] < scores[j]) {
                            highScoreTeam = j;
                        }
                    }
                }

                now = record[1];
            }

            // 最終処理
            times[highScoreTeam] += (timeOfContest - now);

            System.out.println(gettingResult(times));

        } catch (Exception e) {
            // 対応しない。
            e.printStackTrace();
        }
    }

    private static int[] convertToIntArray(String[] target) {
        int[] result = new int[target.length];

        for (int i = 0; i < target.length; i++) {
            result[i] = Integer.parseInt(target[i]);
        }

        return result;
    }

    private static int gettingResult(int[] times) {
        int result = 0;
        int max = 0;
        for (int i = 0; i < times.length; i++) {
            if (max < times[i]) {
                max = times[i];
                result = i;
            }
        }
        return result + 1;
    }
}
