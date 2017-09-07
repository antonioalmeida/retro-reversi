package feup.lpoo.reversi.model;

import java.io.Serializable;

/**
 * Stores a game state, part of the memento design pattern
 */
public class GameMemento implements Serializable {
    /**
     * The game board at that state
     */
    private BoardModel board;

    /**
     * Player turn at that state
     */
    private TurnState turn;

    /**
     * Constructor, initializes all fields
     * @param board game board in new state
     * @param turn player turn in new state
     */
    public GameMemento(BoardModel board, TurnState turn) {
        this.board = board;
        this.turn = turn;
    }

    /**
     * Getter for the state's game board
     * @return state's game board
     */
    public BoardModel getBoard() {
        return board;
    }

    /**
     * Getter for the state's player's turn
     * @return state's player's turn
     */
    public TurnState getTurn() {
       return turn;
    }
}
