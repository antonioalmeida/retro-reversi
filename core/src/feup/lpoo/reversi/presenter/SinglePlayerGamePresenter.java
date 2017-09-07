package feup.lpoo.reversi.presenter;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.model.AIModel;
import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.UserModel;
import feup.lpoo.reversi.presenter.ai.AIMoveStrategy;
import feup.lpoo.reversi.presenter.ai.AIPresenter;

/**
 * Game presenter used during offline game-play against an AI player
 */
public class SinglePlayerGamePresenter extends GamePresenter {
    /**
     * The AI presenter "bridge"
     */
    private AIPresenter AI;

    /**
     * The AI's move choosing strategy
     */
    private AIMoveStrategy strategy;

    /**
     * Is the human player playing with the black pieces?
     */
    private boolean userIsBlack;

    /**
     * Constructor, initializes all fields
     * @param reversi main program object
     * @param strategy Chosen AI strategy
     * @param isBlack boolean stating whether human is playing black or white
     */
    public SinglePlayerGamePresenter(Reversi reversi, AIMoveStrategy strategy, boolean isBlack) {
        super(reversi);
        userIsBlack = isBlack;
        this.strategy = strategy;
        AI = new AIPresenter(strategy);
        initPlayers();
        game = new GameModel(blackPlayer, whitePlayer);
        AI.setGame(game);
    }

    /**
     * Initializes players according to userIsBlack's information
     */
    @Override
    public void initPlayers() {
        if(userIsBlack) {
            blackPlayer = new UserModel('B');
            whitePlayer = new AIModel('W', AI);
        }
        else {
            blackPlayer = new AIModel('B', AI);
            whitePlayer = new UserModel('W');
        }
    }

    /**
     * Performs a game reset, re-initializing players and game models
     */
    @Override
    public void restartGame() {
        AI = new AIPresenter(strategy);
        initPlayers();
        game = new GameModel(blackPlayer, whitePlayer);
        AI.setGame(game);
    }

    /**
     * Checks if the human player has won the game
     * @return true if human player won, false otherwise
     */
    private boolean userWon() {
        int black = game.getBlackPoints();
        int white = game.getWhitePoints();

        if(userIsBlack && black > white)
            return true;
        else if(!userIsBlack && white > black)
            return true;

        return false;
    }

    /**
     * Calls the Play Services API to update the user's achievements
     */
    @Override
    public void playServicesCalls() {
        boolean victory = userWon();
        reversi.getPlayServices().matchCompleted(victory);
    }

    /**
     * Calls the game model to undo the last 1 or 2 moves depending on whose turn it is
     */
    @Override
    public void screenAction() {
        if(game.getCurrentPlayer() instanceof AIModel) {
            game.undoMove(1);
            return;
        }
        game.undoMove(2);
    }

}
