package bot;

import game.*;

import java.awt.*;

public class AiSquareBoard extends SquareBoard {
    
    /**
     * The board width (in squares)
     */
    private int  width = 0;
    
    /**
     * The board height (in squares).
     */
    private int  height = 0;
    
    /**
     * The square board color matrix. This matrix (or grid) contains
     * a color entry for each square in the board. The matrix is
     * indexed by the vertical, and then the horizontal coordinate.
     */
    private Color[][]  matrix = null;
    
    /**
     * The number of lines removed. This counter is increased each
     * time a line is removed from the board.
     */
    private int  removedLines = 0;
    
    
    public Color[][] getMatrix() {
        return matrix;
    }
    /**
     * Creates a new square board with the specified size. The square
     * board will initially be empty.
     *
     * @param width     the width of the board (in squares)
     * @param height    the height of the board (in squares)
     */
    public AiSquareBoard(int width, int height) {
        super(width, height);
    }
    
    /**
     * Checks if a specified square is empty, i.e. if it is not
     * marked with a color. If the square is outside the board,
     * false will be returned in all cases except when the square is
     * directly above the board.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     *
     * @return true if the square is emtpy, or
     *         false otherwise
     */
    public boolean isSquareEmpty(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return x >= 0 && x < width && y < 0;
        } else {
            return matrix[y][x] == null;
        }
    }
    
    /**
     * Checks if a specified line is empty, i.e. only contains
     * empty squares. If the line is outside the board, false will
     * always be returned.
     *
     * @param y         the vertical position (0 <= y < height)
     *
     * @return true if the whole line is empty, or
     *         false otherwise
     */
    public boolean isLineEmpty(int y) {
        if (y < 0 || y >= height) {
            return false;
        }
        for (int x = 0; x < width; x++) {
            if (matrix[y][x] != null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if a specified line is full, i.e. only contains no empty
     * squares. If the line is outside the board, true will always be
     * returned.
     *
     * @param y         the vertical position (0 <= y < height)
     *
     * @return true if the whole line is full, or
     *         false otherwise
     */
    public boolean isLineFull(int y) {
        if (y < 0 || y >= height) {
            return true;
        }
        for (int x = 0; x < width; x++) {
            if (matrix[y][x] == null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the board contains any full lines.
     *
     * @return true if there are full lines on the board, or
     *         false otherwise
     */
    public boolean hasFullLines() {
        for (int y = height - 1; y >= 0; y--) {
            if (isLineFull(y)) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Returns the board height (in squares). This method returns,
     * i.e, the number of vertical squares that fit on the board.
     *
     * @return the board height in squares
     */
    public int getBoardHeight() {
        return height;
    }
    
    /**
     * Returns the board width (in squares). This method returns, i.e,
     * the number of horizontal squares that fit on the board.
     *
     * @return the board width in squares
     */
    public int getBoardWidth() {
        return width;
    }
    
    /**
     * Returns the number of lines removed since the last clear().
     *
     * @return the number of lines removed since the last clear call
     */
    public int getRemovedLines() {
        return removedLines;
    }
    
    /**
     * Returns the color of an individual square on the board. If the
     * square is empty or outside the board, null will be returned.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     *
     * @return the square color, or null for none
     */
    public Color getSquareColor(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        } else {
            return matrix[y][x];
        }
    }
    
    /**
     * Changes the color of an individual square on the board. The
     * square will be marked as in need of a repaint, but the
     * graphical component will NOT be repainted until the update()
     * method is called.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * @param color     the new square color, or null for empty
     */
    public void setSquareColor(int x, int y, Color color) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        matrix[y][x] = color;
    }
    
    /**
     * Clears the board, i.e. removes all the colored squares. As
     * side-effects, the number of removed lines will be reset to
     * zero, and the component will be repainted immediately.
     */
    public void clear() {
        removedLines = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.matrix[y][x] = null;
            }
        }
    }
    
    /**
     * Removes all full lines. All lines above a removed line will be
     * moved downward one step, and a new empty line will be added at
     * the top. After removing all full lines, the component will be
     * repainted.
     *
     * @see #hasFullLines
     */
    public void removeFullLines() {
        // Remove full lines
        for (int y = height - 1; y >= 0; y--) {
            if (isLineFull(y)) {
                removeLine(y);
                removedLines++;
                y++;
            }
        }
    }
    
    /**
     * Removes a single line. All lines above are moved down one step,
     * and a new empty line is added at the top. No repainting will be
     * done after removing the line.
     *
     * @param y         the vertical position (0 <= y < height)
     */
    private void removeLine(int y) {
        if (y < 0 || y >= height) {
            return;
        }
        for (; y > 0; y--) {
            for (int x = 0; x < width; x++) {
                matrix[y][x] = matrix[y - 1][x];
            }
        }
        for (int x = 0; x < width; x++) {
            matrix[0][x] = null;
        }
    }
    
    public void setMessage(String msg) {}
}
