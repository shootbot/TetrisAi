import bot.*;
import genetic.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Erase {

	@Test
	void eraseLines() {

		Brain brain = new Brain(new Species(10, 10, 10, 10));
		String board =
				".........." +
				".........." +
				".........." +
				".........." +
				".........." +
				"..X......." +
				"XXXX......" +
				"XXXXX...X." +
				"XXXXX.X.XX" +
				"XXXXXXX.XX" +
				"X.XXXXXXXX" +
				"XXXXXXXXXX" +
				"XXXXXXXXXX" +
				".XXXXXXXX." +
				".XXXXXXXXX" +
				".XXXXXXXXX" +
				".XXXX.XXXX" +
				"XXXXXXXXXX" +
				"XXXXXX.XXX" +
				"XXXXXXX.XX";

		String expectedBoard =
				".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						".........." +
						"..X......." +
						"XXXX......" +
						"XXXXX...X." +
						"XXXXX.X.XX" +
						"XXXXXXX.XX" +
						"X.XXXXXXXX" +
						".XXXXXXXX." +
						".XXXXXXXXX" +
						".XXXXXXXXX" +
						".XXXX.XXXX" +
						"XXXXXX.XXX" +
						"XXXXXXX.XX";

		int[][] field = Ut.toField(board);
		brain.setTmpField(field);
		brain.removeFullLines();

		String res = Ut.fieldToString(brain.getTmpField());
		System.out.println(Ut.addNewLines(expectedBoard));
		System.out.println(Ut.addNewLines(res));
		Assertions.assertEquals(expectedBoard, res);


	}
}
