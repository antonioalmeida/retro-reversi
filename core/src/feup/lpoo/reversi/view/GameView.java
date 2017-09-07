package feup.lpoo.reversi.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.presenter.LocalMultiplayerGamePresenter;
import feup.lpoo.reversi.presenter.OnlineMultiplayerGamePresenter;
import feup.lpoo.reversi.presenter.SinglePlayerGamePresenter;
import feup.lpoo.reversi.presenter.ai.AIMoveStrategy;
import feup.lpoo.reversi.presenter.GamePresenter;

public class GameView extends ScreenAdapter {
    private GamePresenter presenter;

    private GameStage stage;

    public GameView(Reversi game, GameInfo info) {
        if(info.isOnline()) {
            presenter = new OnlineMultiplayerGamePresenter(game);
            stage = new OnlineGameStage(game, presenter);
        }
        else {
            if(info.isSinglePlayer())
                presenter = new SinglePlayerGamePresenter(game, info.getStrategy(), info.userIsBlack());
            else
                presenter = new LocalMultiplayerGamePresenter(game);

            stage = new LocalGameStage(game, presenter);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(Reversi.BACKGROUND_COLOR.r, Reversi.BACKGROUND_COLOR.g, Reversi.BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }
}
