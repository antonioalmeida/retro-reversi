package feup.lpoo.reversi.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores information about a game session
 */
public class GameModel implements Serializable{

    /**
     * Constant representing an empty piece
     */
    static final char EMPTY_PIECE = '-';

    /**
     * Constant representing a black piece
     */
    static final char BLACK_PIECE = 'B';

    /**
     * Constant representing a white piece
     */
    static final char WHITE_PIECE = 'W';

    /**
     * Constant representing a suggestion piece
     */
    static final char SUGGESTION_PIECE = 'X';

    /**
     * Constant representing the default board size
     */
    static final int BOARD_SIZE = 8;

    /**
     * Instance of TurnState enumeration that stores current turn state
     */
    private TurnState turn;

    /**
     * Instance of GameState enumeration that stores current turn state
     */
    private GameState state;

    /**
    * Current valid moves (updated on every turn)
    */
    private ArrayList<MoveModel> currentMoves;

    /**
     * The game session's board
     */
    private BoardModel gameBoard;

    /**
     * Game's state caretaker to restore previous states if necessary
     */
    private GameCareTaker caretaker;

    /**
     * Player playing black pieces
     */
    private PlayerModel blackPlayer;

    /**
     * Player playing white pieces
     */
    private PlayerModel whitePlayer;

    /**
     * Constructor, initializes all fields
     * @param black Player playing black pieces
     * @param white Player playing white pieces
     */
    public GameModel(PlayerModel black, PlayerModel white) {
        gameBoard = new BoardModel();

        blackPlayer = black;
        whitePlayer = white;

        caretaker = new GameCareTaker();

        turn = TurnState.BLACK;
        state = GameState.RUNNING;

        currentMoves = getValidMoves(getCurrentPlayer());
        gameBoard.setSuggestions(currentMoves);
        caretaker.add(saveState());
    }

    /**
     * Calculates the valid moves for a player
     * @param player Current player
     * @return set of valid moves for the player
     */
    private ArrayList<MoveModel> getValidMoves(PlayerModel player) {
        return gameBoard.getValidMoves(player.getPiece());
    }

    /**
     * Checks if a move at given coordinates if valid
     * @param x move's x-coordinate
     * @param y move's y-coordinates
     * @return move as a MoveModel object if it's valid, null otherwise
     */
    public MoveModel getValidMove(int x, int y) {
        for(int i = 0; i < currentMoves.size(); i++)
            if(currentMoves.get(i).getX() == x && currentMoves.get(i).getY() == y)
                return currentMoves.get(i);

        return null;
    }

    /**
     * Places a piece on the board and replicates its effects (possible rotation) on the remaining pieces
     * @param move The move to be played
     */
    private void makeMove(MoveModel move) {
        gameBoard.setPieceAt(move.getX(), move.getY(), move.getPiece());

        for(Integer[] pos : move.getChangedPositions())
            gameBoard.rotatePiece(pos[0], pos[1]);
    }

    /**
     * Updates the game state and information based on the current player's next move
     */
    public void updateGame(){
        if(isOver())
            return;

        MoveModel toMake = getCurrentPlayer().getMove();
        makeMove(toMake);

        updateTurn();
        updatePoints();
        updateGameState();
        currentMoves = getValidMoves(getCurrentPlayer());
        gameBoard.setSuggestions(currentMoves);
        verifySpecialEnding();

        caretaker.add(saveState());
    }

    /**
     * Updates the player's amount of points after a game update
     */
    private void updatePoints() {
        int black = gameBoard.getCurrentPoints(blackPlayer.getPiece());
        int white = gameBoard.getCurrentPoints(whitePlayer.getPiece());

        blackPlayer.setPoints(black);
        whitePlayer.setPoints(white);
    }

    /**
     * Updates the game's state after a game update
     */
    private void updateGameState() {
        int black = blackPlayer.getPoints();
        int white = whitePlayer.getPoints();

        if((black + white) == 64)
            state = (black > white) ? GameState.BLACK_WON : GameState.WHITE_WON;
        else if(black == 0)
            state = GameState.WHITE_WON;
        else if(white == 0)
            state = GameState.BLACK_WON;
        else //Useful for cases when game is over and user makes an undo
            state = GameState.RUNNING;

    }

