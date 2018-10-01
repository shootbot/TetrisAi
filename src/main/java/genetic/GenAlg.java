package genetic;

import game.*;

import java.beans.*;
import java.util.Arrays;
import java.util.Random;

public class GenAlg implements PropertyChangeListener {
	private Species[] species;
	private static Random rng =new Random();;
	private Game game;
	private static int SPECIES_NUM = 8;
	private static int TESTS_NUM = 5;
	private static int EVOLUTIONS_NUM = 100;
	public static int HOLE_P_MAX = 1000;
	public static int HOLE_P_MIN = -1000;
	public static int ROOF_P_MAX = 1000;
	public static int ROOF_P_MIN = -1000;
	public static int SQUARE_P_MAX = 1000;
	public static int SQUARE_P_MIN = -1000;
	public static int CLIFF_P_MAX = 1000;
	public static int CLIFF_P_MIN = -1000;
    
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("result")) {
            messageBox((String) evt.getNewValue(), "Game ended");
        } else {
            f.repaint();
        }
    }
	
	public GenAlg() {
		species = new Species[SPECIES_NUM];
		for (int i = 0; i < SPECIES_NUM; i++) {
			species[i] = new Species();
		}
	}
	
	public static void main(String[] args) {
		GenAlg ga = new GenAlg();
		ga.doEvolution();
	}
	
	public void doEvolution() {
		game = Game.newAiGame();
		for (int i = 0; i < EVOLUTIONS_NUM; i++) {
			test(game);
			crossover();
			mutation();
		}
		
		test(game);
		System.out.println("winner: " + species[0]);
	}
	
	private void test(Game game) {
		for (int i = 0; i < SPECIES_NUM; i++) {
			double totalScore = 0;
			for (int j = 0; j < TESTS_NUM; j++) {
				double score = playGame(species[i]);
				do {
					try {
						Thread.sleep(50);
					} catch (InterruptedException ignore) {
						// Do nothing
					}
				} while (gameInProgress);
				totalScore += score;
				System.out.println("test #" + j + ": " + species[i] + " score: " + score);
			}
			species[i].score = totalScore / TESTS_NUM;
		}
		Arrays.sort(species);
	}
	
	private void playGame(Species sp) {
	   game.setSpecies(sp);
	   game.start();
    }
	
	private void crossover() {
		// preserve 0, 1, 2, 3
        // mix 0, 1, 2, 3, 4, 5
        // dont use 6, 7
		Species s4 = species[0].pair(species[1]);
        Species s5 = species[2].pair(species[3]);
        Species s6 = species[0].pair(species[5]);
        Species s7 = species[4].pair(species[1]);
        
        species[4] = s4;
        species[5] = s5;
        species[6] = s6;
        species[7] = s7;
	}
	
	private void mutation() {
		for (int i = 0; i < SPECIES_NUM; i++) {
			species[i].holePenalty += (HOLE_P_MAX - HOLE_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
			species[i].roofPenalty += (ROOF_P_MAX - ROOF_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
			species[i].squarePenalty += (SQUARE_P_MAX - SQUARE_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
			species[i].cliffPenalty += (CLIFF_P_MAX - CLIFF_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
            
            species[i].normalize();
			
			if (species[i].holePenalty > HOLE_P_MAX) species[i].holePenalty = HOLE_P_MAX;
			if (species[i].holePenalty < HOLE_P_MIN) species[i].holePenalty = HOLE_P_MIN;
			if (species[i].holePenalty > ROOF_P_MAX) species[i].holePenalty = ROOF_P_MAX;
			if (species[i].holePenalty < ROOF_P_MIN) species[i].holePenalty = ROOF_P_MIN;
			if (species[i].holePenalty > SQUARE_P_MAX) species[i].holePenalty = SQUARE_P_MAX;
			if (species[i].holePenalty < SQUARE_P_MIN) species[i].holePenalty = SQUARE_P_MIN;
			if (species[i].holePenalty > CLIFF_P_MAX) species[i].holePenalty = CLIFF_P_MAX;
			if (species[i].holePenalty < CLIFF_P_MIN) species[i].holePenalty = CLIFF_P_MIN;
		}
	}
}

