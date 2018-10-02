package bot;/*
 * @(#)game.Game.java
 *
 * This work is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */
// ����� ������ �����
// �� ������ �������� ����� �������� ������ � ������ ������������ (������� "�����")
// ������� ����� ������� �����
// �� �������� �������� �� �������� "���������"
// ��� ����� ������� �������, �� ����� �������� � �����
// �� ������ � �������� ������

import game.*;
import genetic.*;

import java.util.*;

/**
 * The Tetris game. This class controls all events in the game and
 * handles all the game logics. The game is started through user
 * interaction with the graphical game component provided by this
 * class.
 *
 * @author Per Cederberg, per@percederberg.net
 * @version 1.2
 */
public class AiGame {
    
    private boolean positionChosen = false; // true if AI has chosen place for current figure
    private Brain brain; // AI
    private Random rng = new Random();
    
    /**
     * The main square board. This board is used for the game itself.
     */
    private AiSquareBoard board;
    
    /**
     * The figures used on both boards. All figures are reutilized in
     * order to avoid creating new objects while the game is running.
     * Special care has to be taken when the preview figure and the
     * current figure refers to the same object.
     */
    private Figure[] figures = {
            new Figure(Figure.SQUARE_FIGURE),
            new Figure(Figure.LINE_FIGURE),
            new Figure(Figure.S_FIGURE),
            new Figure(Figure.Z_FIGURE),
            new Figure(Figure.RIGHT_ANGLE_FIGURE),
            new Figure(Figure.LEFT_ANGLE_FIGURE),
            new Figure(Figure.TRIANGLE_FIGURE)
    };
    
    /**
     * The current score. The score is increased for every figure that
     * is possible to place on the main board.
     */
    private int score = 0;
    
    /**
     * The current figure. The figure will be updated when
     */
    private volatile Figure figure = null;
    
    /**
     * The next figure.
     */
    private volatile Figure nextFigure = null;
    
    /**
     * The rotation of the next figure.
     */
    private int nextRotation = 0;
    private int numberOfGames;
    private int totalScore;
    private boolean gameEnded = true;
    
    /**
     * Creates a new Tetris game. The square board will be given
     * the default size of 10x20.
     */
    public AiGame() {
        this(10, 20);
    }
    
    /**
     * Creates a new Tetris game. The square board will be given
     * the specified size.
     *
     * @param width  the width of the square board (in positions)
     * @param height the height of the square board (in positions)
     */
    public AiGame(int width, int height) {
        brain = new Brain();
        board = new AiSquareBoard(width, height);
    }
    
    public int play(Species sp, int numberOfGames) {
        brain.setSpecies(sp);
//        ExecutorService es = Executors.newFixedThreadPool(1);
        this.numberOfGames = numberOfGames;
        runGame();
        return totalScore;
    }
    
    private void runGame() {
        totalScore = 0;
        while (numberOfGames > 0) {
            handleTimer();
        }
    }
    
    /**
     * Handles a game start event. Both the main and preview square
     * boards will be reset, and all other game parameters will be
     * reset. Finally the game thread will be launched.
     */
    private void handleStart() {
        gameEnded = false;
        score = 0;
        figure = null;
        nextFigure = randomFigure();
        positionChosen = false;
        nextRotation = nextFigure.getRotation();
        board.clear();
    }
    
    /**
     * Handles a game over event. This will stop the game thread,
     * reset all figures and print a game over message.
     */
    private void handleGameOver() {
        
        // Reset figures
        if (figure != null) {
            figure.detach();
        }
        figure = null;
        if (nextFigure != null) {
            nextFigure.detach();
        }
        nextFigure = null;
        
        System.out.println("Game ended: " + score);
        totalScore += score;
        
        numberOfGames--;
        gameEnded = true;
    }
    
    /**
     * Handles a figure start event. This will move the next figure
     * to the current figure position, while also creating a new
     * preview figure. If the figure cannot be introduced onto the
     * game board, a game over event will be launched.
     */
    private void handleFigureStart() {
        int rotation;
        
        // Move next figure to current
        figure = nextFigure;
        rotation = nextRotation;
        nextFigure = randomFigure();
        //nextFigure.rotateRandom();
        nextRotation = nextFigure.getRotation();
        
        // Attach figure to game board
        figure.setRotation(rotation);
        if (!figure.attach(board, false)) {
            figure.detach();
            handleGameOver();
        }
    }
    
    /**
     * Handles a figure landed event. This will check that the figure
     * is completely visible, or a game over event will be launched.
     * After this control, any full lines will be removed. If no full
     * lines could be removed, a figure start event is launched
     * directly.
     */
    private void handleFigureLanded() {
        // Check and detach figure
        if (figure.isAllVisible()) {
            score += 10;
        } else {
            handleGameOver();
            return;
        }
        figure.detach();
        figure = null;
        
        // Check for full lines or create new figure
        if (board.hasFullLines()) {
            board.removeFullLines();
        } else {
            handleFigureStart();
        }
    }
    
    /**
     * Handles a timer event. This will normally move the figure down
     * one step, but when a figure has landed or isn't ready other
     * events will be launched. This method is synchronized to avoid
     * race conditions with other asynchronous events (keyboard and
     * mouse).
     */
    private synchronized void handleTimer() {
        if (gameEnded) {
            handleStart();
        } else if (figure == null) {
            handleFigureStart();
        } else if (figure.hasLanded() && positionChosen) {
            handleFigureLanded();
            positionChosen = false;
        } else if (!positionChosen) {
            String[] moves = brain.getFigureMoves(board, figure);
            moveFigure(moves);
            positionChosen = true;
        }
    }
    
    private void moveFigure(String[] moves) {
        for (String move : moves) {
            switch (move) {
                case "Down":
                    figure.moveDown();
                    break;
                case "Left":
                    figure.moveLeft();
                    break;
                case "Right":
                    figure.moveRight();
                    break;
                case "Rotate":
                    figure.rotateClockwise();
                    break;
            }
        }
    }
    
    /**
     * Returns a random figure. The figures come from the figures
     * array, and will not be initialized.
     *
     * @return a random figure
     */
    private Figure randomFigure() {
        return figures[rng.nextInt(figures.length)];
    }
}



