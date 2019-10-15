package org.libertybikes.ai;

import java.util.Arrays;

import org.libertybikes.ai.model.GameTick;
import org.libertybikes.ai.model.Obstacle;
import org.libertybikes.ai.model.Obstacle.MovingObstacle;
import org.libertybikes.ai.model.Player;

/**
 * Represents the main logic of your custom Liberty Bikes AI. Every game tick AIWebSocket will
 * call processAIMove(String message) where the message represents the state of the game board
 * at that time. Using this information you have to decide when to change your bike's direction.
 *
 * Helpful Information:
 *
 * The game board is represented as a 2d array of size 121
 * - top left corner is [0][0]
 * - top right corner is [120][0]
 * - bottom right is [120][120]
 *
 * Each game tick you will recieve a list of all obstacles (including players)
 * - Each obstacle is a rectangle
 * - Their stated position is the top left space they are occupying
 * - Adding their height and width, you can determine the total space they occupy
 *
 * Each game tick, each player will leave a trail behind from the middle of their 3x3 surface area
 * - This is not provided in the message, you will have to keep track of the building walls.
 * - For example, a player at [50][50] will leave an trail at [51][51]
 * - Moving obstacles can erase player trails
 */
public class AILogic {

    private static final int BOARD_SIZE = 120;

