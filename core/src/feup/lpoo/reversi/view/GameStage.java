package feup.lpoo.reversi.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.presenter.GamePresenter;
import feup.lpoo.reversi.view.board.BoardView;

public abstract class GameStage extends Stage {
    protected Reversi game;
    protected GamePresenter presenter;

    protected Table hud;
    protected Table paddleTable;
    protected Table boardTable;
    protected Table buttonTable;
    protected Table gameOverTable;
    protected Table winnerTable;

    protected Image blackIcon;
    protected Image whiteIcon;
    protected Image paddle1;
    protected Image paddle2;

    protected Label score1;
    protected Label score2;

    protected BoardView board;

    protected TextButton back;
    protected TextButton restart;
    protected TextButton mainMenu;

    protected Label winner;

    private Dialog exitDialog;

    protected boolean gameOver; //To ensure that gameOver() only runs once per game

    public GameStage(Reversi game, GamePresenter presenter) {
        super(game.getViewport(), game.getBatch());
        gameOver = false;
        this.game = game;
        this.presenter = presenter;
        initElements();
        initExitDialog();
        initTables();
        addElements();
        addToStage();
        addListeners();
    }

    protected void initElements() {
        blackIcon = new Image(game.getAssetManager().get("black.png", Texture.class));
        whiteIcon = new Image(game.getAssetManager().get("white.png", Texture.class));
        paddle1 = new Image(game.getAssetManager().get("paddle.png", Texture.class));
        paddle2 = new Image(game.getAssetManager().get("paddle.png", Texture.class));
        score1 = new Label("02", game.getSkin());
        score2 = new Label("02", game.getSkin());
        board = new BoardView(presenter);
        back = new TextButton("Back", game.getSkin());
        back.setColor(Reversi.SECONDARY_COLOR);
        restart = new TextButton(" Restart ", game.getSkin());
        mainMenu = new TextButton(" Main Menu", game.getSkin());
        winner = new Label("", game.getSkin());
    }

    protected void initTables() {
        paddleTable = new Table();
        hud = new Table();
        boardTable = new Table();
        buttonTable = new Table();
        gameOverTable = new Table();
        winnerTable = new Table();
    }

    protected void addElements() {
        addPaddles();
        addHud();
        addBoard();
        addBackButton();
        addGameOverHud();
    }

    private void initExitDialog(){
        exitDialog = new Dialog("Exit game confirmation", game.getSkin()) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object)
                    game.setScreen(new feup.lpoo.reversi.view.menus.MainMenuView(game));
                else
                    hide(null); //Do NOT remove null argument
            }
        };
        //exitDialog.text("Are you sure you want to quit? You will lose current game progress");
        exitDialog.button("Yes", true).button("No", false);
    }

    private void addPaddles() {
        paddleTable.setFillParent(true);
        paddleTable.top();
        paddleTable.add(paddle1).expandX().padTop(70);
        paddleTable.add(paddle2).expandX().padTop(70);
    }

    private void addHud() {
        hud.setFillParent(true);
        hud.top();
        hud.add(blackIcon).expandX().padTop(75).align(Align.right).padRight(5);
        hud.add(score1).expandX().padTop(75).align(Align.left).padLeft(5);
        hud.add(whiteIcon).expandX().padTop(75).align(Align.right).padRight(5);
        hud.add(score2).expandX().padTop(75).align(Align.left).padLeft(5);
        hud.row();
    }

    private void addBoard() {
        boardTable.setFillParent(true);
        boardTable.add(board).center().expandY();
    }

    private void addBackButton() {
        buttonTable.setFillParent(true);
        buttonTable.bottom();
        buttonTable.row();
        buttonTable.add(back).center().padBottom(20);
    }

    private void addGameOverHud() {
        gameOverTable.setVisible(false);
        gameOverTable.setFillParent(true);
        gameOverTable.bottom();
        gameOverTable.add(restart).expandX().padBottom(50);
        gameOverTable.add(mainMenu).expandX().padBottom(50);

        winnerTable.setFillParent(true);
        winnerTable.setVisible(false);
        winnerTable.bottom().add(winner).center().padBottom(130);
    }

    protected void addToStage() {
        addActor(paddleTable);
        addActor(hud);
        addActor(boardTable);
        addActor(buttonTable);
        addActor(gameOverTable);
        addActor(winnerTable);
    }

    protected void gameOver() {
        if(!gameOver) {
            presenter.playServicesCalls();
            gameOverTable.setVisible(true);
            winnerTable.setVisible(true);
            showResult();
        }
    }

    protected void restartGame() {
        gameOver = false;
        presenter.restartGame();
        gameOverTable.setVisible(false);
        winnerTable.setVisible(false);
    }

    private void showResult() {
        String result = presenter.getResult();
        winner.setText(result);
    }

    @Override
    public void act(float dt) {
        presenter.updateGame();
        updateScore();
        updateTurn();
        board.act(dt);
        if(presenter.gameOver()) {
            gameOver();
            gameOver = true;
        }
    }

    private void updateScore() {
        score1.setText(presenter.getBlackPoints());
        score2.setText(presenter.getWhitePoints());
    }

    private void updateTurn() {
        paddle1.setVisible(presenter.isBlackTurn());
        paddle2.setVisible(presenter.isWhiteTurn());
    }

    private void showExitDialog(){
        exitDialog.show(this);
    }

    protected void addListeners() {

        back.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                showExitDialog();
                return true;
            }
        });

        restart.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                restartGame();
                return true;
            }
        });

        mainMenu.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new feup.lpoo.reversi.view.menus.MainMenuView(game));
                return true;
            }
        });
        Gdx.input.setInputProcessor(this);
    }
}
