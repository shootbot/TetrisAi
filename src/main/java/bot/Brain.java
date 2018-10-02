package bot;

import game.*;
import genetic.*;

import java.awt.*;
import java.util.*;

import static game.Game.*;

public class Brain {
    private int[][] field = new int[20][10];
    private int[][] tmpField = new int[20][10];
    private int maxScore;
    private Species sp;
    private Set<Figure> visited = new HashSet<>();
    private LinkedList<String> maxMoves;
    private LinkedList<String> moves = new LinkedList<>();
    
    public Brain() {
    }
    
    public Brain(Species sp) {
        this.sp = sp;
    }
    
    public void setTmpField(int[][] tmpField) {
        this.tmpField = tmpField;
    }
    
    public void setSpecies(Species sp) {
        this.sp = sp;
    }
    
    public String[] getFigureMoves(SquareBoard board, Figure f) {
        Color[][] matrix = board.getMatrix();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                field[i][j] = (matrix[i][j] == null ? 0 : 1);
            }
        }
        
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
        checkMove(moves, f);
//        log("maxMoves: " + maxMoves.toString());
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
        if (canMoveTo(f, f.xPos, f.yPos, (f.orientation + 1) % f.maxOrientation)) {
            // turn clockwise
            int oldOrientation = f.orientation;
            f.orientation = (f.orientation + 1) % f.maxOrientation;
//			System.out.print("Turn ");
            moves.add("Rotate");
            checkMove(moves, f);
            moves.removeLast();
            f.orientation = oldOrientation;
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
        
        removeFullLines();
        
        score += evaluateHoles();
        score += evaluateRoofs();
        score += evaluateSquares();
        score += evaluateCliffs();
        
        //System.out.println("x, y, orient, score: " + xPos + " " + yPos + " " + newOrientation + " " + score);
        return score;
    }
    
    public void removeFullLines() {
        for (int y = HEIGHT - 1; y >= 0; y--) {
            boolean lineIsFull = true;
            for (int x = 0; x < WIDTH; x++) {
                if (tmpField[y][x] == 0) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                for (int y2 = y; y2 > 0; y2--) {
                    for (int x = 0; x < WIDTH; x++) {
                        tmpField[y2][x] = tmpField[y2 - 1][x];
                    }
                }
                for (int x = 0; x < WIDTH; x++) {
                    tmpField[0][x] = 0;
                }
                y++;
            }
        }
    }
    
    public int evaluateHoles() {
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
    
    public int evaluateRoofs() {
        int score = 0;
        for (int i = 1; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (tmpField[i][j] == 0 && tmpField[i - 1][j] == 1) {
                    int roofHeight = 0;
                    int k = i;
                    do {
                        k--;
                        roofHeight++;
                    } while (k > 0 && tmpField[k - 1][j] == 1);
                    score -= sp.roofPenalty * roofHeight;
                }
            }
        }
        return score;
    }
    
    public int evaluateSquares() {
        int score = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (tmpField[y][x] != 0) {
                    score += Math.abs(5 - x);
                    if (y < 10) {
                        int base = 10 - y;
                        int heightPenalty = base * base * base * base;
                        score -= heightPenalty;
                    }
                    score += y * y / 10;
                    score -= sp.squarePenalty;
                }
            }
        }
        return score;
    }
    
    public int evaluateCliffs() {
        int score = 0;
        // height of each column
        int[] height = new int[WIDTH];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (tmpField[y][x] != 0) {
                    height[x] = HEIGHT - y;
                    break;
                }
            }
        }
        
        int cliffs = 0, dhTotal = 0;
        // leftmost is cliff?
        if (height[1] - height[0] > 1) {
            cliffs++;
            dhTotal += height[1] - height[0];
        }
        
        // rightmost is cliff?
        if (height[WIDTH - 2] - height[WIDTH - 1] > 1) {
            cliffs++;
            dhTotal += height[WIDTH - 2] - height[WIDTH - 1];
        }
        
        for (int x = 1; x < WIDTH - 1; x++) {
            int dhLeft = height[x - 1] - height[x];
            int dhRight = height[x + 1] - height[x];
            if (dhLeft > 1 && dhRight > 1) {
                cliffs++;
                dhTotal += Math.min(dhLeft, dhRight);
            }
        }
        // todo need cliff total?
        score -= sp.cliffPenalty * dhTotal;
        return score;
    }
    
    private boolean canMoveTo(Figure f, int newX, int newY, int newOr) {
        int x;
        int y;
        
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
    
    public int[][] getTmpField() {
        return tmpField;
    }
}
