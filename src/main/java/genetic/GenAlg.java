package genetic;

import bot.*;

import java.util.*;

public class GenAlg {
    private Species[] species;
    private static Random rng = new Random();
    
    private AiGame game = new AiGame();
    private static final int SPECIES_NUM = 8;
    private static final int TESTS_NUM = 3;
    private static final int GENERATIONS_NUM = 20;
    public static final int HOLE_P_MAX = 1000;
    public static final int HOLE_P_MIN = -1000;
    public static final int ROOF_P_MAX = 1000;
    public static final int ROOF_P_MIN = -1000;
    public static final int SQUARE_P_MAX = 1000;
    public static final int SQUARE_P_MIN = -1000;
    public static final int CLIFF_P_MAX = 1000;
    public static final int CLIFF_P_MIN = -1000;
    
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
        
        for (int i = 0; i < GENERATIONS_NUM; i++) {
            test();
            crossover();
            mutation();
        }
        
        test();
        System.out.println("winner: " + species[0]);
    }
    
    private void test() {
        for (int i = 0; i < SPECIES_NUM; i++) {
            System.out.println("Testing " + species[i]);
            double score = playGame(species[i], TESTS_NUM);
            species[i].score = score;
            System.out.println("Avg score: " + score);
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
            species[i].holePenalty += (HOLE_P_MAX - HOLE_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
            species[i].roofPenalty += (ROOF_P_MAX - ROOF_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
            species[i].squarePenalty += (SQUARE_P_MAX - SQUARE_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
            species[i].cliffPenalty += (CLIFF_P_MAX - CLIFF_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
            
            species[i].normalize();
            
            if (species[i].holePenalty > HOLE_P_MAX) species[i].holePenalty = HOLE_P_MAX;
            if (species[i].holePenalty < HOLE_P_MIN) species[i].holePenalty = HOLE_P_MIN;
            if (species[i].holePenalty > ROOF_P_MAX) species[i].holePenalty = ROOF_P_MAX;
            if (species[i].holePenalty < ROOF_P_MIN) species[i].holePenalty = ROOF_P_MIN;
            if (species[i].holePenalty > SQUARE_P_MAX)
                species[i].holePenalty = SQUARE_P_MAX;
            if (species[i].holePenalty < SQUARE_P_MIN)
                species[i].holePenalty = SQUARE_P_MIN;
            if (species[i].holePenalty > CLIFF_P_MAX)
                species[i].holePenalty = CLIFF_P_MAX;
            if (species[i].holePenalty < CLIFF_P_MIN)
                species[i].holePenalty = CLIFF_P_MIN;
        }
    }
}