    /**
     * Updates the player turn after a game update
     */
    private void updateTurn() {
        ArrayList<MoveModel> possibleMoves = getValidMoves(getNonCurrentPlayer());

        if(possibleMoves.size() > 0)
            turn = (turn == TurnState.BLACK ? TurnState.WHITE : TurnState.BLACK);
    }

    /**
     * Verifies a special ending situation and adjusts score accordingly if so so they always add up to 64
     */
    private void verifySpecialEnding() {
        int black = gameBoard.getCurrentPoints(blackPlayer.getPiece());
        int white = gameBoard.getCurrentPoints(whitePlayer.getPiece());

        if(currentMoves.size() == 0 && black + white < 64) {
            if (black > white) {
                black = 64 - white;
                state = GameState.BLACK_WON;
            } else if (white > black){
                white = 64 - black;
                state = GameState.WHITE_WON;
            }

            blackPlayer.setPoints(black);
            whitePlayer.setPoints(white);
        }
    }

    /**
     * Gets the piece at given position, considering both game and suggestion matrices
     * @param x piece's x-coordinate
     * @param y piece's y-coordinate
     * @return piece at given coordinates
     */
    public char getPieceAt(int x, int y) {
        char temp = gameBoard.getPieceAt(x,y);

        if(temp == GameModel.EMPTY_PIECE && getCurrentPlayer() instanceof UserModel && getCurrentPlayer().isActive())
            return gameBoard.getSuggestionAt(x,y);

        return temp;
    }

    /**
     * Getter for the current valid moves
     * @return set of valid moves at the moment
     */
    public ArrayList<MoveModel> getCurrentMoves() {
        return currentMoves;
    }

    /**
     * Getter for the game board
     * @return the game's board
     */
    public BoardModel getGameBoard() {
        return gameBoard;
    }

    /**
     * Getter for the current player
     * @return current player
     */
    public PlayerModel getCurrentPlayer() {
        if(turn == TurnState.BLACK)
            return blackPlayer;

        return whitePlayer;
    }

    /**
     * Getter for the non-current player
     * @return non-current player
     */
    public PlayerModel getNonCurrentPlayer() {
        if(turn == TurnState.BLACK)
            return whitePlayer;

        return blackPlayer;
    }

    /**
     * Getter for black player's amount of points
     * @return black player's points
     */
    public int getBlackPoints() {
        return blackPlayer.getPoints();
    }

    /**
     * Getter for white player's amount of points
     * @return white player's points
     */
    public int getWhitePoints() {
        return whitePlayer.getPoints();
    }

    /**
     * Checks if game is over
     * @return true if game is over (not in running state), false otherwise
     */
    public boolean isOver() {
        return state != GameState.RUNNING;
    }

    /**
     * Generates a game state to be saved to the game care taker
     * @return current game state
     */
    public GameMemento saveState() {
        BoardModel temp = null;
        try {
            temp = (BoardModel) gameBoard.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new GameMemento(temp, turn);
    }

    /**
     * Sets a previous game state as the current one
     * @param state state to be restored
     * @throws CloneNotSupportedException
     */
    public void setPreviousState(GameMemento state) throws CloneNotSupportedException {
        gameBoard = (BoardModel) state.getBoard().clone();
        turn = state.getTurn();
        currentMoves = getValidMoves(getCurrentPlayer());
        updatePoints();
        updateGameState();
    }

    /**
     * Undoes a given number of moves
     * @param n Number of moves to be undone
     */
    public void undoMove(int n) {
        if(caretaker.getSize() == 1)
            return;

        for(int i = 0; i < n; i++) {
            caretaker.removeLast();
            if (caretaker.getSize() == 1)
                break;
        }

        try {
            setPreviousState(caretaker.getLast());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
