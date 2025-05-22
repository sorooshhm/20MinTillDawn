package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.model.App;
import com.tillDawn.model.GameAssets;
import com.tillDawn.utilities.BiLingualHandling;
import com.tillDawn.utilities.SoundInputProcessor;

public class SettingMenu implements Screen {
    private Main main;
    private Stage stage;
    private Skin skin;
    private float musicVolume;
    private SelectBox<String> musicFile;
    private boolean isPlaying = true;
    private BiLingualHandling lang = BiLingualHandling.getInstance();

    public SettingMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        setStage();

        createUI();
    }

    public void createUI() {
        Table mainTale = new Table();
        mainTale.setFillParent(true);
        mainTale.add(new Label(lang.getMessage("SETTINGS"), skin, "title")).colspan(2).padBottom(30).row();
        stage.addActor(mainTale);

        Slider slider = new Slider(0, 100, 1, false, skin);
        slider.setValue(100);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVolume = (float) ((Slider) actor).getValue() / 100;
                App.getCurrMusic().setVolume(musicVolume);
            }
        });

        musicFile = new SelectBox<>(skin);
        musicFile.setItems(
            "Enter Sandman",
            "Show must go on",
            "South of heaven"
        );
        musicFile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Music music = null;
                if (musicFile.getSelected().equals("Enter Sandman")) {
                    music = GameAssets.EnterSandMan;
                } else if (musicFile.getSelected().equals("Show must go on")) {
                    music = GameAssets.ShowMustGoOn;
                } else if (musicFile.getSelected().equals("South of heaven")) {
                    music = GameAssets.SouthOfHeaven;
                }
                App.getCurrMusic().dispose();
                music.setVolume(musicVolume);
                App.setCurrMusic(music);
                music.play();
            }
        });

        TextButton pauseMusic = new TextButton(lang.getMessage("PAUSE_MUSIC"), skin);
        pauseMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isPlaying = !isPlaying;
                if (!isPlaying) {
                    pauseMusic.setText(lang.getMessage("RESUME_MUSIC"));
                    App.getCurrMusic().pause();
                } else {
                    pauseMusic.setText(lang.getMessage("PAUSE_MUSIC"));
                    App.getCurrMusic().play();
                }
            }
        });
        String text = App.isSfxStage() ? lang.getMessage("SFX_ON") : lang.getMessage("SFX_OFF");
        TextButton sfxBtn = new TextButton(text, skin);
        sfxBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.setSfxStage(!App.isSfxStage());
                if (App.isSfxStage()) {
                    sfxBtn.setText(lang.getMessage("SFX_ON"));
                } else {
                    sfxBtn.setText(lang.getMessage("SFX_OFF"));
                }
            }
        });

        TextButton editButtons = new TextButton(lang.getMessage("EDIT_BUTTONS"), skin);
        editButtons.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                App.setCurrScreen(new EditButtonsMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        String t = App.getCurrUser().isAutoReload() ? lang.getMessage("AUTO_RELOAD_ON") : lang.getMessage("AUTO_RELOAD_OFF");
        TextButton autoReload = new TextButton(t, skin);
        autoReload.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.getCurrUser().setAutoReload(!App.getCurrUser().isAutoReload());
                if (App.getCurrUser().isAutoReload()) {
                    autoReload.setText(lang.getMessage("AUTO_RELOAD_ON"));
                } else {
                    autoReload.setText(lang.getMessage("AUTO_RELOAD_OFF"));
                }
            }
        });

        String bw = App.getCurrUser().isGrayShadow() ? lang.getMessage("BLACK_WHITE_ON") : lang.getMessage("BLACK_WHITE_OFF");
        TextButton blackWhite = new TextButton(bw, skin);
        blackWhite.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.getCurrUser().setGrayShadow(!App.getCurrUser().isGrayShadow());
                if (App.getCurrUser().isGrayShadow()) {
                    blackWhite.setText(lang.getMessage("BLACK_WHITE_OFF"));
                } else {
                    blackWhite.setText(lang.getMessage("BLACK_WHITE_ON"));
                }
            }
        });

        String txt = lang.getLanguage().equals("EN") ? "LANG : EN" : "LANG : FN";
        TextButton langBtn = new TextButton(txt, skin);
        langBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lang.getLanguage().equals("EN")) {
                    lang.setLanguage("FR");
                    langBtn.setText("LANG : FR");
                } else {
                    langBtn.setText("LANG : EN");
                    lang.setLanguage("EN");
                }
            }
        });

        TextButton back = new TextButton(lang.getMessage("BACK"), skin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        mainTale.add(new Label(lang.getMessage("MUSIC") + ":", skin)).padRight(10);
        mainTale.add(slider).width(300).pad(20).row();
        mainTale.add(new Label(lang.getMessage("MUSIC_VOLUME") + ":", skin)).padRight(10);
        mainTale.add(musicFile).width(500).padBottom(20).row();
        mainTale.add(pauseMusic).width(380).colspan(2).padBottom(20).row();
        mainTale.add(sfxBtn).width(380).colspan(2).padBottom(20).row();
        mainTale.add(editButtons).width(500).padBottom(20);
        mainTale.add(autoReload).width(600).pad(10).row();
        mainTale.add(blackWhite).width(600).colspan(2).padBottom(20).row();

        mainTale.add(langBtn).width(500).colspan(2).padRight(20);
        mainTale.add(back).width(500).colspan(2).pad(20).row();

    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        Image back = new Image(GameAssets.SettingMenuBackground);
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
