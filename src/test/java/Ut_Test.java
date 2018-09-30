import org.junit.jupiter.api.Test;

public class Ut_Test {
	@Test
	void toBoolField() {
		String board =
				".....X...." +
				".....X...." +
				".....XX..." +
				".........." +
				".........." +
				"..X......." +
				"XXXX......" +
				"XXXXX...X." +
				"XXXXX.X.XX" +
				"XXXXXXX.XX" +
				"X.XXXXXXXX" +
				"XXXX.XXXXX" +
				"XXX.XXXXXX" +
				".XXXXXXXX." +
				".XXXXXXXXX" +
				".XXXXXXXXX" +
				".XXXX.XXXX" +
				".XXXXXXXXX" +
				"XXXXXX.XXX" +
				"XXXXXXX.XX";

		int[][] field = Ut.toField(board);
		field[5][6] = 5;
	}
}
