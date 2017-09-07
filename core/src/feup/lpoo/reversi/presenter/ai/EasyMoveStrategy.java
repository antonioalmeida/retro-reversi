package feup.lpoo.reversi.presenter.ai;

import java.util.ArrayList;

import feup.lpoo.reversi.model.BoardModel;
import feup.lpoo.reversi.model.MoveModel;

import java.util.Random;

/**
 * AI Strategy implementation that simply chooses a random valid move
 */
public class EasyMoveStrategy implements AIMoveStrategy {
    /**
     * Random number generator used to choose a move
     */
    private Random generator;

    /**
     * Default constructor, initializes generator
     */
    public EasyMoveStrategy() {
        generator = new Random();
    }

    /**
     * Chooses a random move from the valid ones in the current board for the specified piece and returns it
     * @param board Current game board
     * @param piece AI's piece in-game
     * @return Move chosen
     */
    @Override
    public MoveModel findMove(BoardModel board, char piece) {
        ArrayList<MoveModel> validMoves = board.getValidMoves(piece);
        int randomMoveIndex = generator.nextInt(validMoves.size());

        return validMoves.get(randomMoveIndex);
    }
}
