package feup.lpoo.reversi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import feup.lpoo.reversi.view.GameInfo;
import feup.lpoo.reversi.view.GameView;
import feup.lpoo.reversi.view.menus.MainMenuView;

public class Reversi extends Game {
	private PlayServices playServices;

	private SpriteBatch batch;
	public static AssetManager assetManager;
	private Viewport viewport;

	private Skin skin;
	private TextureAtlas atlas;

    public static Color BACKGROUND_COLOR = new Color(0.21f, 0.28f, 0.47f, 1);
    public static Color SECONDARY_COLOR = new Color(0.5f, 0.8f, -0.5f, 1);

	public Reversi(PlayServices ps) {
		this.playServices = ps;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
        assetManager = new AssetManager();
		viewport = new ExtendViewport(512, 854);

        atlas = new TextureAtlas("retro-normal-font/retro-normal-font.atlas");
        skin = new Skin(Gdx.files.internal("retro-normal-font/retro-normal-font.json"), atlas);
		loadAssets();

        playServices.signIn();
		setScreen(new MainMenuView(this));
	}

	@Override
	public void render () {
        super.render();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        assetManager.dispose();
        atlas.dispose();
		skin.dispose();
	}

	public void setOnlineMatchScreen() {
		GameInfo info = new GameInfo(false, true);
		setScreen(new GameView(this, info));
	}

	private void loadAssets() {
		assetManager.load("tile.png", Texture.class);
		assetManager.load("white.png", Texture.class);
		assetManager.load("black.png", Texture.class);
		assetManager.load("hint.png", Texture.class);
		assetManager.load("paddle.png", Texture.class);
		assetManager.load("rotation/black-rotation.png", Texture.class);
		assetManager.load("rotation/white-rotation.png", Texture.class);
		assetManager.finishLoading();
	}

	public Viewport getViewport() {
		return viewport;
	}

	public Skin getSkin() {
		return skin;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public PlayServices getPlayServices() {
		return playServices;
	}
}
