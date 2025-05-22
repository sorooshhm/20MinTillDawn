package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.model.App;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.User;
import com.tillDawn.model.enums.Heroes;
import com.tillDawn.utilities.SoundInputProcessor;

public class HintMenu implements Screen {
    private Main main;
    private Stage stage;
    private Skin skin;
    private Heroes selectedHero = Heroes.SHANA;
    private User user;

    public HintMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        user = App.getCurrUser();
        setStage();
        createUI();
    }

    public void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        stage.addActor(mainTable);
        mainTable.add(new Label("HINT MENU", skin, "title")).colspan(2).padBottom(20).row();
        mainTable.add(createHeroSelection()).colspan(2).growX().padBottom(30).row();
        Table contentTable = new Table();
        contentTable.add(createControlsSection()).width(400).padRight(20);
        contentTable.add(createCheatsSection()).width(400);
        mainTable.add(contentTable).colspan(2).padBottom(20).row();
        mainTable.add(createAbilitiesSection()).colspan(2).growX().row();
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        ScrollPane scrollPane = new ScrollPane(mainTable, skin);
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        stage.addActor(scrollPane);
        stage.addActor(backButton);
    }

    private Table createHeroSelection() {
        Table table = new Table();
        table.defaults().pad(5);

        table.add(new Label("SELECT HERO", skin)).colspan(2).padBottom(10).row();

        SelectBox<String> heroSelect = new SelectBox<>(skin);
        heroSelect.setItems(Heroes.getValuesStrings());
        heroSelect.setSelected(Heroes.SHANA.getName());
        Label descriptionLabel = new Label(selectedHero.toString(), skin);
        heroSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedHero = Heroes.findHeroByName(heroSelect.getSelected());
                descriptionLabel.setText(selectedHero.toString());
            }
        });


        table.add(heroSelect).width(300).center().padRight(15);
        table.add(descriptionLabel).center().growX().row();

        return table;
    }

    private Table createControlsSection() {
        Table table = new Table();
        table.defaults().pad(5);

        table.add(new Label("CONTROLS", skin)).colspan(2).padBottom(10).row();

        addControlRow(table, "UP", Input.Keys.toString(user.getButtons()[0]));
        addControlRow(table, "DOWN", Input.Keys.toString(user.getButtons()[1]));
        addControlRow(table, "LEFT", Input.Keys.toString(user.getButtons()[2]));
        addControlRow(table, "RIGHT", Input.Keys.toString(user.getButtons()[3]));
        addControlRow(table, "AUTO AIM", Input.Keys.toString(user.getButtons()[4]));
        addControlRow(table, "RELOAD", Input.Keys.toString(user.getButtons()[5]));
        if (user.getButtons()[6] != Input.Buttons.LEFT && Input.Buttons.RIGHT != user.getButtons()[6]) {
            addControlRow(table, "SHOOT", Input.Keys.toString(user.getButtons()[6]));
        } else {
            if (user.getButtons()[6] == Input.Buttons.LEFT) {
                addControlRow(table, "SHOOT", "LEFT CLICK");
            } else {
                addControlRow(table, "SHOOT", "RIGHT CLICK");
            }
        }

        return table;
    }

    private Table createCheatsSection() {
        Table table = new Table();
        table.defaults().pad(5);

        table.add(new Label("CHEAT CODES", skin)).colspan(2).padBottom(10).row();

        addCheatRow(table, "Key 1", "Decrease time of the game");
        addCheatRow(table, "Key 2", "Player level up");
        addCheatRow(table, "Key 3", "Increase HP");
        addCheatRow(table, "Key 4", "Go to boss fight");
        addCheatRow(table, "Key 5", "Increase projectile");

        return table;
    }

    private Table createAbilitiesSection() {
        Table table = new Table();
        table.defaults().pad(10);

        table.add(new Label("ABILITIES", skin)).colspan(2).padBottom(5).row();

        table.add(new Label("Vitality : Full the HP" , skin)).padRight(10);
        table.add(new Label("Damager : Increase damage of the gun", skin)).padBottom(2).row();
        table.add(new Label("Procrease : Increase the projectile", skin)).padRight(10);
        table.add(new Label("Amocrease : Increase the ammo max" , skin)).row();
        addAbilityRow(table, "Speedy", "Increase the speed");

        return table;
    }

    private void addAbilityRow(Table table, String name, String description) {
        table.add().size(32).padRight(10);

        Table textTable = new Table();
        textTable.add(new Label(name, skin)).left().row();
        textTable.add(new Label(description, skin)).left().padTop(1);

        table.add(textTable).left();
        table.row();
    }

    private void addCheatRow(Table table, String key, String effect) {
        table.add(new Label(key, skin)).left().width(80);
        table.add(new Label(effect, skin)).left().padLeft(10);
        table.row();
    }

    private void addControlRow(Table table, String action, String key) {
        table.add(new Label(action, skin)).left().width(150);
        table.add(new Label(key, skin)).left().padLeft(20);
        table.row();
    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        Image back = new Image(GameAssets.HintMenuBackground);
        back.setFillParent(true);
//        stage.addActor(back);
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
