import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

import feup.lpoo.reversi.model.AIModel;
import feup.lpoo.reversi.model.BoardModel;
import feup.lpoo.reversi.model.GameModel;
import feup.lpoo.reversi.model.MoveModel;
import feup.lpoo.reversi.model.UserModel;
import feup.lpoo.reversi.presenter.ai.AIPresenter;
import feup.lpoo.reversi.presenter.ai.EasyMoveStrategy;
import feup.lpoo.reversi.presenter.ai.HardMoveStrategy;
import feup.lpoo.reversi.presenter.ai.MediumMoveStrategy;

public class ReversiTest {
    @Test
    public void assertCorrectBoardInitialization(){
        BoardModel board = new BoardModel();
        char[][] startingBoard = board.getCurrentBoard();
        char[][] expectedBoard = {
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','W','B','-','-','-'},
                {'-','-','-','B','W','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
        };

        for(int i = 0; i < 8; i++)
            assertArrayEquals(startingBoard[i], expectedBoard[i]);
    }

    @Test
    public void assertCorrectLegalMovesCalculation(){
        BoardModel board = new BoardModel();
        ArrayList<MoveModel> legalMoves;

        char[][] situation1 = {
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','W','B','-','-','-'},
                {'-','-','-','B','W','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
        };

        board.setBoard(situation1);

        legalMoves = board.getValidMoves('W');
        assertTrue(legalMoves.size() == 4);

        char[][] situation2 = {
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','W','-','-','-','-','B','-'},
                {'-','-','W','B','B','B','-','-'},
                {'-','-','-','W','B','-','-','-'},
                {'-','-','-','B','W','W','-','-'},
                {'-','-','B','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
        };

        board.setBoard(situation2);
        legalMoves = board.getValidMoves('B');
        assertTrue(legalMoves.size() == 6);

        char[][] situation3 = {
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','W','-','-','-','-'},
                {'-','-','B','B','W','B','-','-'},
                {'-','-','-','B','W','W','-','-'},
                {'-','-','-','-','B','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
        };

        board.setBoard(situation3);
        legalMoves = board.getValidMoves('W');
        assertTrue(legalMoves.size() == 11);

        //Actually test if the correct legal moves are being chosen
        ArrayList<MoveModel> actualMoves = new ArrayList<MoveModel>();
        actualMoves.add(new MoveModel(2, 2, 'W'));
        actualMoves.add(new MoveModel(5, 2, 'W'));
        actualMoves.add(new MoveModel(6, 2, 'W'));
        actualMoves.add(new MoveModel(1, 3, 'W'));
        actualMoves.add(new MoveModel(6, 3, 'W'));
        actualMoves.add(new MoveModel(1, 4, 'W'));
        actualMoves.add(new MoveModel(2, 4, 'W'));
        actualMoves.add(new MoveModel(2, 5, 'W'));
        actualMoves.add(new MoveModel(3, 5, 'W'));
        actualMoves.add(new MoveModel(3, 6, 'W'));
        actualMoves.add(new MoveModel(4, 6, 'W'));
        for(MoveModel move : actualMoves)
            assertTrue(legalMoves.indexOf(move) >= 0);
    }

    @Test
    public void assertCorrectTurnAttribution(){
        UserModel player1 = new UserModel('B');
        UserModel player2 = new UserModel('W');
        GameModel game = new GameModel(player1, player2);

        assertTrue(game.getCurrentPlayer() == player1); //Black goes first

        player1.setMove(new MoveModel(3, 2, 'B'));
        game.updateGame();

        assertTrue(game.getCurrentPlayer() == player2); //White goes second

        player2.setMove(new MoveModel(4, 2, 'W'));
        game.updateGame();

        assertTrue(game.getCurrentPlayer() == player1); //Now, black again (and so on, usually)

        //Change board to a pass situation: black to play. If he plays G8, white has no moves and has to pass his turn
        char[][] passSituation = {
                {'B','W','W','W','W','W','W','W'},
                {'B','W','B','B','W','-','-','-'},
                {'B','W','B','B','W','-','-','-'},
                {'B','W','W','W','W','-','-','-'},
                {'B','W','W','W','W','-','-','-'},
                {'B','W','W','W','W','-','-','-'},
                {'B','W','W','B','W','-','-','-'},
                {'B','B','B','B','B','W','-','-'},
        };

        game.getGameBoard().setBoard(passSituation);

        //Getting the move from this method because it also prepares necessary pieces for rotation
        MoveModel nextMove = game.getGameBoard().getValidMove(6, 7, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getGameBoard().getValidMoves('W').size() == 0);
        assertTrue(game.getCurrentPlayer() == player1); //Black's turn once again
    }

    @Test
    public void assertCorrectPieceRotation(){
        UserModel player1 = new UserModel('B');
        UserModel player2 = new UserModel('W');
        GameModel game = new GameModel(player1, player2);
        MoveModel nextMove;

        char[][] situation1 = {
                {'-','-','-','B','-','-','-','-'},
                {'-','-','-','W','B','-','-','-'},
                {'-','-','-','-','W','-','-','-'},
                {'-','W','W','-','W','W','W','B'},
                {'-','-','B','W','W','-','-','-'},
                {'-','W','-','-','-','B','-','-'},
                {'B','-','-','B','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'}
        };

        game.getGameBoard().setBoard(situation1);

        Integer[][] piecesToRotate1 = {
                {4, 3},
                {5, 3},
                {6, 3},
                {4, 4}
        };

        for(Integer[] coordinates : piecesToRotate1)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'W');

        nextMove = game.getGameBoard().getValidMove(3, 3, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        for(Integer[] coordinates : piecesToRotate1)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'B');

        //Perfect situation for testing: white to move and can move for 6(!) straight turns if he follows the sequence: C8, H8, G7, A7, A1, A2
        //We'll test 3 so we don't make the test already bigger than it is
        char[][] situation2 = {
                {'-','B','B','B','B','B','W','B'},
                {'-','-','B','B','B','B','W','B'},
                {'B','B','B','B','B','B','W','B'},
                {'B','B','B','B','B','W','B','B'},
                {'B','B','B','B','W','B','B','B'},
                {'B','B','B','W','W','B','B','B'},
                {'-','B','W','B','B','B','-','B'},
                {'B','W','-','B','B','B','B','-'}
        };

        game.getGameBoard().setBoard(situation2);

        //First Move: C8
        Integer[][] piecesToRotate2 = {
                {3, 6}
        };

        for(Integer[] coordinates : piecesToRotate2)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'B');

        nextMove = game.getGameBoard().getValidMove(2, 7, 'W');
        player2.setMove(nextMove);
        game.updateGame();

        for(Integer[] coordinates : piecesToRotate2)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'W');

        //Second Move: H8
        Integer[][] piecesToRotate3 = {
                {3, 7},
                {4, 7},
                {5, 7},
                {6, 7}
        };

        for(Integer[] coordinates : piecesToRotate3)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'B');

        nextMove = game.getGameBoard().getValidMove(7, 7, 'W');
        player2.setMove(nextMove);
        game.updateGame();

        for(Integer[] coordinates : piecesToRotate3)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'W');

