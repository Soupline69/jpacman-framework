package nl.tudelft.jpacman.npc.ghost.strategy;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.npc.ghost.Ghost;

/**
 * Interface determine the actual ghost's strategy
 * @author Mikis
 *
 */
public abstract class Strategy {
	protected Board board;
	
	/**
	 * attach the board to the strategy. Why ? The strategy has to know the dimension of the board and has to know
	 * if a square is accesible to a ghost.
	 * @param board
	 */
	public Strategy(Board board) {
		this.board = board;
	}
	
	public abstract Direction move(Ghost ghost);
}
