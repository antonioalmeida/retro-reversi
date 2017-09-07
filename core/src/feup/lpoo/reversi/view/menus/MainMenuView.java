package feup.lpoo.reversi.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import feup.lpoo.reversi.Reversi;

public class MainMenuView extends ScreenAdapter {
    private Reversi game;

    private Stage stage;
    private Table buttonTable;
    private Table titleTable;

    private TextButton singlePlayer;
    private TextButton multiPlayer;
    private TextButton achievements;
    private TextButton sign;

    private Label mainTitle;

    public MainMenuView(Reversi game) {
        this.game = game;
        stage = new Stage(game.getViewport(), game.getBatch());

        addTitle();
        addButtons();
        addListeners();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Reversi.BACKGROUND_COLOR.r, Reversi.BACKGROUND_COLOR.g, Reversi.BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        stage.draw();
    }

    private void addTitle() {
        mainTitle = new Label("  Retro \nReversi", game.getSkin());
        mainTitle.setFontScale(2);

        titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.top();
        titleTable.add(mainTitle).expandX().padTop(100);

        stage.addActor(titleTable);
    }

    private void addButtons() {
        buttonTable = new Table();
        buttonTable.bottom();
        buttonTable.setFillParent(true);

        singlePlayer = new TextButton("\n  Single Player  \n", game.getSkin());
        multiPlayer = new TextButton("\n  Multi Player  \n", game.getSkin());
        achievements = new TextButton("\n  Achievements  \n", game.getSkin());
        sign = new TextButton(game.getPlayServices().isSignedIn() ?  "  Sign Out  " : "  Sign In  ", game.getSkin());
        sign.setColor(game.SECONDARY_COLOR);

        buttonTable.add(singlePlayer).center().padBottom(40);
        buttonTable.row();
        buttonTable.add(multiPlayer).center().padBottom(40);
        buttonTable.row();
        buttonTable.add(achievements).center().padBottom(40);
        buttonTable.row();
        buttonTable.add(sign).center().padBottom(40);

        stage.addActor(buttonTable);
    }

    private void addListeners() {

        singlePlayer.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new DifficultyMenuView(game));
                return true;
            }
        });

        multiPlayer.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MultiplayerMenuView(game));
                return true;
            }
        });

        achievements.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.getPlayServices().isSignedIn())
                    game.getPlayServices().showAchievements();
                else
                    game.getPlayServices().signIn();
                return true;
            }
        });

        sign.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(game.getPlayServices().isSignedIn()) {
                    game.getPlayServices().signOut();
                    sign.setText("  Sign In  ");
                }
                else {
                    game.getPlayServices().signIn();
                    sign.setText("  Sign Out  ");
                }
                return true;
            }
        });


    }
}
