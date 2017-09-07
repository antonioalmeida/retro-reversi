package feup.lpoo.reversi.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a game board
 */
public class BoardModel implements Cloneable, Serializable {
    /**
     * The board itself
     */
    private char[][] board;

    /**
     * Suggestions (actually the valid moves) to be shown to current player, if human
     */
    private char[][] suggestions;

    /**
     * Every possible direction in which to test if a generic move is valid
     */
    private static final int[][] directions = {
            {-1,-1},
            {-1,0},
            {-1,1},
            {0,-1},
            {0,1},
            {1,-1},
            {1,0},
            {1,1}
    };

    /**
     * Constructor, initializes both boards
     */
    public BoardModel() {
        board = generateMatrix(GameModel.BOARD_SIZE, GameModel.BOARD_SIZE);
        suggestions = generateSuggestions(GameModel.BOARD_SIZE, GameModel.BOARD_SIZE);
    }

    /**
     * Places a piece in the game board
     * @param x piece's x-coordinate
     * @param y piece's y-coordinate
     * @param piece piece's color
     */
    public void setPieceAt(int x, int y, char piece) {
        if(x < GameModel.BOARD_SIZE && y < GameModel.BOARD_SIZE)
            board[y][x] = piece;
    }

    /**
     * Checks if given coordinates are within the board's bounds
     * @param x coordinates' x-value
     * @param y coordinates' y-value
     * @return true if both values lie within [0,7], false otherwise
     */
    public boolean validPosition(int x, int y) {
        if(x < GameModel.BOARD_SIZE && y < GameModel.BOARD_SIZE && x >= 0 && y >= 0)
            return true;

        return false;
    }

    /**
     * Getter for a piece on the game board
     * @param x piece's x-coordinate
     * @param y piece's y-coordinate
     * @return piece color at given position (may return empty piece if empty position)
     */
    public char getPieceAt(int x, int y) {
        if(validPosition(x,y))
            return board[y][x];

        return GameModel.EMPTY_PIECE;
    }

    /**
     * Getter for a suggestion (valid move) on the suggestions board
     * @param x piece's x-coordinate
     * @param y piece's y-coordinates
     * @return piece color at given position (may return empty if coordinates do not match a currently valid move)
     */
    public char getSuggestionAt(int x, int y) {
        if (validPosition(x, y))
            return suggestions[y][x];

        return GameModel.EMPTY_PIECE;
    }

    /**
     * Changes a piece's color to the opponent's piece color
     * @param x piece's x-coordinate
     * @param y piece's y-coordinate
     */
    public void rotatePiece(int x, int y) {
        if(getPieceAt(x,y) == GameModel.EMPTY_PIECE)
            return;

        if(getPieceAt(x,y) == GameModel.BLACK_PIECE)
            setPieceAt(x,y, GameModel.WHITE_PIECE);
        else
            setPieceAt(x,y, GameModel.BLACK_PIECE);
    }

    /**
     * Collects the currently valid moves for the specified piece
     * @param piece piece's color
     * @return list of valid moves at that moment
     */
    public ArrayList<MoveModel> getValidMoves(char piece) {
        ArrayList<MoveModel> result = new ArrayList<MoveModel>();

        for (int y = 0; y < GameModel.BOARD_SIZE; y++) {
            for (int x = 0; x < GameModel.BOARD_SIZE; x++) {
                MoveModel temp = getValidMove(x, y, piece);

                if (temp != null)
                    result.add(temp);
            }
        }

        return result;
    }

    /**
     * Checks if a move at given coordinates is valid and returns it if so
     * @param x move's x-coordinate
     * @param y move's y-coordinate
     * @param piece move's piece's color
     * @return the move in a MoveModel object (containing its effects on the board) if it's valid, null otherwise
     */
    public MoveModel getValidMove(int x, int y, char piece) {

        if(getPieceAt(x,y) != GameModel.EMPTY_PIECE)
            return null;

        MoveModel result = new MoveModel(x,y,piece);

        boolean isValidMove = false;
        for(int i = 0; i < directions.length; i++)
            if(checkValidDirection(result, directions[i]))
                isValidMove = true;

        if(isValidMove)
            return result;

        return null;
    }

