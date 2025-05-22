package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.utilities.SoundInputProcessor;

import java.util.ArrayList;

public class ScoreboardMenu implements Screen {
    private Main main;
    private Stage stage;
    private Skin skin;
    private Table scoreTable;
    private String currentSort = "score";
    private ArrayList<User> users;
    private User user;

    public ScoreboardMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        user = App.getCurrUser();
        users = UserRepository.findAllUsers();
        setStage();
        createUI();
    }

    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        stage.addActor(mainTable);
        mainTable.add(new Label("SCOREBOARD", skin)).colspan(3).padBottom(20).row();
        mainTable.add(createSortSelector()).colspan(3).padBottom(20).row();

        scoreTable = new Table();
        scoreTable.defaults().pad(5);

        ScrollPane scrollPane = new ScrollPane(scoreTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        mainTable.add(scrollPane).colspan(3).grow().row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        mainTable.add(backButton).colspan(3).padTop(20).width(200).height(80);
        refreshScores();
    }

    private Table createSortSelector() {
        Table table = new Table();
        table.defaults().pad(5);

        table.add(new Label("Sort by:", skin)).right().padRight(10);

        SelectBox<String> sortSelect = new SelectBox<>(skin);
        sortSelect.setItems("Score", "Kills", "Time Alive");
        sortSelect.setSelected("Score");
        sortSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (sortSelect.getSelectedIndex()) {
                    case 0:
                        currentSort = "score";
                        break;
                    case 1:
                        currentSort = "kills";
                        break;
                    case 2:
                        currentSort = "time";
                        break;
                }
                refreshScores();
            }
        });

        table.add(sortSelect).width(200);
        return table;
    }

    private void refreshScores() {
        scoreTable.clear();

        scoreTable.add(new Label("Rank", skin)).width(60);
        scoreTable.add(new Label("Username", skin)).width(200);
        scoreTable.add(new Label("Score", skin)).width(100);
        scoreTable.add(new Label("Kills", skin)).width(100);
        scoreTable.add(new Label("Time Alive", skin)).width(120);
        scoreTable.row();

        sortScores();

        for (int i = 0; i < Math.min(users.size(), 10); i++) {
            User player = users.get(i);

            Color rowColor = Color.WHITE;
            if (i == 0) rowColor = Color.GOLD;
            else if (i == 1) rowColor = Color.GRAY;
            else if (i == 2) rowColor =  new Color(0.8f, 0.5f, 0.2f, 1f);

            if(player.get_id().equals(user.get_id())){
                rowColor = Color.BLUE;
            }

            Label rankLabel = new Label((i + 1) + ".", skin);
            rankLabel.setColor(rowColor);
            scoreTable.add(rankLabel);

            Label nameLabel = new Label(player.getUsername(), skin);
            nameLabel.setColor(rowColor);
            scoreTable.add(nameLabel);

            Label scoreLabel = new Label(String.valueOf(player.getScore()), skin);
            scoreLabel.setColor(rowColor);
            scoreTable.add(scoreLabel);

            Label killsLabel = new Label(String.valueOf(player.getKills()), skin);
            killsLabel.setColor(rowColor);
            scoreTable.add(killsLabel);

            Label timeLabel = new Label(player.getFormattedTime(), skin);
            timeLabel.setColor(rowColor);
            scoreTable.add(timeLabel);

            scoreTable.row();
        }
    }

    private void sortScores() {
        switch (currentSort) {
            case "score":
                users.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
                break;
            case "kills":
                users.sort((a, b) -> Integer.compare(b.getKills(), a.getKills()));
                break;
            case "time":
                users.sort((a, b) -> Float.compare(b.getMostTimeAlive(), a.getMostTimeAlive()));
                break;
        }
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
    public void render(float v) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(v);
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
        stage.dispose();
        skin.dispose();
    }
}
