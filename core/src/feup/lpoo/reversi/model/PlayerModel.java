package feup.lpoo.reversi.model;

import java.io.Serializable;

/**
 * Represents a player in-game
 */
public abstract class PlayerModel implements Serializable{
    /**
     * The amount of points he has
     */
    protected int points;

    /**
     * His next move
     */
    protected MoveModel move;

    /**
     * The piece color he's playing with
     */
    protected char piece;

    /**
     * Ready status for his in game action
     */
    protected boolean ready;

    /**
     * Active status for his online in game action
     */
    protected boolean active;

    /**
     * Constructor, initializes fields to a default player
     * @param piece Piece color he's going to play with
     */
    public PlayerModel(char piece) {
        setPoints(2);
        this.piece = piece;
        ready = false;
        active = true;
    }

    /**
     * Setter for the player's next move
     * @param move His next move
     */
    public void setMove(MoveModel move) {
        this.move = move;
    }

    /**
     * Setter for the player's point
     * @param points Updated amount of points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Getter for his next move
     * @return player's next move
     */
    public MoveModel getMove() {
        return move;
    }

    /**
     * Getter for his piece
     * @return player's piece color
     */
    public char getPiece() {
        return piece;
    }

    /**
     * Getter for his point
     * @return player's amount of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setter for the ready status, makes it evaluate to true until further update
     */
    public void setReady() {
        ready = true;
    }

    /**
     * Setter for the ready status, makes it evaluate to false until further update
     */
    public void resetReady() {
        ready = false;
    }

    /**
     * Getter for ready & active status
     * @return logical 'and' of active and ready statuses
     */
    public boolean isReady() {
        return active && ready;
    }

    /**
     * Setter for the active status
     * @param val new active status
     */
    public void setActive(boolean val) {
        active = val;
    }

    /**
     * Getter for the active status
     * @return active status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Compares two players by their piece color
     * @param o other player to be compared
     * @return true if both players have the same piece color, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        PlayerModel p2 = (PlayerModel)o;
        return piece == p2.getPiece();
    }
}
