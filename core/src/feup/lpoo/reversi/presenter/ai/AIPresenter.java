package feup.lpoo.reversi.presenter.ai;

import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.MoveModel;

/**
 * Serves as a bridge between the AI and the board
 */
public class AIPresenter {
    /**
     * The game which holds the current board
     */
    GameModel game;

    /**
     * The AI's move choosing strategy
     */
    AIMoveStrategy strategy;

    /**
     * Constructor, sets the move choosing strategy
     * @param strategyChosen Move choosing strategy to be used
     */
    public AIPresenter(AIMoveStrategy strategyChosen){
        this.strategy = strategyChosen;
    }

    /**
     * Sets the game which stores the board to be possibly used by the AI algorithm
     * @param game
     */
    public void setGame(GameModel game) {
        this.game = game;
    }

    /**
     * Gets the AI move from the strategy's interface and returns it
     * @param piece AI's current piece
     * @return Move chosen by strategy
     */
    public MoveModel findMove(char piece) {
       MoveModel move = strategy.findMove(game.getGameBoard(), piece);

       return move;
    }
}
