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
	
	public Direction move(Ghost ghost) {
		switch(ghost.getClass().getSimpleName()) {
			case "Blinky" : return blinkyMove(ghost);
			case "Pinky" : return pinkyMove(ghost);
			case "Clyde" : return clydeMove(ghost);
			case "Inky" : return inkyMove(ghost);
			default : return null;
		}
	}
		
	public abstract Direction blinkyMove(Ghost ghost);
	public abstract Direction inkyMove(Ghost ghost);
	public abstract Direction pinkyMove(Ghost ghost);
	public abstract Direction clydeMove(Ghost ghost);

}
