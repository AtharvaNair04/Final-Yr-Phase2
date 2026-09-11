import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Main {

	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 1);

	private String inputData = null;

	public String[] data = null;

	public short num = 0;

	public short count = 0;

	public HashMap<Short, Short> humanData = new HashMap<Short, Short>();

	public ArrayList<Short> topId = new ArrayList<Short>();

	public ArrayList<Short> topLength = new ArrayList<Short>();

	public static void main(String[] args) {
		new Main().start();
		System.exit(0);
	}

	public void start() {
		while (true) {
			try {
				this.inputData = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.data = this.inputData.split(" ");

			if (this.data.length == 1) {
				if (this.data[0].equals("0")) {
					break;
				}
				try {
					this.num = Short.parseShort(this.data[0]);
				} catch (Exception e) {
					continue;
				}

			}

			this.humanData.clear();

			for (int i = 0; i < this.num; i++) {
				try {
					this.inputData = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (this.inputData != null) {
					this.data = this.inputData.split(" ");
				}

				this.humanData.put(Short.parseShort(data[0]),
						(short) (Short.parseShort(data[1]) + Short.parseShort(data[2])));

			}

			Iterator<Short> itId = this.humanData.keySet().iterator();
			int numCount = 0;
			while (itId.hasNext()) {
				short id = itId.next();
				if (numCount == 0) {
					this.topId.add(id);
					this.topLength.add(this.humanData.get(id));
				} else {
					if (this.topLength.get(count) <= this.humanData.get(id)) {
						this.topId.set(this.count, id);
						this.topLength.set(this.count, this.humanData.get(id));
					}
				}
				numCount++;
			}

			this.count++;

		}

		for (int i = 0; i < this.topId.size(); i++) {
			System.out.println(this.topId.get(i) + " " + this.topLength.get(i));
		}

	}

}
