import bot.AiGame;
import genetic.Species;
import org.junit.jupiter.api.Test;

public class Result {
	@Test
	void test() {
		Species sp = new Species(1094, 94, 42, 320);
		AiGame game = new AiGame();
		double result = game.play(sp, 100);
		System.out.println(result / 100);
	}
}
