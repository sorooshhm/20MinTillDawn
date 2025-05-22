package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.model.App;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.User;
import com.tillDawn.utilities.BiLingualHandling;
import com.tillDawn.utilities.SoundInputProcessor;

public class ProfileMenu implements Screen {
    private Main main;
    private Stage stage;
    private Skin skin;
    private User user;
    private BiLingualHandling lang = BiLingualHandling.getInstance();

    public ProfileMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        setStage();
        this.user = App.getCurrUser();
        createUI();
    }

    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        mainTable.add(new Label(lang.getMessage("PROFILE"), skin, "title")).colspan(2).padBottom(40).row();

        Image profileImage = new Image();
        Texture profileTexture = new Texture(Gdx.files.internal(user.getAvatar()));
        profileImage.setDrawable(new TextureRegionDrawable(new TextureRegion(profileTexture)));

        profileImage.setScaling(Scaling.fit);
        mainTable.add(profileImage).size(150, 150).padBottom(30).colspan(2).row();

        Table infoTable = new Table();
        infoTable.defaults().pad(10).left();

        infoTable.add(new Label(lang.getMessage("USERNAME")+":", skin, "subtitle")).left();
        infoTable.add(new Label(user.getUsername(), skin)).left().row();

        infoTable.add(new Label(lang.getMessage("SCORE")+":", skin, "subtitle")).left();
        infoTable.add(new Label(String.valueOf(user.getScore()), skin)).left().row();

        infoTable.add(new Label(lang.getMessage("KILLS")+":", skin, "subtitle")).left();
        infoTable.add(new Label(String.valueOf(user.getKills()), skin)).left().row();

        infoTable.add(new Label(lang.getMessage("MOST_TIME_ALIVE")+":", skin, "subtitle")).left();
        infoTable.add(new Label(String.valueOf(user.getFormattedTime()), skin)).left().row();

        mainTable.add(infoTable).colspan(2).padBottom(40).row();

        TextButton backButton = new TextButton(lang.getMessage("BACK"), skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        TextButton editButton = new TextButton(lang.getMessage("EDIT"), skin);
        editButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new EditProfileMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        mainTable.add(backButton).width(200).height(60);
        if (!user.isGuest())
            mainTable.add(editButton).width(200).height(60);
        infoTable.pad(40f);
    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        Image back = new Image(GameAssets.ProfileMenuBackground);
        back.setFillParent(true);
        stage.addActor(back);
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
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, true);
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
