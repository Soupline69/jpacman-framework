package nl.tudelft.jpacman.npc.ghost.strategy;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.npc.ghost.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Class determine how a ghost move when it has this strategy.
 * @author Mikis
 *
 */
public class ScatterStrategy extends Strategy {
	
	public ScatterStrategy(Board board) {
		super(board);
	}
	
	/**
	 * Define the next move for Blinky.
	 * If blinky isn't at home then blinky goes at home (top right)
	 * If blinky is at home then blinky move and always choose the right way
	 * @param blinky
	 * @return the next move for blinky
	 */
	@Override
	public Direction blinkyMove(Ghost blinky) {
		if(!blinky.isHome()) {
			return goHome(blinky, board.getWidth() - 2, 1, Direction.EAST);
		}
		return moveHomeBlinky(blinky);
	}
	
	/**
	 * Define the next move for Pinky.
	 * If pinky isn't at home then pinky goes at home (top left)
	 * If pinky is at home then pinky move and always choose the left way
	 * @param pinky
	 * @return the next move for pinky
	 */
	@Override
	public Direction pinkyMove(Ghost pinky) {
		if(!pinky.isHome()) {
			return goHome(pinky, 1, 1, Direction.WEST);
		}
		return moveHomePinky(pinky);
	}
	
	/**
	 * Define the next move for Clyde.
	 * If Clyde isn't at home then Clyde goes at home (bottom left)
	 * If Clyde is at home then Clyde move and always choose the left way
	 * @param Clyde
	 * @return the next move for Clyde
	 */
	@Override
	public Direction clydeMove(Ghost clyde) {
		if(!clyde.isHome()) {
			return goHome(clyde, 1, board.getHeight() - 2, Direction.SOUTH);
		}
		return moveHomeClyde(clyde);
	}
	
	/**
	 * Define the next move for Inky.
	 * If Inky isn't at home then Inky goes at home (bottom right)
	 * If Inky is at home then Inky move and always choose the right way
	 * @param Inky
	 * @return the next move for Inky
	 */
	@Override
	public Direction inkyMove(Ghost inky) {
		if(!inky.isHome()) {
			return goHome(inky, board.getWidth() - 2, board.getHeight() - 2, Direction.SOUTH);
		}
		return moveHomeInky(inky);
	}
	
	/**
	 * Bring ghost at home (defined by x;y)
	 * @param ghost
	 * @param x
	 * @param y
	 * @param direction that ghost has to take when it arrives at home for have a good next move
	 * @return the next move for going home
	 */
	private Direction goHome(Ghost ghost, int x, int y, Direction direction) {
		isAtHome(ghost, x, y, direction);
		List<Direction> path = Navigation.shortestPath(ghost.getSquare(), board.squareAt(x, y), ghost); 
		if (path != null && !path.isEmpty()) {
			return path.get(0);
		}
		return null; // We can give a direction but we have to do another switch to know what ghost'instance is
	}
	
	/**
	 * Determine if a ghost has reached home (defined by x;y)
	 * @param ghost
	 * @param x
	 * @param y
	 * @param direction that ghost has to take when it arrives at home for have a good next move
	 */
	private void isAtHome(Ghost ghost, int x, int y, Direction direction) {
		if(ghost.getSquare() == board.squareAt(x, y)) {
			ghost.setIsHome(true);
			ghost.setDirection(direction);
		}
	}
	
	/**
	 * @param blinky
	 * @return the next move for blinky (the right way or continue to the actual direction)
	 */
	private Direction moveHomeBlinky(Ghost blinky) {
		return getDirectionRight(blinky);
	}
	
	/**
	 * @param Pinky
	 * @return the next move for Pinky (the left way or continue to the actual direction)
	 */
	private Direction moveHomePinky(Ghost pinky) {
		return getDirectionLeft(pinky);
	}
	
	/**
	 * @param Inky
	 * @return the next move for inky (the right way or continue to the actual direction or another direction, if there is an exception)
	 */
	private Direction moveHomeInky(Ghost inky) {
		Direction direction = moveHomeBottom(inky, Direction.EAST);
		if(direction != null) {
			return direction;
		}

		return getDirectionRight(inky);
	}

	private Direction moveHomeBottom(Ghost ghost, Direction direction) {
		List<Direction> possibilities = getPossibilities(ghost);
		if(possibilities.size() <= 2) {
			if(ghost.getDirection() == direction && possibilities.contains(Direction.NORTH)) {
				return Direction.NORTH;
			} else if(ghost.getDirection() == Direction.SOUTH && possibilities.contains(direction)) {
				return direction;
			}
		} else {
			if(ghost.getDirection() == direction && possibilities.contains(direction)) {
				return direction;
			}
		}
		return null;
	}
	
	/**
	 * @param Clyde
	 * @return the next move for Clyde (the left way or continue to the actual direction or another direction, if there is an exception)
	 */
	private Direction moveHomeClyde(Ghost clyde) {
		Direction direction = moveHomeBottom(clyde, Direction.WEST);
		if(direction != null) {
			return direction;
		}

		return getDirectionLeft(clyde);
	}
	
	/**
	 * @param ghost
	 * @return the possible way around the ghost
	 */
	private List<Direction> getPossibilities(Ghost ghost) {
		List<Direction> possibilities = new ArrayList<>();
		for (Direction d : Direction.values()) {
			if (ghost.getSquare().getSquareAt(d).isAccessibleTo(ghost)) {
				possibilities.add(d);
			}
		}
		return possibilities;
	}
	
	private Direction getDirectionRight(Ghost ghost) {
		Map<Direction, Direction> right = new EnumMap<>(Direction.class);
		{
			right.put(Direction.NORTH, Direction.EAST);
			right.put(Direction.SOUTH, Direction.WEST);
			right.put(Direction.WEST, Direction.NORTH);
			right.put(Direction.EAST, Direction.SOUTH);
		}
		return getDirection(ghost, right.get(ghost.getDirection()));
	}
	
	private Direction getDirectionLeft(Ghost ghost) {
		Map<Direction, Direction> left = new EnumMap<>(Direction.class);
		{
			left.put(Direction.NORTH, Direction.WEST);
			left.put(Direction.SOUTH, Direction.EAST);
			left.put(Direction.WEST, Direction.SOUTH);
			left.put(Direction.EAST, Direction.NORTH);
		}
		return getDirection(ghost, left.get(ghost.getDirection()));
	}
	
	private Direction getDirection(Ghost ghost, Direction direction) {
		if(ghost.getSquare().getSquareAt(direction).isAccessibleTo(ghost)) {
			return direction;
		} 
		return ghost.getDirection();
	}

}