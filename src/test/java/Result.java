import bot.AiGame;
import genetic.Species;
import org.junit.jupiter.api.Test;

public class Result {
	@Test
	void test() {
		// 34k hole: 2730, roof: -276, square: 353, cliff: 846
		Species sp = new Species(1094, 94, 42, 320);
		AiGame game = new AiGame();

		long startTime = System.nanoTime();
		double result = game.play(sp, 100);
		System.out.println(result / 100 + ", time: " + (System.nanoTime() - startTime) / 1000_000_000L);
	}
}
