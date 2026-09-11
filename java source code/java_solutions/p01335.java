import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int[][] basePoints = new int[4][13];
		int[] rates = new int[9];

		Map<Character, Integer> cardMap = new HashMap<Character, Integer>() {
			{
				put('A', 0);
				put('2', 1);
				put('3', 2);
				put('4', 3);
				put('5', 4);
				put('6', 5);
				put('7', 6);
				put('8', 7);
				put('9', 8);
				put('T', 9);
				put('J', 10);
				put('Q', 11);
				put('K', 12);
			}
		};
		Map<Character, Integer> markMap = new HashMap<Character, Integer>() {
			{
				put('S', 0);
				put('C', 1);
				put('H', 2);
				put('D', 3);
			}
		};

		while (true) {
			int n = sc.nextInt();
			if (n == 0) {
				break;
			}

			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 13; j++) {
					basePoints[i][j] = sc.nextInt();
				}
			}
			for (int i = 0; i < 9; i++) {
				rates[i] = sc.nextInt();
			}

			for (int i = 0; i < n; i++) {
				Card[] cards = new Card[5];
				int basePoint = 0;
				Set<Integer> marks = new HashSet<Integer>();
				Set<Integer> numbers = new HashSet<Integer>();
				for (int j = 0; j < 5; j++) {
					String cardString = sc.next();
					cards[j] = new Card(cardMap.get(cardString.charAt(0)),
							markMap.get(cardString.charAt(1)));
					marks.add(cards[j].type);
					numbers.add(cards[j].number);
					basePoint += basePoints[cards[j].type][cards[j].number];
				}

				Yaku yaku = resolveRate(cards, marks, numbers);
				int rate = yaku == null ? 0 : rates[yaku.ordinal()];
				System.out.println(basePoint * rate);

			}

			if (!sc.hasNextInt()) {
				break;
			}
			else {
				System.out.println();
			}
		}

	}

	static Yaku resolveRate(Card[] cards, Set<Integer> marks,
			Set<Integer> numbers) {
		Arrays.sort(cards);

		// tbV
		boolean isFlush = marks.size() == 1;

		// straight
		boolean isStraight = true;
		for (int i = 1; i < 5; i++) {
			if (cards[i - 1].number + 1 != cards[i].number
					&& (i != 1 || cards[0].number != 0 || cards[1].number != 9)) {
				isStraight = false;
				break;
			}
		}

		if (isFlush && isStraight) {
			if (cards[0].number == 0 && cards[4].number == 12) {
				return Yaku.ROYAL;
			}
			else {
				return Yaku.STRAIGHT_FLUSH;
			}
		}
		if (isStraight) {
			return Yaku.STRAIGHT;
		}
		if (isFlush) {
			return Yaku.FLUSH;
		}
		if (numbers.size() == 2) {
			int numEqualToFirst = 0;
			for (int j = 1; j < 5; j++) {
				if (cards[0].number == cards[j].number) {
					numEqualToFirst++;
				}
			}
			if (numEqualToFirst == 0 || numEqualToFirst == 3) {
				return Yaku.FOUR_PAIR;
			}
			else {
				return Yaku.FULLHAUSE;
			}
		}
		if (numbers.size() == 3) {
			for (int j = 0; j < 5; j++) {
				int numCount = 0;
				for (int k = 0; k < 5; k++) {
					if (cards[j].number == cards[k].number)
						numCount++;

				}
				if (numCount == 3) {
					return Yaku.THREE_PAIR;
				}
			}
			return Yaku.TWO_PAIR;
		}
		if (numbers.size() == 4) {
			return Yaku.ONE_PAIR;
		}

		return null;

	}

	static class Card implements Comparable<Card> {
		final int number;
		final int type;

		public Card(int number, int type) {
			super();
			this.number = number;
			this.type = type;
		}

		@Override
		public int compareTo(Card o) {
			if (number != o.number)
				return number - o.number;
			return type - o.type;
		}

	}

}

enum Yaku {
	ONE_PAIR, TWO_PAIR, THREE_PAIR, STRAIGHT, FLUSH, FULLHAUSE, FOUR_PAIR, STRAIGHT_FLUSH, ROYAL;
}
