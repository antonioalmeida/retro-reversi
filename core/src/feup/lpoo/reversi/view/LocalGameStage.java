package feup.lpoo.reversi.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.presenter.GamePresenter;

/**
 * Created by antonioalmeida on 29/05/2017.
 */

public class LocalGameStage extends GameStage {

    private TextButton undo;

    public LocalGameStage(Reversi game, GamePresenter presenter) {
        super(game, presenter);
    }

    @Override
    public void initElements() {
        super.initElements();
        undo = new TextButton("Undo", game.getSkin());
    }

    @Override
    public void addElements() {
        addUndo();
        super.addElements();
    }

    private void addUndo() {
        buttonTable.add(undo).center().padBottom(40);
    }

    @Override
    public void gameOver() {
        super.gameOver();
        if(!gameOver) {
            undo.setVisible(false);
            back.setVisible(false);
        }
    }

    @Override
    public void restartGame() {
        super.restartGame();
        undo.setVisible(true);
    }

    @Override
    public void addListeners() {
        super.addListeners();

        undo.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                presenter.screenAction();
                return true;
            }
        });


    }
}
