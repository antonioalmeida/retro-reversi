package feup.lpoo.reversi.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.presenter.ai.EasyMoveStrategy;
import feup.lpoo.reversi.presenter.ai.HardMoveStrategy;
import feup.lpoo.reversi.presenter.ai.MediumMoveStrategy;
import feup.lpoo.reversi.view.GameInfo;
import feup.lpoo.reversi.view.GameView;

public class DifficultyMenuView extends ScreenAdapter {
    private Reversi game;

    private Stage stage;
    private Table buttonTable;
    private Table titleTable;
    private Table pieceTable;
    private Table backButtonTable;

    private TextButton randomAIButton;
    private TextButton immediateAIButton;
    private TextButton calculatedAIButton;
    private TextButton backButton;

    private CheckBox blackCheckBox;
    private CheckBox whiteCheckBox;

    private ButtonGroup<CheckBox> pieceChoiceGroup;

    //Labels
    private Label mainTitle;

    public DifficultyMenuView(Reversi game) {
        this.game = game;
        stage = new Stage(game.getViewport(), game.getBatch());

        addTitle();
        addPieceChoice();
        addChoiceButtons();
        addListeners();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(Reversi.BACKGROUND_COLOR.r, Reversi.BACKGROUND_COLOR.g, Reversi.BACKGROUND_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        stage.draw();
    }

    private void addTitle() {
        mainTitle = new Label("Difficulty", game.getSkin());
        mainTitle.setFontScale(2);

        titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.top();
        titleTable.add(mainTitle).center().padTop(70);

        stage.addActor(titleTable);
    }

    private void addChoiceButtons() {
        randomAIButton = new TextButton("\n  Easy  \n", game.getSkin());
        immediateAIButton = new TextButton("\n  Medium  \n", game.getSkin());
        calculatedAIButton = new TextButton("\n  Hard  \n", game.getSkin());
        backButton = new TextButton(" Back ", game.getSkin());
        backButton.setTransform(true);
        backButton.setColor(Reversi.SECONDARY_COLOR);

        buttonTable.add(randomAIButton).center().padBottom(40);
        buttonTable.row();
        buttonTable.add(immediateAIButton).center().padBottom(40);
        buttonTable.row();
        buttonTable.add(calculatedAIButton).center().padBottom(40);
        buttonTable.row();
        buttonTable.add(backButton).center().padBottom(40);

        stage.addActor(buttonTable);
    }

    private void addPieceChoice(){
        buttonTable = new Table();
        buttonTable.bottom();
        buttonTable.setFillParent(true);

        pieceChoiceGroup = new ButtonGroup<CheckBox>();
        whiteCheckBox = new CheckBox(" White", game.getSkin());
        blackCheckBox = new CheckBox(" Black", game.getSkin());
        pieceChoiceGroup.add(blackCheckBox);
        pieceChoiceGroup.add(whiteCheckBox);
        pieceChoiceGroup.setMaxCheckCount(1);
        pieceChoiceGroup.setMinCheckCount(1);
        pieceChoiceGroup.setUncheckLast(true);
        pieceChoiceGroup.setChecked("Black");

        buttonTable.add(whiteCheckBox).center().padBottom(15).row();
        buttonTable.add(blackCheckBox).center().padBottom(40).row();
    }

    private void addListeners() {
        randomAIButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameInfo info = new GameInfo(true, pieceChoiceGroup.getCheckedIndex() == 0, new EasyMoveStrategy());
                game.setScreen(new GameView(game, info));
                return true;
            }
        });

        immediateAIButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameInfo info = new GameInfo(true, pieceChoiceGroup.getCheckedIndex() == 0, new MediumMoveStrategy());
                game.setScreen(new GameView(game, info));
                return true;
            }
        });

        calculatedAIButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameInfo info = new GameInfo(true, pieceChoiceGroup.getCheckedIndex() == 0, new HardMoveStrategy());
                game.setScreen(new GameView(game, info));
                return true;
            }
        });

        backButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuView(game));
                return true;
            }
        });
    }
}
