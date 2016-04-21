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
		initStrategies();
		attach(board);
	}
	
	public void initStrategies() {
		Strategy chase = new ChaseStrategy();
		Strategy scatter = new ScatterStrategy();
		strategies.put("chase", chase);
		strategies.put("scatter", scatter);
	}
	
	public Map<String, Strategy> getStrategies() {
		return strategies;
	}
	
	public void attach(Board board) {
		for(Strategy s : strategies.values()) {
			s.attach(board);
		}
	}
}
