import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EvaluateFunctionsTest {
	static int[][] field;
	static Brain brain = new Brain(new Species(10, 10, 10, 10));
	static String board1, board2, board3, board4;

	@BeforeAll
	static void init() {
		board1 =
				".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						"..X......." +
						"XXXX......" +
						"XXXXX...X." +
						"XXXX..X.XX" +
						"XXXXXXX.XX" +
						"X.XXXXXXX." +
						"XXXXXXXXXX" +
						"XXXXXXXXXX" +
						".XXXXXXXX." +
						".XXXXXXXX." +
						".XXXXXXXXX" +
						".XXXXXXXXX" +
						".XXXXXXXXX" +
						"XXXXXXXXXX" +
						"XXXXXXXXXX";

		board2 =
				".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						"X........." +
						"X........." +
						"X........." +
						"XX........" +
						"X.X......." +
						"XXXXXX....";

		board3 =
				".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".XX..X..X." +
						"..X.XXX.X." +
						".XX.XXXXX." +
						".XXXXXXXX." +
						".XXXXXXXX." +
						".XXX.XXXXX" +
						".XXX.XXXXX" +
						".XXX.XXXXX" +
						".XXX.XXXXX" +
						"XXXXXXXXXX" +
						"XXXXXXXXXX";

		board4 =
				".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".XX..X..X." +
						"..X.X.X.X." +
						".XX.XXXXX." +
						".XXXXXXXX." +
						".XXXXXXXX." +
						".XXX.XXXXX" +
						".XXX.XXXXX" +
						".XXX.XXXXX" +
						".XXX.XXXXX" +
						"X.XXXXXXXX" +
						"X.XX.XXXXX";


	}

	@Test
	void roofs() {

		field = Ut.toField(board1);
		brain.setTmpField(field);

		int score = brain.evaluateRoofs();
		System.out.println(score);

		Assertions.assertEquals(-16 * 10, score);
	}

	@Test
	void squares() {
		field = Ut.toField(board2);
		brain.setTmpField(field);

		int score = brain.evaluateSquares();
		System.out.println(score);

		int expected = 19 * 6 + 18 * 2 + 17 * 2 + 16 + 15 + 14 - 13 * 10;
		Assertions.assertEquals(expected, score);
	}

	@Test
	void cliffs() {
		field = Ut.toField(board3);
		brain.setTmpField(field);

		int score = brain.evaluateCliffs();
		System.out.println(score);

		int expected = -16 * 10;
		Assertions.assertEquals(expected, score);
	}

	@Test
	void holes() {

		field = Ut.toField(board4);
		brain.setTmpField(field);

		int score = brain.evaluateHoles();
		System.out.println(score);

		int expected = -9 * 10;
		Assertions.assertEquals(expected, score);
	}
}
