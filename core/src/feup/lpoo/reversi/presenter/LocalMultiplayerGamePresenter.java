package feup.lpoo.reversi.presenter;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.model.AIModel;
import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.UserModel;
import feup.lpoo.reversi.presenter.ai.AIPresenter;

/**
 * Game presenter used during offline multi-player games
 */
public class LocalMultiplayerGamePresenter extends GamePresenter {

    /**
     * Constructor
     * @param reversi main program object
     */
    public LocalMultiplayerGamePresenter(Reversi reversi) {
        super(reversi);
        initPlayers();
        game = new GameModel(blackPlayer, whitePlayer);
    }

    /**
     * Performs a game reset, re-initializing players and game models
     */
    @Override
    public void restartGame() {
        initPlayers();
        game = new GameModel(blackPlayer, whitePlayer);
    }

    /**
     * API call that updates achievements, however in this particular class it does nothing
     */
    @Override
    public void playServicesCalls() {

    }

    /**
     * Initializes both player models
     */
    @Override
    public void initPlayers() {
            blackPlayer = new UserModel('B');
            whitePlayer = new UserModel('W');
    }

    /**
     * Calls the game model to undo the last move played
     */
    @Override
    public void screenAction() {
        game.undoMove(1);
    }
}
