import java.util.*;
import java.awt.Color;

class Brain {
	private int[][] field;
	private int[][] tmpField;
	private int maxScore;
	private Species sp;
	private Set<Figure> visited;
	private LinkedList<String> maxMoves;
	private LinkedList<String> moves;
	
	public Brain(Species sp) {
		this.sp = sp;
		field = new int[20][10];
		tmpField = new int[20][10];
		visited = new HashSet<>();
		moves = new LinkedList<>();
	}
	
	public void setSpecies(Species sp) {
		this.sp = sp;
	}
	
	public String[] getFigureMoves(SquareBoard board, Figure f) {
		Color[][] matrix = board.getMatrix();
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				field[i][j] = (matrix[i][j] == null ? 0 : 1);
			}
		}

		log(Ut.boardToString(board));
		log("");
		
		// deleting current position from board to avoid some bugs
		int x, y;
		for (int i = 0; i < 4; i++) {
			x = f.xPos + f.getRelativeX(i, f.orientation);
			y = f.yPos + f.getRelativeY(i, f.orientation);
			if (y < 0) continue;
			field[y][x] = 0;
		}
		
		visited.clear();
		moves.clear();
		maxScore = -999999;
//		System.out.println("getMoves " + f.type + " " + f.xPos + " " + f.yPos + " " + f.orientation);
		checkMove(moves, f);
		return maxMoves.toArray(new String[maxMoves.size()]);
	}
	
	private void checkMove(LinkedList<String> moves, Figure f) {
//		System.out.println("+ " + f.xPos + " " + f.yPos + " " + f.orientation);
		if (visited.contains(f)) {
//			System.out.println("-visited");
			return;
		} else {
			visited.add(f);
		}
		
		if (canMoveTo(f, f.xPos, f.yPos + 1, f.orientation)) {
			// move down
			f.yPos += 1;
			moves.add("Down");
			checkMove(moves, f);
			moves.removeLast();
			f.yPos -= 1;
		}
		if (canMoveTo(f, f.xPos - 1, f.yPos, f.orientation)) {
			// move left
			f.xPos -= 1;
			moves.add("Left");
			checkMove(moves, f);
			moves.removeLast();
			f.xPos += 1;
		}
		if (canMoveTo(f, f.xPos + 1, f.yPos, f.orientation)) {
			// move right
			f.xPos += 1;
			moves.add("Right");
			checkMove(moves, f);
			moves.removeLast();
			f.xPos -= 1;
		}
		if (canMoveTo(f, f.xPos, f.yPos, (f.orientation + 1) % f.maxOrientation)) {
			// turn clockwise
			int oldOrientation = f.orientation;
			f.orientation =  (f.orientation + 1) % f.maxOrientation;
//			System.out.print("Turn ");
			moves.add("Turn");
			checkMove(moves, f);
			moves.removeLast();
			f.orientation = oldOrientation;
		}
		if (!canMoveTo(f, f.xPos, f.yPos + 1, f.orientation)) {
			// got a final position
			int score = getScore(f);
//			System.out.println("score: " + score);
			if (maxScore < score || (maxScore == score && maxMoves.size() > moves.size())) {
//				System.out.println("maxScore overcame " + f.xPos + " " + f.yPos + " " + f.orientation);
				maxScore = score;
				maxMoves = (LinkedList<String>) moves.clone();
			}
		}
//		System.out.println("-");
	}
	
	
	private int getScore(Figure f) {
		//System.out.println("getScore: " + f.xPos + " " + f.yPos + " " + f.orientation);
		int score = 0;
		// copy field to avoid destroying it when removing full lines
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 10; x++) {
				tmpField[y][x] = field[y][x];
			}
		}
		for (int i = 0; i < 4; i++) {
			if (f.yPos + f.getRelativeY(i, f.orientation) < 0) continue;
			tmpField[f.yPos + f.getRelativeY(i, f.orientation)][f.xPos + f.getRelativeX(i, f.orientation)] = 2;
		}
		
		// remove full lines
		boolean lineIsFull = true;
		for (int y = 19; y >= 0; y--) {
			lineIsFull = true;
			for (int x = 0; x < 10; x++) {
				if (tmpField[y][x] == 0) {
					lineIsFull = false;
					break;
				}
			}
			if (lineIsFull) {
				for (int y2 = y; y2 > 0; y2--) {
					for (int x2 = 0; x2 < 10; x2++) {
						tmpField[y2][x2] = field[y2 - 1][x2];
					}
				}
				for (int x = 0; x < 10; x++) {
					tmpField[0][x] = 0;
				}
			}
		}
		
		score += evaluateHoles();
		score += evaluateRoofs();
		score += evaluateSquares();
		score += evaluateCliffs();
		
		//System.out.println("x, y, orient, score: " + xPos + " " + yPos + " " + newOrientation + " " + score);
		return score;
	}
	
	
	
	private int evaluateHoles() {
		int score = 0;
		int holeHeight = 0;
		for (int x = 0; x < 10; x++) {
			for (int y = 19; y >= 0; y--) {
				if (tmpField[y][x] == 0) {
					holeHeight = 0;
					do {
						y--;
						holeHeight++;
					} while (y >= 0 && tmpField[y][x] == 0);
					if (y >= 0) {
						score -= sp.holePenalty * holeHeight;
					}
				}
			}
		}
		return score;
	}
	
	private int evaluateRoofs() {
		int score = 0;
		int roofHeight = 0;
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {
				if (tmpField[y][x] != 0) {
					roofHeight = 0;
					do {
						y++;
						roofHeight++;
					} while (y < 20 && tmpField[y][x] != 0);
					score -= sp.roofPenalty * roofHeight;
				}
			}
		}
		return score;
	}
	
	private int evaluateSquares() {
		int score = 0;
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 10; x++) {
				if (tmpField[y][x] != 0) {
					score += y - sp.squarePenalty; // every occupied square counts as -y points
				}
			}
		}
		return score;
	}
	
	private int evaluateCliffs() {
		int score = 0;
		int[] height = new int[10];
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {
				if (tmpField[y][x] != 0) {
					height[x] = 20 - y;
					break;
				}
			}
		}
		int cliffs = 0, dhLeft = 0, dhRight = 0, hdTotal = 0;
		if (height[1] - height[0] > 2) {
			cliffs++;
			hdTotal += height[1] - height[0];
		}
		if (height[8] - height[9] > 2) {
			cliffs++;
			hdTotal += height[8] - height[9];
		}
		for (int x = 1; x < 9; x++) {
			dhLeft = height[x - 1] - height[x];
			dhRight = height[x + 1] - height[x];
			if (dhLeft > 2 && dhRight > 2) {
				cliffs++;
				hdTotal += Math.min(dhLeft, dhRight);
			}
		}
		if (cliffs > 0) {
			score -= sp.cliffPenalty * hdTotal * cliffs;
		}
		return score;
	}
	
	private boolean canMoveTo(Figure f, int newX, int newY, int newOr) {
        int  x;
        int  y;

        for (int i = 0; i < 4; i++) {
            x = newX + f.getRelativeX(i, newOr);
            y = newY + f.getRelativeY(i, newOr);
            if (x < 0 || x > 9 || y < 0 || y > 19 || field[y][x] != 0) {
                return false;
            }
        }
        return true;
    }

    private void log(String msg) {
		System.out.println(msg);
	}

	
}
	