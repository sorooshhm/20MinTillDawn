package com.tillDawn.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.model.App;
import com.tillDawn.model.Buttons;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.User;
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.utilities.BiLingualHandling;
import com.tillDawn.utilities.SoundInputProcessor;

public class EditButtonsMenu implements Screen {
    private Main main;
    private Stage stage;
    private Skin skin;
    private BiLingualHandling lang = BiLingualHandling.getInstance();

    public EditButtonsMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        setStage();
        createUI();
    }

    public void initializeButtons() {
        for (int i = 0; i < App.getButtons().length; i++) {
            Buttons btn = App.getButtons()[i];
            App.getCurrUser().getButtons()[i] = btn.getOriginal();
        }
    }

    public void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.add(new Label(lang.getMessage("EDIT_BUTTONS"), skin, "title")).colspan(2).padBottom(20).row();
        Table btnTable = new Table();
        btnTable.defaults();
        createButtons(btnTable, skin);
        table.add(btnTable);
        TextButton back = new TextButton(lang.getMessage("BACK"), skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!App.getCurrUser().isGuest())
                    UserRepository.save(App.getCurrUser());
                dispose();
                App.setCurrScreen(new SettingMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        TextButton reset = new TextButton(lang.getMessage("RESET"), skin);
        reset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btnTable.reset();
                initializeButtons();
                createButtons(btnTable, skin);
            }
        });

        table.add(back).colspan(2).padRight(50f);
        table.add(reset).colspan(2);
    }

    public void createButtons(Table btnTable, Skin skin) {
        createInputs(btnTable, skin, lang.getMessage("UP"), 0);
        createInputs(btnTable, skin, lang.getMessage("DOWN"), 1);
        createInputs(btnTable, skin, lang.getMessage("LEFT"), 2);
        createInputs(btnTable, skin, lang.getMessage("RIGHT"), 3);
        createInputs(btnTable, skin, lang.getMessage("AUTO_AIM"), 4);
        createInputs(btnTable, skin, lang.getMessage("RELOAD"), 5);
        createInputs(btnTable, skin, lang.getMessage("SHOOT"), 6);
    }

    public void createInputs(Table table, Skin skin, String name, int buttonIndex) {
        table.add(new Label(name + " : ", skin)).padRight(5).right();
        String str = "";
        User u = App.getCurrUser();
        if (u.getButtons()[buttonIndex] != Input.Buttons.LEFT && u.getButtons()[buttonIndex] != Input.Buttons.RIGHT) {
            str = Input.Keys.toString(u.getButtons()[buttonIndex]).toUpperCase();
        } else {
            str = u.getButtons()[buttonIndex] == Input.Buttons.LEFT ? lang.getMessage("LEFT_CLICK") : lang.getMessage("RIGHT_CLICK");
        }
        TextButton upButton = new TextButton(str, skin);
        upButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(new InputProcessor() {
                    @Override
                    public boolean keyDown(int i) {
                        return false;
                    }

                    @Override
                    public boolean keyUp(int i) {
                        int btnIndex = App.getCurrUser().findButtonWithCode(i);
                        if (btnIndex != -1) {
                            showError(lang.getMessage("THE_KEY_IS_IN_USED"));
                            Gdx.input.setInputProcessor(stage);
                            return true;
                        }
                        System.out.println(i);
                        u.getButtons()[buttonIndex] = i;
                        upButton.setText(Input.Keys.toString(i).toUpperCase());
                        Gdx.input.setInputProcessor(stage);
                        return false;
                    }

                    @Override
                    public boolean keyTyped(char c) {

                        return true;
                    }

                    @Override
                    public boolean touchDown(int i, int i1, int i2, int i3) {
                        return false;
                    }

                    @Override
                    public boolean touchUp(int i, int i1, int i2, int i3) {
                        int btnIndex = App.getCurrUser().findButtonWithCode(i3);
                        if (btnIndex != -1) {
                            showError(lang.getMessage("THE_KEY_IS_IN_USED"));
                            Gdx.input.setInputProcessor(stage);
                            return true;
                        }
                        u.getButtons()[buttonIndex] = i3;
                        upButton.setText(i3 == Input.Buttons.LEFT ? lang.getMessage("LEFT_CLICK") : lang.getMessage("RIGHT_CLICK"));
                        Gdx.input.setInputProcessor(stage);
                        return false;
                    }

                    @Override
                    public boolean touchCancelled(int i, int i1, int i2, int i3) {
                        return false;
                    }

                    @Override
                    public boolean touchDragged(int i, int i1, int i2) {
                        return false;
                    }

                    @Override
                    public boolean mouseMoved(int i, int i1) {
                        return false;
                    }

                    @Override
                    public boolean scrolled(float v, float v1) {
                        return false;
                    }
                });
            }
        });
        table.add(upButton).height(80).width(250).padBottom(10).row();
    }

    public void showError(String message) {
        Dialog dialog = new Dialog(lang.getMessage("ERROR"), skin);
        dialog.text(message);
        dialog.button(lang.getMessage("OK"));
        dialog.show(stage);
    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        Image back = new Image(GameAssets.EditButtonsMenuBackground);
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
