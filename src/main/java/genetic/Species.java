package genetic;

import java.util.*;

import static genetic.Evolution.*;

public class Species implements Comparable {
    static final double MUTATION_FACTOR = 0.1;

    public double score;
    public int holePenalty;
    public int roofPenalty;
    public int squarePenalty;
    public int cliffPenalty;
    
    private static final Random rng = new Random();
    
    public Species() {
        this.holePenalty = genRandom(HOLE_P_MIN, HOLE_P_MAX);
        this.roofPenalty = genRandom(Evolution.ROOF_P_MIN, Evolution.ROOF_P_MAX);
        this.squarePenalty = genRandom(Evolution.SQUARE_P_MIN, Evolution.SQUARE_P_MAX);
        this.cliffPenalty = genRandom(Evolution.CLIFF_P_MIN, Evolution.CLIFF_P_MAX);
    }
    
    public Species(int holePenalty, int roofPenalty, int squarePenalty, int cliffPenalty) {
        this.holePenalty = holePenalty;
        this.roofPenalty = roofPenalty;
        this.squarePenalty = squarePenalty;
        this.cliffPenalty = cliffPenalty;
    }
    
    private int genRandom(int min, int max) {
        return min + rng.nextInt(max - min + 1);
    }
    
    public Species pair(Species sp2) {
        int resHolePenalty = (holePenalty + sp2.holePenalty) / 2;
        int resRoofPenalty = (roofPenalty + sp2.roofPenalty) / 2;
        int resSquarePenalty = (squarePenalty + sp2.squarePenalty) / 2;
        int resCliffPenalty = (cliffPenalty + sp2.cliffPenalty) / 2;
        return new Species(resHolePenalty, resRoofPenalty, resSquarePenalty, resCliffPenalty);
    }

    @Override
    public int compareTo(Object ob) {
        Species other = (Species) ob;
        return -Double.compare(score, other.score);
    }

	@Override
    public String toString() {
        String answer = "Species{hole: " + holePenalty
                + ", roof: " + roofPenalty
                + ", square: " + squarePenalty
                + ", cliff: " + cliffPenalty
                + "}";
        return answer;
    }
    
    public void mutate() {
        holePenalty += getAdjustment(HOLE_P_MIN, HOLE_P_MAX);
        roofPenalty += getAdjustment(ROOF_P_MIN, ROOF_P_MAX);
        squarePenalty += getAdjustment(SQUARE_P_MIN, SQUARE_P_MAX);
        cliffPenalty += getAdjustment(CLIFF_P_MIN, CLIFF_P_MAX);
    
//        if (holePenalty > HOLE_P_MAX) holePenalty = HOLE_P_MAX;
//        if (holePenalty < HOLE_P_MIN) holePenalty = HOLE_P_MIN;
//        if (roofPenalty > ROOF_P_MAX) roofPenalty = ROOF_P_MAX;
//        if (roofPenalty < ROOF_P_MIN) roofPenalty = ROOF_P_MIN;
//        if (squarePenalty > SQUARE_P_MAX) squarePenalty = SQUARE_P_MAX;
//        if (squarePenalty < SQUARE_P_MIN) squarePenalty = SQUARE_P_MIN;
//        if (cliffPenalty > CLIFF_P_MAX) cliffPenalty = CLIFF_P_MAX;
//        if (cliffPenalty < CLIFF_P_MIN) cliffPenalty = CLIFF_P_MIN;
    }
    
    private int getAdjustment(int min, int max) {
        return (int)((max - min) * MUTATION_FACTOR * (rng.nextDouble() * 2 - 1));
    }
}
