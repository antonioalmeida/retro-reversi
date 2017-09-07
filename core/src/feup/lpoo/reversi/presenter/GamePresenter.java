package feup.lpoo.reversi.presenter;

import java.util.Locale;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.MoveModel;
import feup.lpoo.reversi.model.PlayerModel;

/**
 * Abstraction for a game presenter, a bridge between the model and view components
 */
public abstract class GamePresenter {
    /**
     * The main program class
     */
    protected Reversi reversi;

    /**
     * The game model component
     */
    protected GameModel game;

    /**
     * The game player with the black pieces
     */
    protected PlayerModel blackPlayer;

    /**
     * The game player with the white pieces
     */
    protected PlayerModel whitePlayer;

    /**
     * Constructor
     * @param reversi main program object
     */
    public GamePresenter(Reversi reversi) {
        this.reversi = reversi;
    }

    /**
     * Resets the game to an initial state
     */
    public abstract void restartGame();

    /**
     * Retrieves the amount of points the player with the black pieces has
     * @return Black player's points in a string format
     */
    public String getBlackPoints() {
        return String.format(Locale.getDefault(), "%02d", game.getBlackPoints());
    }

    /**
     * Retrieves the amount of points the player with the white pieces has
     * @return White player's points in a string format
     */
    public String getWhitePoints() {
        return String.format(Locale.getDefault(), "%02d", game.getWhitePoints());
    }

    /**
     * Retrieve a final message according to the result
     * @return small message containing the game outcome
     */
    public String getResult() {
        int black = game.getBlackPoints();
        int white = game.getWhitePoints();

        String result;
        if(black > white)
            result =  "Black wins!";
        else if(white > black)
            result =  "White wins!";
        else
            result = "It's a tie!";
        return result;
    }

    /**
     * Takes a screen input location and updates the game (if necessary) accordingly
     * @param x input's x-coordinate
     * @param y input's y-coordinate
     * @throws CloneNotSupportedException
     */
    public void handleInput(int x, int y) throws CloneNotSupportedException {
        MoveModel move = game.getValidMove(x, y);

        if(move != null) {
            PlayerModel currentPlayer = game.getCurrentPlayer();

            if(currentPlayer.isActive()) {
                currentPlayer.setMove(move);
                currentPlayer.setReady();
            }
        }
    }

    /**
     * Calls the game model update method when current player is ready
     */
    public void updateGame() {
        if(!game.isOver()) {
            if (game.getCurrentPlayer().isReady()) {
                game.getCurrentPlayer().resetReady();
                game.updateGame();
            }
        }
    }

    /**
     * Checks if current game session has terminated
     * @return true if game model session is over, false otherwise
     */
    public boolean gameOver() {
        return game.isOver();
    }

    /**
     * Google play services API calls to possibly update user achievements
     */
    public abstract void playServicesCalls();

    /**
     * Retrieves a game board piece
     * @param x piece's x-coordinate
     * @param y piece's y-coordinate
     * @return piece color at given position
     */
    public char getCurrentPiece(int x, int y) {
        return game.getPieceAt(x,y);
    }

    /**
     * Initializes both player models according to the game type
     */
    public abstract void initPlayers();

    /**
     * Checks if it's the black player's turn to play
     * @return true if it's his turn, false otherwise
     */
    public boolean isBlackTurn() {
        return game.getCurrentPlayer().equals(blackPlayer);
    }

    /**
     * Checks if it's the white player's turn to play
     * @return true if it's his turn, false otherwise
     */
    public boolean isWhiteTurn() {
        return game.getCurrentPlayer().equals(whitePlayer);
    }

    /**
     * Performs an action according to the on-screen visible button
     */
    public abstract void screenAction();
}
