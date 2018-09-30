import java.awt.*;

/**
 * Utility class
 */
public class Ut {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 20;

	public static String boardToString(SquareBoard board) {
		StringBuilder sb = new StringBuilder();
		Color[][] field = board.getMatrix();
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				char c = field[i][j] == null ? '.' : 'X';
				sb.append(c);
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
