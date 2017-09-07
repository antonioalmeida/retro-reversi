package feup.lpoo.reversi.presenter.ai;

import feup.lpoo.reversi.model.BoardModel;
import feup.lpoo.reversi.model.MoveModel;


/**
 * The interface used for the AI's move choosing
 */
public interface AIMoveStrategy {
    /**
     * Finds a move to play according to the piece and strategy interface implementation
     * @param piece AI's piece in-game
     * @param board Current game board
     * @return The move chosen by the AI algorithm
     */
    MoveModel findMove(BoardModel board, char piece);
}

