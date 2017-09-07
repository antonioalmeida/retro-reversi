package feup.lpoo.reversi.view.board;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import feup.lpoo.reversi.presenter.GamePresenter;


public class BoardView extends Group {

    private final int IMAGE_SIZE = 64;
    private final int BOARD_SIZE = 8;
    private final int BOARD_WIDTH = (IMAGE_SIZE * BOARD_SIZE) / 2;

    public BoardView(GamePresenter presenter) {

        for(int y = 0; y < BOARD_SIZE; y++) {
            for(int x = 0; x < BOARD_SIZE; x++) {
                Actor cell = new CellView(presenter, IMAGE_SIZE * x - BOARD_WIDTH, IMAGE_SIZE * y - BOARD_WIDTH, x, (BOARD_SIZE - 1) - y) ;
                addActor(cell);
            }
        }

    }

}
