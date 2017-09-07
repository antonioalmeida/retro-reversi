package feup.lpoo.reversi.view;

import feup.lpoo.reversi.presenter.ai.AIMoveStrategy;

/**
 * Class to encapsulate a match's information
 */
public class GameInfo {
    private boolean isSinglePlayer;
    private boolean isBlack;
    private boolean isOnline;
    private AIMoveStrategy strategy;

    public GameInfo(boolean singlePlayer, boolean online) {
        this.isSinglePlayer = singlePlayer;
        this.isOnline = online;
    }

    public GameInfo(boolean singlePlayer, boolean isBlack, AIMoveStrategy strategy) {
        this(singlePlayer, false);
        this.isBlack = isBlack;
        this.strategy = strategy;
    }

    public boolean isSinglePlayer() {
        return isSinglePlayer;
    }

    public boolean userIsBlack() {
        return isBlack;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public AIMoveStrategy getStrategy() {
        return strategy;
    }
}
