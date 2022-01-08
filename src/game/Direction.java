package game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Albert Troussard (330361)
 */
public enum Direction {
 LEFT,RIGHT,UP,DOWN;

 public static List<Direction> all(){ return List.of(Direction.values()); }
}