        //Third move: G7
        Integer[][] piecesToRotate4 = {
                {4, 6},
                {5, 6},
                {5, 5},
                {6, 3},
                {6, 4},
                {6, 5}
        };

        for(Integer[] coordinates : piecesToRotate4)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'B');

        nextMove = game.getGameBoard().getValidMove(6, 6, 'W');
        player2.setMove(nextMove);
        game.updateGame();

        for(Integer[] coordinates : piecesToRotate4)
            assertTrue(game.getPieceAt(coordinates[0], coordinates[1]) == 'W');
    }

    @Test
    public void assertCorrectScoreUpdate(){
        UserModel player1 = new UserModel('B');
        UserModel player2 = new UserModel('W');
        GameModel game = new GameModel(player1, player2);
        MoveModel nextMove;

        assertTrue(game.getBlackPoints() == 2 && game.getWhitePoints() == 2); //In the starting position

        char[][] situation1 = {
                {'-','-','-','B','-','-','-','-'},
                {'-','-','-','W','B','-','-','-'},
                {'-','-','-','-','W','-','-','-'},
                {'-','W','W','-','W','W','W','B'},
                {'-','-','B','W','W','-','-','-'},
                {'-','W','-','-','-','B','-','-'},
                {'B','-','-','B','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'}
        };

        game.getGameBoard().setBoard(situation1);

        nextMove = game.getGameBoard().getValidMove(3, 3, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getBlackPoints() == 12 && game.getWhitePoints() == 6);

        nextMove = game.getGameBoard().getValidMove(6, 4, 'W');
        player2.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getBlackPoints() == 11 && game.getWhitePoints() == 8);

        char[][] situation2 = {
                {'-','-','B','B','B','-','-','W'},
                {'-','-','B','-','B','-','W','W'},
                {'-','W','B','B','B','W','W','B'},
                {'-','-','W','B','W','W','W','-'},
                {'-','-','B','B','B','-','-','-'},
                {'-','-','B','B','W','B','W','-'},
                {'-','-','-','-','-','-','B','-'},
                {'-','-','-','-','-','-','-','-'}
        };

        game.getGameBoard().setBoard(situation2);

        nextMove = game.getGameBoard().getValidMove(7, 3, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getBlackPoints() == 21 && game.getWhitePoints() == 9);
    }

    @Test
    public void assertGameCompletionOnNoLegalMoves(){
        UserModel player1 = new UserModel('B');
        UserModel player2 = new UserModel('W');
        GameModel game = new GameModel(player1, player2);
        MoveModel nextMove;

        assertTrue(game.getBlackPoints() == 2 && game.getWhitePoints() == 2); //In the starting position

        char[][] specialSituation = {
                {'B','B','B','B','B','B','B','B'},
                {'B','B','B','B','B','B','B','B'},
                {'B','B','B','B','B','B','B','B'},
                {'B','B','B','B','B','B','B','-'},
                {'B','B','B','B','B','B','-','-'},
                {'B','B','B','B','W','-','-','W'},
                {'B','B','B','B','B','B','B','-'},
                {'B','B','B','B','B','B','B','B'},
        };

        game.getGameBoard().setBoard(specialSituation);

        nextMove = game.getGameBoard().getValidMove(5, 5, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getBlackPoints() == 63 && game.getWhitePoints() == 1);
        assertTrue(game.isOver());
    }
    
    @Test
    public void assertCorrectGameCompletionOnWipeout(){
        UserModel player1 = new UserModel('B');
        UserModel player2 = new UserModel('W');
        GameModel game = new GameModel(player1, player2);
        MoveModel nextMove;
        
        char[][] situation = {
                {'B','B','B','B','B','B','B','B'},
                {'B','B','B','B','B','B','B','B'},
                {'B','B','B','B','B','B','B','B'},
                {'B','B','B','B','B','B','B','B'},
                {'B','W','B','B','B','B','B','B'},
                {'B','B','W','B','B','B','B','B'},
                {'B','B','B','-','B','B','B','B'},
                {'B','B','B','B','-','B','B','B'},
        };

        game.getGameBoard().setBoard(situation);

        nextMove = game.getGameBoard().getValidMove(3, 6, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getBlackPoints() == 64 && game.getWhitePoints() == 0);
        assertTrue(game.isOver());
    }

    @Test
    public void assertCorrectGameCompletionOnFullBoard(){
        UserModel player1 = new UserModel('B');
        UserModel player2 = new UserModel('W');
        GameModel game = new GameModel(player1, player2);
        MoveModel nextMove;

        //Apparently excellent for white... but has lost already!
        char[][] situation = {
                {'-','W','W','W','W','W','W','-'},
                {'W','W','W','W','W','W','W','W'},
                {'W','W','W','W','W','W','W','W'},
                {'W','W','W','W','B','W','W','W'},
                {'W','W','W','W','W','W','W','W'},
                {'W','W','W','W','W','W','W','W'},
                {'W','W','W','W','W','W','W','W'},
                {'-','W','W','W','W','W','W','-'},
        };

        game.getGameBoard().setBoard(situation);

        nextMove = game.getGameBoard().getValidMove(0, 7, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        nextMove = game.getGameBoard().getValidMove(0, 0, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        nextMove = game.getGameBoard().getValidMove(7, 0, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        nextMove = game.getGameBoard().getValidMove(7, 7, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getBlackPoints() == 40 && game.getWhitePoints() == 24);
        assertTrue(game.isOver());
    }

    @Test
    public void assertCorrectPlayUndo(){
        UserModel player1 = new UserModel('B');
        UserModel player2 = new UserModel('W');
        GameModel game = new GameModel(player1, player2);
        char[][] currBoard;
        MoveModel nextMove;

        char[][] initialBoard = game.getGameBoard().getCurrentBoard();

        nextMove = game.getGameBoard().getValidMove(3, 2, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        char[][] boardAfterFirstMove = game.getGameBoard().getCurrentBoard(); //White's turn

        nextMove = game.getGameBoard().getValidMove(4, 2, 'W');
        player2.setMove(nextMove);
        game.updateGame();

        char[][] boardAfterSecondMove = game.getGameBoard().getCurrentBoard(); //Black's turn

        nextMove = game.getGameBoard().getValidMove(5, 1, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getCurrentPlayer() == player2); //Assure it's white's turn

        //Go mainMenu 1 play
        game.undoMove(1);
        currBoard = game.getGameBoard().getCurrentBoard();
        for(int i = 0; i < 8; i++)
            assertArrayEquals(currBoard[i], boardAfterSecondMove[i]);
        assertTrue(game.getCurrentPlayer() == player1);

        //Make another play to compensate the one we just went mainMenu from
        nextMove = game.getGameBoard().getValidMove(5, 5, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        assertTrue(game.getCurrentPlayer() == player2);

        //Go mainMenu even further
        game.undoMove(2);
        currBoard = game.getGameBoard().getCurrentBoard();
        for(int i = 0; i < 8; i++)
            assertArrayEquals(currBoard[i], boardAfterFirstMove[i]);
        assertTrue(game.getCurrentPlayer() == player2);

        game.undoMove(2); //Back to the initial state
        currBoard = game.getGameBoard().getCurrentBoard();

        for(int i = 0; i < 8; i++)
            assertArrayEquals(currBoard[i], initialBoard[i]);

        assertTrue(game.getCurrentPlayer() == player1);
    }

    @Test(timeout=5000)
    public void assertCorrectEasyAIMoveChoice(){
        UserModel player1 = new UserModel('B');
        AIPresenter player2Presenter = new AIPresenter(new EasyMoveStrategy());
        AIModel player2 = new AIModel('W', player2Presenter);
        GameModel game = new GameModel(player1, player2);
        player2Presenter.setGame(game);

        MoveModel nextMove;
        ArrayList<MoveModel> validMovesATM;

        nextMove = game.getGameBoard().getValidMove(2, 3, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        validMovesATM = game.getCurrentMoves();
        while(!player2.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertTrue(validMovesATM.indexOf(player2.getMove()) >= 0);
    }

    @Test(timeout=5000)
    public void assertCorrectMediumAIMoveChoice(){
        UserModel player1 = new UserModel('B');
        player1.setReady();
        AIPresenter player2Presenter = new AIPresenter(new MediumMoveStrategy());
        AIModel player2 = new AIModel('W', player2Presenter);
        GameModel game = new GameModel(player1, player2);
        player2Presenter.setGame(game);

        MoveModel nextMove;

        char[][] startingSituation = {
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','W','-'},
                {'-','B','W','-','B','B','B','-'},
                {'-','W','B','W','B','B','B','-'},
                {'-','-','W','W','W','B','B','-'},
                {'-','-','W','W','W','W','-','-'},
                {'-','-','W','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
        };

        game.getGameBoard().setBoard(startingSituation);

        nextMove = game.getGameBoard().getValidMove(5, 6, 'B');
        if(player1.isReady())
            player1.setMove(nextMove);
        game.updateGame();

        while(!player2.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(player2.getMove().equals(new MoveModel(0, 2, 'W')));

        nextMove = game.getGameBoard().getValidMove(2, 7, 'B');
        if(player1.isReady())
            player1.setMove(nextMove);
        game.updateGame();

        while(!player2.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        player1.resetReady();

        //Both moves have the same board coefficient so he could choose either
        assertTrue(player2.getMove().equals(new MoveModel(7, 3, 'W')) ||
                    player2.getMove().equals(new MoveModel(7, 4, 'W')));
    }

    @Test
    public void assertCorrectHardAIMoveChoice(){
        UserModel player1 = new UserModel('B');
        player1.setReady();
        AIPresenter player2Presenter = new AIPresenter(new HardMoveStrategy());
        AIModel player2 = new AIModel('W', player2Presenter);
        GameModel game = new GameModel(player1, player2);
        player2Presenter.setGame(game);

        MoveModel nextMove;

        char[][] startingSituation = {
                {'-','-','-','-','W','-','-','-'},
                {'-','-','-','-','W','-','-','-'},
                {'-','-','W','B','W','-','-','-'},
                {'-','-','B','B','W','-','-','-'},
                {'-','-','-','B','W','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
        };

        game.getGameBoard().setBoard(startingSituation);

        nextMove = game.getGameBoard().getValidMove(5, 3, 'B');
        player1.setMove(nextMove);
        game.updateGame();

        while(!player2.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(player2.getMove().equals(new MoveModel(6, 2, 'W')));

        nextMove = game.getGameBoard().getValidMove(5, 0, 'B');
        if(player1.isReady())
            player1.setMove(nextMove);
        game.updateGame();

        while(!player2.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(player2.getMove().equals(new MoveModel(1, 3, 'W')));
    }
}