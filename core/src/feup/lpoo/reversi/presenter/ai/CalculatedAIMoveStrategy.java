package feup.lpoo.reversi.presenter.ai;


/**
 *  AI Strategy interface that takes analyzes the board according to the piece's positions
 *  @see AIMoveStrategy
 */
public interface CalculatedAIMoveStrategy extends AIMoveStrategy {
    /**
     *  Coefficient matrix used to evaluate a board status (higher values means better position)
     */
    int[][] BOARD_VALUE = {
            {100, -1, 5, 2, 2, 5, -1, 100},
            {-1, -10,1, 1, 1, 1,-10, -1},
            {5 , 1,  1, 1, 1, 1,  1,  5},
            {2 , 1,  1, 0, 0, 1,  1,  2},
            {2 , 1,  1, 0, 0, 1,  1,  2},
            {5 , 1,  1, 1, 1, 1,  1,  5},
            {-1,-10, 1, 1, 1, 1,-10, -1},
            {100, -1, 5, 2, 2, 5, -1, 100}
    };
}
