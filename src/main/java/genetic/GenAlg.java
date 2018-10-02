package genetic;

import bot.*;

import java.util.*;

public class GenAlg {
	private static final int SPECIES_NUM = 8;
	private static final int TESTS_NUM = 5;
	private static final int GENERATIONS_NUM = 30;

	public static final int HOLE_P_MAX = 1000;
	public static final int HOLE_P_MIN = -1000;
	public static final int ROOF_P_MAX = 1000;
	public static final int ROOF_P_MIN = -1000;
	public static final int SQUARE_P_MAX = 1000;
	public static final int SQUARE_P_MIN = -1000;
	public static final int CLIFF_P_MAX = 1000;
	public static final int CLIFF_P_MIN = -1000;

    private Species[] species = new Species[SPECIES_NUM];
    private static Random rng = new Random();
    private AiGame game = new AiGame();

    
    public GenAlg() {
        for (int i = 0; i < SPECIES_NUM; i++) {
            species[i] = new Species();
        }
    }
    
    public static void main(String[] args) {
    	long startTime = System.nanoTime();
        GenAlg ga = new GenAlg();
        ga.doEvolution();
		System.out.println((System.nanoTime() - startTime) / 1000_000_000L);
    }
    
    public void doEvolution() {
        for (int i = 0; i < GENERATIONS_NUM; i++) {
            testAndSort();
            crossover();
            mutation();
        }
        
        testAndSort();
        Species winner = species[0];
        System.out.println("Winner " + winner.score + ": " + winner);
    }
    
    private void testAndSort() {
        for (int i = 0; i < SPECIES_NUM; i++) {
            System.out.println("Testing " + species[i]);
            double score = playGame(species[i], TESTS_NUM);
            species[i].score = score;
            System.out.println("|Avg score: " + score);
        }
        Arrays.sort(species);
    }
    
    private double playGame(Species sp, int numberOfGames) {

        int totalScore = game.play(sp, numberOfGames);
        return (double) totalScore / numberOfGames;
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
            species[i].normalize();
        }
    }
}

