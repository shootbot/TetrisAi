import java.util.Random;

class Species implements Comparable {
	public int score = 0;
	public int holePenalty;
	public int roofPenalty;
	public int squarePenalty;
	public int cliffPenalty;

	public Species() {
	}

	public Species(Random rng) {
		this();
		this.holePenalty = GenAlg.HOLE_P_MIN + rng.nextInt(GenAlg.HOLE_P_MAX - GenAlg.HOLE_P_MIN);
		this.roofPenalty = GenAlg.ROOF_P_MIN + rng.nextInt(GenAlg.ROOF_P_MAX - GenAlg.ROOF_P_MIN);
		this.squarePenalty = GenAlg.SQUARE_P_MIN + rng.nextInt(GenAlg.SQUARE_P_MAX - GenAlg.SQUARE_P_MIN);
		this.cliffPenalty = GenAlg.CLIFF_P_MIN + rng.nextInt(GenAlg.CLIFF_P_MAX - GenAlg.CLIFF_P_MIN);
	}

	public Species(int holePenalty, int roofPenalty, int squarePenalty, int cliffPenalty) {
		this.holePenalty = holePenalty;
		this.roofPenalty = roofPenalty;
		this.squarePenalty = squarePenalty;
		this.cliffPenalty = cliffPenalty;
	}

	public Species pair(Species sp2) {
		int resHolePenalty = (holePenalty + sp2.holePenalty) / 2;
		int resRoofPenalty = (roofPenalty + sp2.roofPenalty) / 2;
		int resSquarePenalty = (squarePenalty + sp2.squarePenalty) / 2;
		int resCliffPenalty = (cliffPenalty + sp2.cliffPenalty) / 2;
		return new Species(resHolePenalty, resRoofPenalty, resSquarePenalty, resCliffPenalty);
	}

	public int compareTo(Object ob) { // maximum first
		if (score < ((Species) ob).score) {
			return 1;
		} else if (score == ((Species) ob).score) {
			return 0;
		} else {
			return -1;
		}
	}

	public String toString() {
		String answer = "hole_p: " + holePenalty;
		answer.concat(", roof_p: " + holePenalty);
		answer.concat(", square_p: " + holePenalty);
		answer.concat(", cliff_p: " + holePenalty);
		return answer;
	}
}
