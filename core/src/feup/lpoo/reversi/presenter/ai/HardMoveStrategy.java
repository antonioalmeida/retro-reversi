package feup.lpoo.reversi.presenter.ai;

import java.util.ArrayList;

import feup.lpoo.reversi.model.BoardModel;
import feup.lpoo.reversi.model.MoveModel;

/**
 * AI strategy implementation that uses negamax algorithm to find a move
 */
public class HardMoveStrategy implements CalculatedAIMoveStrategy {
    /**
     * Maximum depth for the negamax algorithm
     */
    private int maxDepth;

    /**
     * Default constructor, initializes maxDepth field to a default value
     */
    public HardMoveStrategy() {
        maxDepth = 4;
    }

    /**
     * Calls the negamax function with the specified board and piece
     * @param board Current game board
     * @param piece AI's piece in-game
     * @return Move chosen
     * @see negamax
     */
    @Override
    public MoveModel findMove(BoardModel board, char piece) {
        return negamax(board, maxDepth, piece).getMove();
    }

    /**
     * Evaluates the current board according to the pre-defined coefficient matrix
     * @param board Board to be evaluated
     * @param piece Current player's piece
     * @return Total score attributed
     */
    public int evaluateBoard(char[][] board, char piece) {
        int score = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (board[i][j] == piece)
                    score += BOARD_VALUE[i][j];
                else if (board[i][j] != '-')
                    score -= BOARD_VALUE[i][j];
            }
        }
        return score;
    }

    /**
     * Local class used to store a move and its score (final effect on the board)
     */
    class ScoredMove{
        /**
         * The move
         */
        private MoveModel move;
        /**
         * The move's score
         */
        private int score;

        /**
         * Constructor that initializes both fields
         * @param move
         * @param score
         */
        public ScoredMove(MoveModel move, int score){
            this.move = move;
            this.score = score;
        }

        /**
         * Getter for the move's score
         * @return score
         */
        public int getScore(){
            return score;
        }

        /**
         * Getter for the move
         * @return move
         */
        public MoveModel getMove(){
            return move;
        }
    }

    /**
     * Negamax based (no alpha-beta pruning) algorithm function to choose the best move within a certain move depth
     * @param board Current board
     * @param depth Maximum move analysis depth
     * @param piece Current player's piece
     * @return Move with most positive effect for the current player
     */
    private ScoredMove negamax(BoardModel board, int depth, char piece){
        char oppPiece = (piece == 'B' ? 'W' : 'B');
        ArrayList<MoveModel> currentValidModes = board.getValidMoves(piece);

        if (depth == 0)
            return new ScoredMove(null, evaluateBoard(board.getCurrentBoard(), piece));

        int currentScore;
        int bestScore = Integer.MIN_VALUE;

        if (currentValidModes.isEmpty())
            return new ScoredMove(null, bestScore);
        MoveModel bestMove = currentValidModes.get(0);

        for(MoveModel move : currentValidModes){
            BoardModel newBoard = null;
            try {
                newBoard = (BoardModel) board.clone();
            }
            catch(CloneNotSupportedException e) {
                System.out.println("RIP Board cloning");
                return null;
            }

            //Recursive call
            newBoard.setPieceAt(move.getX(), move.getY(), move.getPiece());
            ScoredMove current = negamax(newBoard, depth - 1, oppPiece);

            //Negamax principle: The best move for player A is the worst for player B
            currentScore = -current.getScore();

            // Update bestScore
            if (currentScore>bestScore){
                    bestScore = currentScore;
                    bestMove = move;
            }
        }
        return new ScoredMove(bestMove,bestScore);
    }
}