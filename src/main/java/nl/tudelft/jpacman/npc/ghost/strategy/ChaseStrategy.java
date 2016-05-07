package nl.tudelft.jpacman.npc.ghost.strategy;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Class determine how a ghost move when it has this strategy
 * @author Mikis
 *
 */
public class ChaseStrategy extends Strategy {
	public ChaseStrategy(Board board) {
		super(board);
	}
	
	/**
	 * Define the next move for blinky. 
	 * First, get the pacman's position.
	 * Second, search the shortest target to reach it.
	 * Or, get a random move (it's just to be sure blinky has a move, normally it never happens).
	 * @param blinky
	 * @return the next move for blinky
	 */
	@Override
	public Direction blinkyMove(Ghost blinky) {
		Square target = Navigation.findNearest(Player.class, blinky.getSquare()).getSquare();

		return shortestPath(blinky, target);
	}
	
	private Direction shortestPath(Ghost ghost, Square target) {
		List<Direction> path = Navigation.shortestPath(ghost.getSquare(), target, ghost);
		if (path != null && !path.isEmpty()) {
			return path.get(0);
		}

		return ghost.randomMove();
	}
	
	/**
	 * Define the next move for pinky.
	 * First, get the pacman's instance.
	 * Second, get the square at "squaresAhead" to the pacman's direction.
	 * Third, search the shortest target to reach it.
	 * Or, get a random move (it happens when the square computed in step 2 is not accessible to a ghost).
	 * @param pinky
	 * @return the next move for pinky
	 */
	@Override
	public Direction pinkyMove(Ghost pinky) {		
		Unit player = Navigation.findNearest(Player.class, pinky.getSquare());
		if (player == null) {
			return pinky.randomMove();
		}

		Direction targetDirection = player.getDirection();
		
		return directionToGo(pinky, targetDirection, player, 4);
	}
	
	public Direction directionToGo(Ghost ghost, Direction targetDirection, Unit player, int squaresAhead) {
		Square destination = player.getSquare();
		for (int i = 0; i < squaresAhead; i++) {
			destination = destination.getSquareAt(targetDirection);
		}

		return shortestPath(ghost, destination);
	}
	
	/**
	 * Define the next move for clyde.
	 * First, get the pacman's position.
	 * Second, if clyde is far enough ("squaresAhead" boxes) then clyde has the blinky's behaviour 
	 *         if clyde is too close from pacman then clyde move to the opposite pacman's direction.
	 * Or, get a random move (it happens when the square computed in step 2 is not accessible to a ghost)
	 * @param clyde
	 * @return the next move for clyde
	 */
	@Override
	public Direction clydeMove(Ghost clyde) {
		final int squaresAhead = 8;
		
		Square target = Navigation.findNearest(Player.class, clyde.getSquare()).getSquare();
		if (target == null) {
			return clyde.randomMove();
		}

		List<Direction> path = Navigation.shortestPath(clyde.getSquare(), target, clyde);
		if (path != null && !path.isEmpty()) {
			Direction d = path.get(0);
			if (path.size() <= squaresAhead) {
				return getOpposites(d);
			}
			return d;
		}
		return clyde.randomMove();
	}
	
	/**
	 * Define the next move for inky.
	 * First, get the pacman's instance.
	 * Second, get the square at "squaresAhead" to the opposite pacman's direction.
	 * Third, search the shortest target to reach it.
	 * Or, get a random move (it happens when the square computed in step 2 is not accessible to a ghost).
	 * @param inky
	 * @return the next move for inky
	 */
	@Override
	public Direction inkyMove(Ghost inky) {		
		Unit player = Navigation.findNearest(Player.class, inky.getSquare());
		if (player == null) {
			return inky.randomMove();
		}

		Direction targetDirection = getOpposites(player.getDirection());
		
		return directionToGo(inky, targetDirection, player, 4);
	}
	
	/**
	 * Get the opposite direction to another direction
	 * @param direction
	 * @return the opposite direction 
	 */
	private Direction getOpposites(Direction direction) {
		Map<Direction, Direction> opposites = new EnumMap<>(Direction.class);
		{
			opposites.put(Direction.NORTH, Direction.SOUTH);
			opposites.put(Direction.SOUTH, Direction.NORTH);
			opposites.put(Direction.WEST, Direction.EAST);
			opposites.put(Direction.EAST, Direction.WEST);
		}
		
		return opposites.get(direction);
	}

}