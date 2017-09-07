package feup.lpoo.reversi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a move and its effects (pieces to be rotated)
 */
public class MoveModel implements Serializable{
    /**
     * Move's x-coordinate
     */
    private int x;

    /**
     * Move's y-coordinates
     */
    private int y;

    /**
     * Piece to be placed
     */
    private char piece;

    /**
     * Positions affected by this move
     */
    private ArrayList<Integer[]> changedPositions;

    /**
     * Constructor, initializes main move related fields
     * @param x Move's board x-coordinate
     * @param y Move's board y-coordinate
     * @param piece Move's piece to be placed at given coordinates
     */
    public MoveModel(int x, int y, char piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
        changedPositions = new ArrayList<Integer[]>();
    }

    /**
     * Getter for the move's x-coordinate
     * @return move's x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the move's y-coordinates
     * @return move's y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for the move's piece
     * @return move's piece
     */
    public char getPiece() {
        return piece;
    }

    /**
     * Adds a set of board positions to the changedPositions list kept
     * @param toAdd positions to be added
     */
    public void addChangedPositions(ArrayList<Integer[]> toAdd) {
       changedPositions.addAll(toAdd);
    }

    /**
     * Getter for the board positions affected by this move
     * @return positions affected by this move
     */
    public ArrayList<Integer[]> getChangedPositions() {
        return changedPositions;
    }

    /**
     * Compares two moves by the position and piece
     * @param otherMove move to be compared
     * @return true if move was played at the same coordinates and with the same piece color, false otherwise
     */
    @Override
    public boolean equals(Object otherMove){
        if(otherMove == null || !(otherMove instanceof MoveModel)) return false;
        MoveModel otherMoveCasted = ((MoveModel)otherMove);
        return x == otherMoveCasted.getX() && y == otherMoveCasted.getY() && piece == otherMoveCasted.getPiece();
    }
}
