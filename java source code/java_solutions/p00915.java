import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(final String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        boolean b = true;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (b) {
            b = runOnce(reader);
        }

        reader.close();
    }

    private boolean runOnce(final BufferedReader reader) throws IOException {
        final String[] params = reader.readLine().split(" ");
        final int count = Integer.parseInt(params[0]);
        final int length = Integer.parseInt(params[1]);
        if (count == 0 && length == 0) return false;
        final List<Ant> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            final Ant a = new Ant(reader.readLine().split(" "), i + 1);
            list.add(a);
        }
        exec(list, length);
        return true;
    }

    private void exec(final List<Ant> ants, final int length) {
        List<Ant> deleted = new ArrayList<>();
        int count;
        for (count = 0; !ants.isEmpty(); count++) {
            deleted = update(ants, length);
        }

        final int index = (deleted.size() == 2) ? (deleted.get(0).left ? deleted.get(0).index : deleted.get(1).index) : deleted.get(0).index;
        System.out.println(count + " " + index);
    }

    private List<Ant> update(final List<Ant> ants, final int length) {
        final Map<Integer, Ant> map = new HashMap<>();
        final ArrayList<Ant> outList = new ArrayList<>();
        for (final Ant ant : ants) {
            ant.position = ant.left ? ant.position - 1 : ant.position + 1;
            if (map.containsKey(ant.position)) {
                final Ant ant1 = map.get(ant.position);
                ant1.left = !ant1.left;
                ant.left = !ant.left;
            }
            map.put(ant.position, ant);
            if (ant.position == 0 || ant.position == length) {
                outList.add(ant);
            }
        }
        ants.removeAll(outList);
        return outList;
    }

    class Ant {
        int position;
        int index;
        boolean left;

        Ant(final String[] params, final int index) {
            this.position = Integer.parseInt(params[1]);
            this.left = params[0].equals("L");
            this.index = index;
        }
    }
}
