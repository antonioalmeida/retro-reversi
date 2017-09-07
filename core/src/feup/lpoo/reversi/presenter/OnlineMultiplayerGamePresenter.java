package feup.lpoo.reversi.presenter;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.UserModel;

/**
 * Game presenter used during online multi-player play
 */
public class OnlineMultiplayerGamePresenter extends GamePresenter {

    /**
     * Constructor
     * @param reversi main program object
     */
    public OnlineMultiplayerGamePresenter(Reversi reversi) {
        super(reversi);
        game = reversi.getPlayServices().getMatchData();
        initPlayers();
    }

    /**
     * Performs a game reset, re-initializing both players and game models
     */
    @Override
    public void restartGame() {
        blackPlayer = new UserModel('B');
        blackPlayer.setActive(true);
        whitePlayer = new UserModel('W');
        whitePlayer.setActive(false);
        game = new GameModel(blackPlayer, whitePlayer);
        reversi.getPlayServices().rematch();
    }

    /**
     * Calls the Play Services API to finish the match and update the user's achievements
     */
    @Override
    public void playServicesCalls() {
        reversi.getPlayServices().takeLastTurn(game);
        reversi.getPlayServices().finishMatch();
    }

    /**
     * Initializes player models
     */
    @Override
    public void initPlayers() {
        blackPlayer = game.getCurrentPlayer();
        whitePlayer = game.getNonCurrentPlayer();
    }

    /**
     * Submits a game turn to the server
     */
    @Override
    public void screenAction() {
        reversi.getPlayServices().takeTurn(game);
    }
}
