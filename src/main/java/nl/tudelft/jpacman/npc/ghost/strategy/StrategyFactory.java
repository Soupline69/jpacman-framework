package nl.tudelft.jpacman.npc.ghost.strategy;

import java.util.HashMap;
import java.util.Map;

import nl.tudelft.jpacman.board.Board;

/**
 * Factory that creates strategies which will determine the ghost's displacements
 * @author Mikis
 *
 */
public class StrategyFactory {
	private final Map<String, Strategy> strategies;
	
	public StrategyFactory(Board board) {
		this.strategies = new HashMap<>();
		initStrategies(board);
	}
	
	public void initStrategies(Board board) {
		Strategy chase = new ChaseStrategy(board);
		Strategy scatter = new ScatterStrategy(board);
		strategies.put("chase", chase);
		strategies.put("scatter", scatter);
	}
	
	public Map<String, Strategy> getStrategies() {
		return strategies;
	}
	
}
