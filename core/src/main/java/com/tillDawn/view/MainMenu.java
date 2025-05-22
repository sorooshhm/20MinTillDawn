package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.model.App;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.User;
import com.tillDawn.utilities.BiLingualHandling;
import com.tillDawn.utilities.SoundInputProcessor;

public class MainMenu implements Screen {
    private final Main main;
    private Stage stage;
    private Skin skin;
    private TextureRegion[] eyeFrames;
    private Animation<TextureRegion> eyeAnimation;
    private float stateTime;

    private static final float FRAME_DURATION = 0.1f;
    private static final float BLINK_INTERVAL = 5.0f; // Time between blinks
    private float timeSinceLastBlink = 0;
    private boolean isBlinking = false;

    private float middleTreeX, middleTreeY;
    private float leftTreeX, leftTreeY;
    private float rightTreeX, rightTreeY;
    private BiLingualHandling lang = BiLingualHandling.getInstance();

    public MainMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        setStage();
        eyeFrames = new TextureRegion[3];
        eyeFrames[0] = new TextureRegion(GameAssets.OpenEyeTexture);
        eyeFrames[1] = new TextureRegion(GameAssets.HalfClosedEyeTexture);
        eyeFrames[2] = new TextureRegion(GameAssets.ClosedEyeTexture);

        eyeAnimation = new Animation<>(FRAME_DURATION,
            eyeFrames[0], eyeFrames[1], eyeFrames[2], eyeFrames[1], eyeFrames[0]);

        stateTime = 0;
        createUI();
    }

    private void calculateTreePositions() {
        middleTreeX = Gdx.graphics.getWidth() * 0.5f - GameAssets.MainMenuTitle.getWidth() * 0.7f;
        middleTreeY = Gdx.graphics.getHeight() * 0.85f;

        rightTreeX = 0f;
        rightTreeY = 0f;

        leftTreeX = Gdx.graphics.getWidth() - GameAssets.MainMenuLeaves2.getWidth() * 2f;
        leftTreeY = 0;
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        buttonStyle.font.getData().setScale(1.2f); // Make buttons slightly larger

        TextButton settingsButton = new TextButton(lang.getMessage("SETTINGS"), skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new SettingMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        table.add(settingsButton).width(450).height(80).padBottom(10).row();

        TextButton profileButton = new TextButton(lang.getMessage("PROFILE_MENU"), skin);
        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new ProfileMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        table.add(profileButton).width(450).height(80).padBottom(10).row();

        TextButton preGameButton = new TextButton(lang.getMessage("PRE_GAME_MENU"), skin);
        preGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new PreGameMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        table.add(preGameButton).width(450).height(80).padBottom(10).row();

        TextButton scoreboardButton = new TextButton(lang.getMessage("SCORE_BOARD"), skin);
        scoreboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new ScoreboardMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        table.add(scoreboardButton).width(450).height(80).padBottom(10).row();

        TextButton hintButton = new TextButton(lang.getMessage("HINT_MENU"), skin);
        hintButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new HintMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        table.add(hintButton).width(450).height(80).padBottom(10).row();

        TextButton loadButton = new TextButton(lang.getMessage("LOAD_GAME"), skin);
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                App.setCurrScreen(new GameMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        User user = App.getCurrUser();
        if (!user.isGuest() && user.getCurrentGame() != null && !user.getCurrentGame().isFinished())
            table.add(loadButton).width(450).height(80).padBottom(20).row();
        TextButton exitButton = new TextButton(lang.getMessage("EXIT"), skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitButton).width(200).height(80);

    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new SoundInputProcessor(GameAssets.clickSound));
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        timeSinceLastBlink += delta;

        if (timeSinceLastBlink >= BLINK_INTERVAL) {
            isBlinking = true;
            timeSinceLastBlink = 0;
            stateTime = 0;
        }

        SpriteBatch batch = new SpriteBatch();
        batch.begin();


        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float eyePercentage = 0.7f;
        if (isBlinking) {
            TextureRegion currentFrame = eyeAnimation.getKeyFrame(stateTime, false);

            float eyeX = Gdx.graphics.getWidth() * 0.5f - currentFrame.getRegionWidth() * 1.15f;
            float eyeY = Gdx.graphics.getHeight() * eyePercentage;

            batch.draw(currentFrame, eyeX, eyeY, currentFrame.getRegionWidth() * 2.3f, currentFrame.getRegionHeight());

            if (eyeAnimation.isAnimationFinished(stateTime)) {
                isBlinking = false;
            }
        } else {
            batch.draw(eyeFrames[0],
                Gdx.graphics.getWidth() * 0.5f - eyeFrames[0].getRegionWidth() * 1.15f,
                Gdx.graphics.getHeight() * eyePercentage, eyeFrames[0].getRegionWidth() * 2.3f, eyeFrames[0].getRegionHeight());
        }
        batch.draw(GameAssets.MainMenuLeaves2, leftTreeX, leftTreeY, GameAssets.MainMenuLeaves2.getWidth() * 2f, Gdx.graphics.getHeight());
        batch.draw(GameAssets.MainMenuTitle, middleTreeX, middleTreeY, GameAssets.MainMenuTitle.getWidth() * 1.5f, GameAssets.MainMenuTitle.getHeight());
        batch.draw(GameAssets.MainMenuLeaves, rightTreeX, rightTreeY, GameAssets.MainMenuLeaves.getWidth() * 2f, Gdx.graphics.getHeight());

        stage.draw();
        stage.act(delta);
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, true);
        calculateTreePositions();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
