import game.*;

import java.awt.*;

import static game.Game.*;

/**
 * Utility class
 */
public class Ut {

	private static String boardToString(SquareBoard board, boolean withNewLines) {
		StringBuilder sb = new StringBuilder();
		Color[][] field = board.getMatrix();
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				char c = field[i][j] == null ? '.' : 'X';
				sb.append(c);
			}
			if (withNewLines) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	public static String boardToString(SquareBoard board) {
		return boardToString(board, false);
	}

	public static String boardToStringWithNewLines(SquareBoard board) {
		return boardToString(board, true);
	}

	public static int[][] toField(String board) {
		int[][] field = new int[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				field[i][j] = board.charAt(i * WIDTH + j) == 'X' ? 1 : 0;
			}
		}
		return field;
	}

	public static String fieldToString(int[][] field) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				char c = field[i][j] == 0 ? '.' : 'X';
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String addNewLines(String board) {
		char[] newBoard = new char[220];
		int curIndex = 0;
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				newBoard[curIndex++] = board.charAt(i * WIDTH + j);
			}
			newBoard[curIndex++] = '\n';
		}
		return String.valueOf(newBoard);
	}

	public static void log(String s) {
		System.out.println(s);
	}
}
