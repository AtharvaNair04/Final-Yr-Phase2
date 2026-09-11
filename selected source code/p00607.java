import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static final int MAXINPUTLINE = 10;
    static final int MAXINPUTCOMMAND = 300;
    static final int MAXMODIFIEDTEXTLINE = 100;
    static final int MAXMODIFIERTEXTCHAR = 1000;
    static final String ENDOFLINE = "$";
    static Scanner scan = new Scanner(System.in);
    static ArrayList<String> modifyingText = new ArrayList<String>();
    static String buffer;
    static int cursorLocLine = 0;
    static int cursorLocRow = 0;

    public static void main(String args[]) {
        ArrayList<String> inputText;
        ArrayList<String> inputCommand;

        inputText = giveTextLine();
//        System.out.println("==============================");
//        System.out.println("give Text Success");

        inputCommand = giveCommand();

//        System.out.println("==============================");
//        System.out.println("give Command Success");
        modifyText(inputText, inputCommand);

        for (int i = 0; i < modifyingText.size(); i++) {
            int outLen = modifyingText.get(i).length();
            if (outLen <= 1) {
                System.out.print("\n");
            } else System.out.println(modifyingText.get(i).substring(0, outLen - 1));
        }
    }

    private static ArrayList<String> giveTextLine() {
        String input;
        ArrayList<String> inputText = new ArrayList<>();
//        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < MAXINPUTLINE; i++) {
            input = scan.nextLine();
            if (input.equals("END_OF_TEXT")) break;
            inputText.add(i, input);
        }
        return inputText;
    }

    private static ArrayList<String> giveCommand() {
        String input;
        ArrayList<String> inputCommand = new ArrayList<>();
//        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < MAXINPUTCOMMAND; i++) {
            input = scan.nextLine();
            if (input.equals("-")) break;
            inputCommand.add(i, input);
        }
        return inputCommand;
    }

    private static void modifyText(final ArrayList<String> inputText, final ArrayList<String> inputCommand) {

        for (int i = 0; i < inputText.size(); i++) {
            modifyingText.add(i, inputText.get(i) + ENDOFLINE);
        }

        for (int i = 0; i < inputCommand.size(); i++) {
//            System.out.println("===============================");
//            System.out.println(inputCommand.get(i) + " " + cursorLocRow + " " + cursorLocLine + " " + modifyingText.get(cursorLocLine).length() + " " + buffer);
//            System.out.println("===============================");
//            modifyingText.forEach(System.out::println);

            if (inputCommand.get(i).equals("a")) editA();
            else if (inputCommand.get(i).equals("e")) editE();
            else if (inputCommand.get(i).equals("p")) editP();
            else if (inputCommand.get(i).equals("n")) editN();
            else if (inputCommand.get(i).equals("f")) editF();
            else if (inputCommand.get(i).equals("b")) editB();
            else if (inputCommand.get(i).equals("d")) editD();
            else if (inputCommand.get(i).equals("k")) editK();
            else if (inputCommand.get(i).equals("y")) editY();
        }
    }

    private static String remove(String str, int removeIndex) {
        StringBuilder builder = new StringBuilder(str);
        return builder.deleteCharAt(removeIndex).toString();
    }

    private static String insert(String str, int insertIndex) {
        StringBuilder builder = new StringBuilder(str);

        if (builder.length() <= insertIndex) {
            return builder.append(buffer).toString();
        } else {
            return builder.insert(insertIndex, buffer).toString();
        }
    }

    private static void editA() {
        cursorLocRow = 0;
    }

    private static void editE() {
        cursorLocRow = currentLineString().length() - 1;
    }

    private static void editP() {
        cursorLocRow = 0;
        if (cursorLocLine > 0) cursorLocLine--;
    }

    private static void editN() {
        cursorLocRow = 0;
        if (isLastLine()) return;
        cursorLocLine++;
    }

    private static void editF() {
        if (isLastRow()) {
            if (isLastLine()) return;
            cursorLocLine++;
            cursorLocRow = 0;
        } else {
            cursorLocRow++;
        }
    }

    private static void editB() {
        if (cursorLocRow > 0) cursorLocRow--;
        else if (cursorLocLine > 0) {
            cursorLocRow = modifyingText.get(cursorLocLine - 1).length() - 1;
            cursorLocLine--;
        }
    }

    private static void editD() {
        if (!isLastRow()) {
            replaceCurrentLine(remove(modifyingText.get(cursorLocLine), cursorLocRow));
        } else {
            if (currentLineString().length() == 1) {
                modifyingText.remove(cursorLocLine);
            } else if (!isLastLine()) {
                replaceCurrentLine(currentLineString() + modifyingText.get(cursorLocLine + 1));
                replaceCurrentLine(remove(currentLineString(), cursorLocRow));
                modifyingText.remove(cursorLocLine + 1);
            }
        }
    }

    private static void editK() {
        if (isLastRow()) {
            if (!isLastLine()) {

                replaceCurrentLine(currentLineString() + modifyingText.get(cursorLocLine + 1));
                replaceCurrentLine(remove(currentLineString(), cursorLocRow));

                for (int i = cursorLocLine + 1; i < modifyingText.size(); i++) {
                    if (isLastLine(i + 1))
                        replaceLine(i, modifyingText.get(i + 1));
                }
                modifyingText.remove(lastLine());
                buffer = "\n";
            }
        } else {
            buffer = currentLineString().substring(cursorLocRow, currentLineString().length() - 1);
            if (cursorLocRow == 0) replaceCurrentLine(ENDOFLINE);
            else replaceCurrentLine(currentLineString().substring(0, cursorLocRow) + ENDOFLINE);
            cursorLocRow = currentLineString().length() - 1;
        }
    }

    private static void editY() {
        if (buffer != null) {
            if (buffer.equals("\n")) {

                if (isLastRow()) {
                    String movingText = currentLineString().substring(cursorLocRow);
                    replaceCurrentLine(currentLineString().substring(0, cursorLocRow) + ENDOFLINE);
                    insertLine(cursorLocLine + 1, movingText);
                } else {
                    String movingText = currentLineString().substring(cursorLocRow);
                    insertLine(cursorLocLine + 1, movingText);

                    if (cursorLocRow != 0) {
                        replaceCurrentLine(currentLineString().substring(0, cursorLocRow) + ENDOFLINE);
                    } else {
                        replaceCurrentLine(ENDOFLINE);
                    }
                }
                cursorLocRow = 0;
                cursorLocLine++;

            } else {
                replaceCurrentLine(insert(currentLineString(), cursorLocRow));
                cursorLocRow += buffer.length();
            }
        }
    }

    private static String currentLineString() {
        return modifyingText.get(cursorLocLine);
    }

    private static void replaceLine(int index, String str) {
        modifyingText.set(index, str);
    }

    private static void replaceCurrentLine(String str) {
        modifyingText.set(cursorLocLine, str);
    }

    private static void insertLine(int index, String str) {
        modifyingText.add(index, str);
    }

    private static void insertInCurrentLine(String str) {
        modifyingText.add(cursorLocLine, str);
    }

    private static boolean isLastLine() {
        return modifyingText.size() <= cursorLocLine + 1;
    }

    private static boolean isLastLine(int i) {
        return modifyingText.size() > i;
    }

    private static boolean isLastRow() {
        return modifyingText.get(cursorLocLine).length() <= cursorLocRow + 1;
    }

    private static int lastLine() {
        return modifyingText.size() - 1;
    }

    private static boolean isEOL() {
        return String.valueOf(currentLineString().charAt(cursorLocRow)).equals(ENDOFLINE);
    }
}