    public static enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    };

    public static enum GAME_SLOT {
        OPEN,
        FIXED_OBSTACLE,
        MOVING_OBSTACLE,
        PLAYER,
        PLAYER_TRAIL
    }

    private final GAME_SLOT[][] gameBoard = new GAME_SLOT[BOARD_SIZE][BOARD_SIZE];
    private final String myPlayerId;
    private boolean filledFixedObstacles = false;
    private DIRECTION currentDirection = DIRECTION.RIGHT;
    private int currentX;
    private int currentY;

    public AILogic(String myId) {
        myPlayerId = myId;
        for (int x = 0; x < BOARD_SIZE; x++)
            Arrays.fill(gameBoard[x], GAME_SLOT.OPEN);
    }

    /*
     * This should contain the main logic of the AI, it receives a JSON straing of all the
     * objects on the board, players and obstacles.
     */
    public DIRECTION processAiMove(GameTick gameTick) {
        updateBoard(gameTick);

        System.out.println("Currently facing: " + currentDirection);
        DIRECTION bestDirection = currentDirection;
        int bestDirectionSlots = 0;
        for (DIRECTION d : DIRECTION.values()) {
            int freeSlots = freeSlots(d);
            if (freeSlots > bestDirectionSlots) {
                bestDirection = d;
                bestDirectionSlots = freeSlots;
            }
        }
        currentDirection = bestDirection;
        System.out.println("  will face: " + currentDirection);
        return currentDirection;
    }

    /**
     * @param lookingDirection The direction to count the number of free slots in
     * @return The number of open game board slots between the player's current location
     *         and the nearest obstacle in that direction
     */
    private int freeSlots(DIRECTION lookingDirection) {
        int xIncrement = 0, yIncrement = 0;
        if (lookingDirection == DIRECTION.LEFT) {
            xIncrement = -1;
            yIncrement = 0;
        } else if (lookingDirection == DIRECTION.UP) {
            xIncrement = 0;
            yIncrement = -1;
        } else if (lookingDirection == DIRECTION.RIGHT) {
            xIncrement = 1;
            yIncrement = 0;
        } else { // down
            xIncrement = 0;
            yIncrement = 1;
        }

        int freeSlots = 0;
        int x = this.currentX + (xIncrement * 2);
        int y = this.currentY + (yIncrement * 2);
        int xOffset = (lookingDirection == DIRECTION.UP || lookingDirection == DIRECTION.DOWN) ? 1 : 0;
        int yOffset = xOffset == 0 ? 1 : 0;
        while (x < BOARD_SIZE && y < BOARD_SIZE &&
               x > 0 && y > 0 &&
               // Since players are 3 slots wide, we need to look 3 slots wide
               gameBoard[x][y] == GAME_SLOT.OPEN &&
               gameBoard[x + xOffset][y + yOffset] == GAME_SLOT.OPEN &&
               gameBoard[x + (-1 * xOffset)][y + (-1 * yOffset)] == GAME_SLOT.OPEN) {
            freeSlots++;
            x += xIncrement;
            y += yIncrement;
        }
        System.out.println("  Found " + freeSlots + " free slots looking " + lookingDirection);
        return freeSlots;
    }

    private void updateBoard(GameTick currentBoard) {
        // Only fill static obstacles once
        if (!filledFixedObstacles) {
            for (Obstacle o : currentBoard.obstacles)
                fill(o);
            filledFixedObstacles = true;
        }

        // Erase moving obstacles and players each turn so they can be re-drawn
        for (int x = 0; x < BOARD_SIZE; x++)
            for (int y = 0; y < BOARD_SIZE; y++)
                if (gameBoard[x][y] == GAME_SLOT.MOVING_OBSTACLE || gameBoard[x][y] == GAME_SLOT.PLAYER)
                    gameBoard[x][y] = GAME_SLOT.OPEN;

        // Re-draw moving obstacles
        for (MovingObstacle o : currentBoard.movingObstacles)
            fill(o);

        // Re-draw players
        for (Player p : currentBoard.players) {
            fill(p);
            if (myPlayerId.equals(p.id)) {
                // Update my location
                currentX = p.x + 1;
                currentY = p.y + 1;
            }
        }
        //printBoard();
    }

    /**
     * Fills the {@code gameBoard} 2d array with the corresponding spaces occupied
     * by the supplied Obstacle
     */
    private void fill(Obstacle o) {
        GAME_SLOT type = GAME_SLOT.FIXED_OBSTACLE;
        if (o instanceof Player)
            type = GAME_SLOT.PLAYER;
        else if (o instanceof MovingObstacle)
            type = GAME_SLOT.MOVING_OBSTACLE;
        for (int w = 0; w < o.width; w++) {
            for (int h = 0; h < o.height; h++) {
                // Render the center slot of a 3x3 player as its trail
                if (type == GAME_SLOT.PLAYER && w == 1 && h == 1)
                    gameBoard[o.x + w][o.y + h] = GAME_SLOT.PLAYER_TRAIL;
                else if (type == GAME_SLOT.PLAYER && gameBoard[o.x + w][o.y + h] == GAME_SLOT.PLAYER_TRAIL)
                    ; // do nothing (don't overwrite player trails with player slots)
                else
                    gameBoard[o.x + w][o.y + h] = type;
            }
        }
    }

    /**
     * Prints a textual representation of {@code gameBoard} 2d array to System.out
     * a '#' char represents a fixed obstacle
     * a '*' char represents a moving ostacle
     * a 'X' char represents a player
     * a 'x' char represents a player's trail
     * Since this method prints 120 lines of output, it would be good to only use this
     * for debugging purposes.
     */
    private void printBoard() {
        StringBuilder sb = new StringBuilder(BOARD_SIZE * BOARD_SIZE + BOARD_SIZE + 1);
        sb.append('\n');
        for (int b = 0; b < BOARD_SIZE + 2; b++)
            sb.append('_');
        sb.append('\n');
        for (int y = 0; y < BOARD_SIZE; y++) {
            sb.append('|');
            for (int x = 0; x < BOARD_SIZE; x++) {
                char c = ' ';
                GAME_SLOT slot = gameBoard[x][y];
                switch (slot) {
                    case FIXED_OBSTACLE:
                        c = '#';
                        break;
                    case MOVING_OBSTACLE:
                        c = '*';
                        break;
                    case PLAYER:
                        c = 'X';
                        break;
                    case PLAYER_TRAIL:
                        c = 'x';
                    case OPEN:
                        break; // leave blank
                    default:
                        throw new IllegalArgumentException("Unknown slot type: " + slot);
                }
                sb.append(c);
            }
            sb.append("|\n");
        }
        for (int b = 0; b < BOARD_SIZE + 2; b++)
            sb.append('_');
        System.out.println(sb.toString());
    }
}