    /**
     * Checks if a move is valid in a given direction and stores to-be rotated pieces if so
     * @param move the move to be tested
     * @param direction the direction to be tested
     * @return true if move is valid in given direction (rotates at least one piece in it), false otherwise
     */
    public boolean checkValidDirection(MoveModel move, int[] direction) {
        char piece = move.getPiece();
        int x = move.getX();
        int y = move.getY();

        char oppositePiece = (piece == GameModel.BLACK_PIECE) ? GameModel.WHITE_PIECE : GameModel.BLACK_PIECE;

        boolean hasOppPieceBetween = false;
        boolean validDirection =  false;

        char currentPiece = piece;

        ArrayList<Integer[]> changedPositions = new ArrayList<Integer[]>();

        int index = 1;
        while(currentPiece != GameModel.EMPTY_PIECE) {
            int currentX = x + index * direction[0];
            int currentY = y + index * direction[1];

            currentPiece = getPieceAt(currentX, currentY);

            if(currentPiece == piece && hasOppPieceBetween) {
                validDirection = true;
                move.addChangedPositions(changedPositions);
                break;
            }

            Integer[] currentPos = {currentX, currentY};
            changedPositions.add(currentPos);

            if(currentPiece == oppositePiece)
                hasOppPieceBetween = true;
            else break; //If currentPiece == empty or == piece, but hasOppPiece == false

            index++;
        }

        return validDirection;
    }

    /**
     * Evaluates the number of pieces of a given color currently on the game board
     * @param piece piece's color
     * @return amount of pieces of that color on the board
     */
    public int getCurrentPoints(char piece) {
        int result = 0;

        for(char[] line : board) {
            for(char elem : line)
                if(elem == piece)
                    result++;
        }

        return result;
    }

    /**
     * Setter for the suggestions (valid moves) board
     * @param moves valid moves (suggestions) to be shown in the next turn
     */
    public void setSuggestions(ArrayList<MoveModel> moves) {
        suggestions = generateSuggestions(GameModel.BOARD_SIZE, GameModel.BOARD_SIZE);
        for(MoveModel elem : moves)
            suggestions[elem.getY()][elem.getX()] = GameModel.SUGGESTION_PIECE;
    }

    /**
     * Getter for a clone of the current game board
     * @return char matrix containing current game board
     */
    public char[][] getCurrentBoard() {
        char[][] temp = new char[GameModel.BOARD_SIZE][GameModel.BOARD_SIZE];

        int index = 0;

        for(char[] line : board)
            temp[index++] = line.clone();

        return temp;
    }

    /**
     * Getter for a clone of the current suggestions board
     * @return char matrix containing current suggestions board
     */
    public char[][] getCurrentSuggestions() {
        char[][] temp = new char[GameModel.BOARD_SIZE][GameModel.BOARD_SIZE];

        int index = 0;

        for(char[] line : suggestions)
            temp[index++] = line.clone();

        return temp;
    }

    /**
     * Generates a game board on its initial state
     * @param width board with
     * @param height board height
     * @return new game board
     */
    private char[][] generateMatrix(int width, int height) {
        char[][] temp = new char[height][width];

        for (char[] line : temp) {
            for (int i = 0; i < line.length; i++) {
                line[i] = GameModel.EMPTY_PIECE;
            }
        }

        temp[3][3] = GameModel.WHITE_PIECE;
        temp[3][4] = GameModel.BLACK_PIECE;
        temp[4][3] = GameModel.BLACK_PIECE;
        temp[4][4] = GameModel.WHITE_PIECE;

        return temp;
    }

    /**
     * Generates an empty board
     * @param width board width
     * @param height board height
     * @return new empty board
     */
    private char[][] generateSuggestions(int width, int height) {
        char[][] temp = new char[height][width];

        for (char[] line : temp) {
            for (int i = 0; i < line.length; i++) {
                line[i] = GameModel.EMPTY_PIECE;
            }
        }

        return temp;
    }

    /**
     * Clones an object of the type
     * @return a clone of the current object
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        BoardModel temp = (BoardModel) super.clone();

        temp.setBoard(getCurrentBoard());
        temp.setSuggestions(getCurrentSuggestions());

        return temp;
    }

    /**
     * Setter for the game board
     * @param board new game board
     */
    public void setBoard(char[][] board) {
        this.board = board;
    }

    /**
     * Setter for the suggestions board (used in the cloning process)
     * @param suggestions
     */
    private void setSuggestions(char[][] suggestions) {
        this.suggestions = suggestions;
    }
}

