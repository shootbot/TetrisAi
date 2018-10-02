package game;/*
 * @(#)game.Main.java
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

import java.applet.Applet;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main class of the Tetris game. This class contains the
 * necessary methods to run the game either as a stand-alone
 * application or as an applet inside a web page.
 *
 * @version  1.2
 * @author   Per Cederberg, per@percederberg.net
 */
public class Main extends Applet {

    /**
     * The applet parameter information structure.
     */
    private static final String PARAMETER[][] = {
        { "tetris.color.background", "color",
            "The overall background color." },
        { "tetris.color.label", "color",
            "The text color of the labels." },
        { "tetris.color.button", "color",
            "The start and pause button bolor." },
        { "tetris.color.board.background", "color",
            "The background game board color." },
        { "tetris.color.board.message", "color",
            "The game board message color." },
        { "tetris.color.figure.square", "color",
            "The color of the square figure." },
        { "tetris.color.figure.line", "color",
            "The color of the line figure." },
        { "tetris.color.figure.s", "color",
            "The color of the 's' curved figure." },
        { "tetris.color.figure.z", "color",
            "The color of the 'z' curved figure." },
        { "tetris.color.figure.right", "color",
            "The color of the right angle figure." },
        { "tetris.color.figure.left", "color",
            "The color of the left angle figure." },
        { "tetris.color.figure.triangle", "color",
            "The color of the triangle figure." }
    };

    /**
     * The Tetris game being played (in applet mode).
     */
    private Game game = null;

    /**
     * The stand-alone main routine.
     *
     * @param args      the command-line arguments
     */
    public static void main(String[] args) {
        Frame frame = new Frame("Tetris");
        Game game = new Game();

        frame.add(game.getComponent());
        frame.pack();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Show frame (and start game)
        frame.show();
    }

    /**
     * Returns information about the parameters that are understood by
     * this applet.
     *
     * @return an array describing the parameters to this applet
     */
    public String[][] getParameterInfo() {
        return PARAMETER;
    }

    /**
     * Stops the game in applet mode.
     */
    public void stop() {
        game.quit();
    }
    

}
