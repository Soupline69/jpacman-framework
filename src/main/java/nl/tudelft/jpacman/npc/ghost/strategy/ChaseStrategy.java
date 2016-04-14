package nl.tudelft.jpacman.npc.ghost.strategy;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;

/**
 * Class determine how a ghost move when it has this strategy
 * @author Mikis
 *
 */
public class ChaseStrategy extends Strategy {

	/**
	 * Define the next move for the ghost according the instance of this ghost
	 */
	@Override
	public Direction move(Ghost ghost) {
		switch(ghost.getClass().getSimpleName()) {
			case "Blinky" : return blinkyMove(ghost);
			case "Pinky" : return pinkyMove(ghost);
			case "Clyde" : return clydeMove(ghost);
			case "Inky" : return inkyMove(ghost);
			default : return null;
		}
	}
	
	/**
	 * Define the next move for blinky. 
	 * First, get the pacman's position.
	 * Second, search the shortest target to reach it.
	 * Or, get a random move (it's just to be sure blinky has a move, normally it never happens).
	 * @param blinky
	 * @return the next move for blinky
	 */
	private Direction blinkyMove(Ghost blinky) {
		Square target = Navigation.findNearest(Player.class, blinky.getSquare()).getSquare();
		
		if (target == null) {
			return blinky.randomMove();
		}

		List<Direction> path = Navigation.shortestPath(blinky.getSquare(), target, blinky); 
		if (path != null && !path.isEmpty()) {
			return path.get(0);
		}
		
		return blinky.randomMove();
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
	private Direction pinkyMove(Ghost pinky) {
		int squaresAhead = 4;
		
		Unit player = Navigation.findNearest(Player.class, pinky.getSquare());
		if (player == null) {
			return pinky.randomMove();
		}

		Direction targetDirection = player.getDirection();
		Square destination = player.getSquare();
		for (int i = 0; i < squaresAhead; i++) {
			destination = destination.getSquareAt(targetDirection);
		}

		List<Direction> path = Navigation.shortestPath(pinky.getSquare(), destination, pinky);
		if (path != null && !path.isEmpty()) {
			return path.get(0);
		}
		return pinky.randomMove();
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
	private Direction clydeMove(Ghost clyde) {
		int squaresAhead = 8;
		
		Square target = Navigation.findNearest(Player.class, clyde.getSquare()).getSquare();
		if (target == null) {
			return clyde.randomMove();
		}

		List<Direction> path = Navigation.shortestPath(clyde.getSquare(), target, clyde);
		if (path != null && !path.isEmpty()) {
			Direction d = path.get(0);
			if (path.size() <= squaresAhead) {
				Direction oppositeDir = getOpposites(d);
				return oppositeDir;
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
	private Direction inkyMove(Ghost inky) {
		int squaresAhead = 4;
		
		Unit player = Navigation.findNearest(Player.class, inky.getSquare());
		if (player == null) {
			return inky.randomMove();
		}

		Direction targetDirection = getOpposites(player.getDirection());
		Square destination = player.getSquare();
		for (int i = 0; i < squaresAhead; i++) {
			destination = destination.getSquareAt(targetDirection);
		}

		List<Direction> path = Navigation.shortestPath(inky.getSquare(), destination, inky);
		if (path != null && !path.isEmpty()) {
			return path.get(0);
		}
		return inky.randomMove();
	}
	
	/**
	 * Get the opposite direction to another direction
	 * @param direction
	 * @return the opposite direction 
	 */
	private Direction getOpposites(Direction direction) {
		Map<Direction, Direction> opposites = new EnumMap<Direction, Direction>(Direction.class);
		{
			opposites.put(Direction.NORTH, Direction.SOUTH);
			opposites.put(Direction.SOUTH, Direction.NORTH);
			opposites.put(Direction.WEST, Direction.EAST);
			opposites.put(Direction.EAST, Direction.WEST);
		}
		
		return opposites.get(direction);
	}

}