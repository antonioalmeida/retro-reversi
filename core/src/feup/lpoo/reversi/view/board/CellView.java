package feup.lpoo.reversi.view.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.HashMap;
import java.util.Map;

import feup.lpoo.reversi.Reversi;
import feup.lpoo.reversi.presenter.GamePresenter;

public class CellView extends Actor {
    private Texture regionBlack = Reversi.assetManager.get("rotation/black-rotation.png");
    private Texture regionWhite = Reversi.assetManager.get("rotation/white-rotation.png");

    private TextureRegion[][] blackFramesTmp = TextureRegion.split(regionBlack, 64, 64);
    private TextureRegion[][] whiteFramesTmp = TextureRegion.split(regionWhite, 64, 64);

    private TextureRegion[] blackFrames = new TextureRegion[7];
    private TextureRegion[] whiteFrames = new TextureRegion[7];

    private Animation<TextureRegion> blackAnimation;
    private Animation<TextureRegion> whiteAnimation;

    private final Texture tile = Reversi.assetManager.get("tile.png");
    private final Texture black = Reversi.assetManager.get("black.png");
    private final Texture white = Reversi.assetManager.get("white.png");
    private final Texture suggestion = Reversi.assetManager.get("hint.png");

    private TextureRegion currentFrame = null;

    private Map<Character, Texture> ICONS = new HashMap<Character, Texture>();

    private GamePresenter presenter;

    private char previousPiece;
    private int animation;

    private final int NO_ANIMATION = 0;
    private final int BLACK_ANIMATION = 1;
    private final int WHITE_ANIMATION = 2;
    private final float ANIMATION_TIME = 0.63f;

    private Texture icon;

    private int actorX;
    private int actorY;

    private int boardX;
    private int boardY;

    private float stateTime = 0;

    public CellView(GamePresenter presenter, int x, int y, int boardX, int boardY) {
        this.presenter = presenter;
        actorX = x;
        actorY = y;
        this.boardX = boardX;
        this.boardY = boardY;
        setBounds(actorX, actorY, tile.getWidth(), tile.getHeight());
        previousPiece = presenter.getCurrentPiece(boardX, boardY);
        addListeners();

        ICONS.put('B', black);
        ICONS.put('W', white);
        ICONS.put('X', suggestion);
        ICONS.put('-', null);

        System.arraycopy(blackFramesTmp[0], 0, blackFrames, 0, 7);
        System.arraycopy(whiteFramesTmp[0], 0, whiteFrames, 0, 7);
        blackAnimation = new Animation<TextureRegion>(0.09f, blackFrames);
        whiteAnimation = new Animation<TextureRegion>(0.09f, whiteFrames);
    }

    private Texture getIcon(char piece) {
        Texture result = ICONS.get(piece);
        return result;
    }

    public void addListeners() {
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    presenter.handleInput(boardX, boardY);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    @Override
    public void act(float dt) {
        char currentPiece = presenter.getCurrentPiece(boardX, boardY);

        if(animation != NO_ANIMATION)
            stateTime += dt;

        if(previousPiece == 'B' && currentPiece == 'W')
            animation = BLACK_ANIMATION;
        else if(previousPiece == 'W' && currentPiece == 'B')
            animation = WHITE_ANIMATION;

        icon = getIcon(currentPiece);
        previousPiece = currentPiece;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(tile,actorX,actorY);

        if(icon != null && animation == NO_ANIMATION)
            batch.draw(icon, actorX, actorY);

        if(animation == BLACK_ANIMATION)
            currentFrame = blackAnimation.getKeyFrame(stateTime, true);
        else if(animation == WHITE_ANIMATION)
            currentFrame = whiteAnimation.getKeyFrame(stateTime, true);

        if(stateTime > ANIMATION_TIME) {
            stateTime = 0;
            animation = 0;
            currentFrame = null;
        }

        if(currentFrame != null)
            batch.draw(currentFrame, actorX, actorY);
    }
}
