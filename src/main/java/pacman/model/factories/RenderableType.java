package pacman.model.factories;

/**
 * Mapping of characters used in map text files to renderable type
 */
public interface RenderableType {
    char HORIZONTAL_WALL = '1';
    char VERTICAL_WALL = '2';
    char UP_LEFT_WALL = '3';
    char UP_RIGHT_WALL = '4';
    char DOWN_LEFT_WALL = '5';
    char DOWN_RIGHT_WALL = '6';
    char PELLET = '7';
    char PACMAN = 'p';
    char GHOST_BLINKY = 'b';
    char GHOST_PINKY = 's';
    char GHOST_INKY = 'i';
    char GHOST_CLYDE = 'c';
}
