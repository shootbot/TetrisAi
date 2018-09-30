import java.util.Arrays;
import java.util.Random;

class GenAlg {
	private Species[] species;
	private Random rng;
	private GameInfo gi;
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
	
	public GenAlg() {
		rng = new Random();
		species = new Species[SPECIES_NUM];
		for (int i = 0; i < SPECIES_NUM; i++) {
			species[i] = new Species(rng);
		}
	}
	
	public static void main(String[] args) {
		GenAlg ga = new GenAlg();
		ga.doEvolution();
	}
	
	public void doEvolution() {
		gi = new GameInfo();
		game = new Game(gi, true);
		for (int i = 0; i < EVOLUTIONS_NUM; i++) {
			test(game);
			crossover();
			mutation();
		}
		test(game);
		System.out.println("winner: " + species[0]);
	}
		
	private void test(Game game) {
		int totalScore = 0;
		for (int i = 0; i < SPECIES_NUM; i++) {
			totalScore = 0;
			for (int j = 0; j < TESTS_NUM; j++) {
				//game.playGame(species[i], gi);
				gi.gameInProgress = true;
				do {
					try {
						Thread.sleep(50);
					} catch (InterruptedException ignore) {
						// Do nothing
					}
				} while (gi.gameInProgress);
				totalScore += gi.score;
				System.out.println("test #" + j + ": " + species[i] + " score: " + gi.score);
			}
			species[i].score = totalScore / TESTS_NUM;
		}
		Arrays.sort(species);
	}
	
	private void crossover() {
		// preserve 0, 1, 2
		species[3] = species[1].pair(species[6]);
		species[4] = species[2].pair(species[5]);
		species[5] = species[3].pair(species[4]);
		species[6] = species[1].pair(species[2]);
		species[7] = new Species(rng);
	}
	
	private void mutation() {
		for (int i = 0; i < SPECIES_NUM; i++) {
			species[i].holePenalty += (HOLE_P_MAX - HOLE_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
			species[i].roofPenalty += (ROOF_P_MAX - ROOF_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
			species[i].squarePenalty += (SQUARE_P_MAX - SQUARE_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
			species[i].cliffPenalty += (CLIFF_P_MAX - CLIFF_P_MIN) * 0.1 * (rng.nextDouble() * 2 - 1);
			
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

