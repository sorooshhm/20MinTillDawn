package com.tillDawn.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.controller.PlayerController;
import com.tillDawn.model.App;
import com.tillDawn.model.AvatarAssets;
import com.tillDawn.model.BaziAssets;
import com.tillDawn.model.Game.*;
import com.tillDawn.model.Game.Game;
import com.tillDawn.model.Game.Tree;
import com.tillDawn.model.User;
import com.tillDawn.repositories.GameRepository;
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.utilities.BiLingualHandling;

import java.util.ArrayList;
import java.util.TimerTask;

public class GameMenu implements Screen, InputProcessor {
    public static final float screenWidth = Gdx.graphics.getWidth();
    public static final float screenHeight = Gdx.graphics.getHeight();
    private Main main;
    private Skin skin;
    private Stage stage;
    private User user;
    private Player player;
    private Game game;
    private SpriteBatch batch;
    private PlayerController playerController;
    private float backMp = 1.5f;
    private BitmapFont font;
    private float spawnTentacles = 0;
    private float spawnEyeBats = 0;
    private boolean isPaused = false;
    private boolean isLose = false;
    private ShaderProgram circleShader;
    private ShaderProgram blackShader;
    private Timer timer;
    private BiLingualHandling lang = BiLingualHandling.getInstance();

    public GameMenu(Main main) {
        new BaziAssets();
        new AvatarAssets();
        this.main = main;
        batch = new SpriteBatch();
        user = App.getCurrUser();
        game = user.getCurrentGame();
        game.setBackHeight(getBackgroundHeight());
        game.setBackWidth(getBackgroundWidth());
        player = game.getPlayer();
        playerController = new PlayerController(player, game, this);
        skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        setStage();
        game.initializeTrees(screenWidth, screenHeight, 50);
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                game.getExplosions().clear();
            }
        }, 10, 2);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (game.getHasturBoss() == null) {
                    spawnHasturBoss();
                }
                this.cancel();
            }
        }, game.getGameMaxTime() / 2, 2);
        createFont();
        circleShader = new ShaderProgram(
            Gdx.files.internal("light_vertex.glsl"),
            Gdx.files.internal("light_fragment.glsl"));
        blackShader = new ShaderProgram(
            Gdx.files.internal("vertex.glsl"),
            Gdx.files.internal("grayscale.glsl")
        );
    }

    public void spawnHasturBoss() {
        float posX = (int) (Math.random() * screenWidth) + player.getCoordinate().getX();
        float posY = (int) (Math.random() * screenHeight) + player.getCoordinate().getY();
        HasturBoss h = new HasturBoss(posX, posY);
        game.setHasturBoss(h);
    }

    public ShaderProgram getCircleShader() {
        return circleShader;
    }

    public void createUI() {
        Table table = new Table();
        table.setFillParent(true);


        stage.addActor(table);
    }

    public void createFont() {
        font = new BitmapFont();

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
    }

    private void renderPlayerStats() {
        font.setColor(1, 1, 1, 1);

        font.draw(batch,
            lang.getMessage("HP") + ": " + String.format("%.1f", player.getHp()) + "/" + player.getMaxHp(),
            20,
            Gdx.graphics.getHeight() - 60);

        font.draw(batch,
            lang.getMessage("XP") + ": " + player.getXp(),
            20,
            Gdx.graphics.getHeight() - 90);
        font.draw(batch,
            lang.getMessage("KILLS") + ": " + player.getKills(),
            20,
            Gdx.graphics.getHeight() - 120);


        font.draw(batch,
            lang.getMessage("AMMO") + ": " + player.getGunAmmo(),
            20,
            Gdx.graphics.getHeight() - 150);

        if (player.isReloading()) {
            font.draw(batch,
                lang.getMessage("RELOAD_GUN"),
                screenWidth / 2f,
                Gdx.graphics.getHeight() - 60);
        }

        float time = game.getGameMaxTime() - game.getGamePlay();
        int min = (int) time / 60;
        int sec = (int) (time % 60);
        BitmapFont f = new BitmapFont();
        f.setColor(Color.BLUE);
        f.getData().setScale(2f);
        f.draw(batch,
            min + " : " + sec,
            Gdx.graphics.getWidth() - 100,
            Gdx.graphics.getHeight() - 60);

    }

    public void renderTrees() {
        for (Tree tree : game.getTrees()) {
            TextureRegion currentFrame = tree.getAnimation().getKeyFrame(tree.getStateTime(), true);
            batch.setColor(1, 1, 1, 0.5f);
            batch.draw(currentFrame, player.getCoordinate().getX() + tree.getX(),
                player.getCoordinate().getY() + tree.getY(), tree.getWidth(), tree.getHeight());
            batch.setColor(1, 1, 1, 1);
            tree.getRectangle().setX(player.getCoordinate().getX() + tree.getX());
            tree.getRectangle().setY(player.getCoordinate().getY() + tree.getY());
        }
    }

    public void renderExplosions() {
        for (ExplosionAnimations explosionAnimation : game.getExplosions()) {
            TextureRegion currentFrame = explosionAnimation.getAnimation()
                .getKeyFrame(explosionAnimation.getStateTime(), false);
            batch.draw(currentFrame, player.getCoordinate().getX() + explosionAnimation.getX(),
                player.getCoordinate().getY() + explosionAnimation.getY(),
                explosionAnimation.getWidth(), explosionAnimation.getHeight());
        }
    }

    public void renderTentacles() {
        for (Tentacle t : game.getTentacles()) {
            TextureRegion currentFrame = t.getAnimation().getKeyFrame(t.getStateTime(), true);
            if (t.getColor() != null) {
                batch.draw(BaziAssets.damage, player.getCoordinate().getX() + t.getX(),
                    player.getCoordinate().getY() + t.getY(), t.getWidth(), t.getHeight());
            } else
                batch.draw(currentFrame, player.getCoordinate().getX() + t.getX(),
                    player.getCoordinate().getY() + t.getY(), t.getWidth(), t.getHeight());
            t.getRectangle().setX(player.getCoordinate().getX() + t.getX());
            t.getRectangle().setY(player.getCoordinate().getY() + t.getY());
        }
    }

    public void renderEyeBats() {
        for (EyeBat e : game.getEyeBats()) {
            TextureRegion currentFrame = e.getAnimation().getKeyFrame(e.getStateTime(), true);
            if (e.getColor() != null) {
                batch.draw(BaziAssets.damage, player.getCoordinate().getX() + e.getX(),
                    player.getCoordinate().getY() + e.getY(), e.getWidth(), e.getHeight());
            } else
                batch.draw(currentFrame, player.getCoordinate().getX() + e.getX(),
                    player.getCoordinate().getY() + e.getY(), e.getWidth(), e.getHeight());
            e.getRectangle().setX(player.getCoordinate().getX() + e.getX());
            e.getRectangle().setY(player.getCoordinate().getY() + e.getY());
            renderEyBatBullets(e);
        }
    }

    public void renderHasturBoss() {
        if (game.getHasturBoss() != null) {
            HasturBoss h = game.getHasturBoss();
            TextureRegion currentFrame = h.getAnimation().getKeyFrame(h.getStateTime(), true);
            batch.draw(currentFrame, player.getCoordinate().getX() + h.getX(),
                player.getCoordinate().getY() + h.getY(), h.getWidth(), h.getHeight());
            h.getRectangle().setX(player.getCoordinate().getX() + h.getX());
            h.getRectangle().setY(player.getCoordinate().getY() + h.getY());
        }

    }

    public void renderHasturBounds() {
        if (game.getHasturBoss() != null) {
            HasturBoss h = game.getHasturBoss();
            ShapeRenderer shapeRenderer = new ShapeRenderer();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(h.getX() + player.getCoordinate().getX() - h.getBounds().getWidth() / 2f,
                h.getY() + player.getCoordinate().getY() - h.getBounds().getHeight() / 2f,
                h.getBounds().getWidth(), h.getBounds().getHeight());
            shapeRenderer.end();
        }
    }

    public void checkHasturBounds() {
        if (game.getHasturBoss() != null) {
            HasturBoss h = game.getHasturBoss();
            float x = Gdx.graphics.getWidth() / 2f;
            float y = Gdx.graphics.getHeight() / 2f;
            if (h.getX() + player.getCoordinate().getX() + h.getBounds().getWidth() / 2f
                - playerController.getWidth() / 2f <= x) {
                player.getCoordinate().setX(player.getCoordinate().getX() + 80f);
                player.setHp(player.getHp() - .1f);
            } else if (h.getX() + player.getCoordinate().getX() - h.getBounds().getWidth() / 2f
                + playerController.getWidth() / 2f >= x) {
                player.getCoordinate().setX(player.getCoordinate().getX() - 80f);
                player.setHp(player.getHp() - .1f);
            } else if (h.getY() + player.getCoordinate().getY() + h.getBounds().getHeight() / 2f
                - playerController.getHeight() / 2f - 50f <= y) {
                player.getCoordinate().setY(player.getCoordinate().getY() + 80f);
                player.setHp(player.getHp() - .1f);
            } else if (h.getY() + player.getCoordinate().getY() - h.getBounds().getHeight() / 2f
                + playerController.getHeight() / 2f - 50f >= y) {
                player.getCoordinate().setY(player.getCoordinate().getY() - 80f);
                player.setHp(player.getHp() - .1f);
            }
        }
    }

    public void renderXps() {
        for (XP e : game.getXps()) {
            batch.draw(e.getTexture(), player.getCoordinate().getX() + e.getX(),
                player.getCoordinate().getY() + e.getY(), e.getWidth(), e.getHeight());
            e.getRectangle().setX(player.getCoordinate().getX() + e.getX());
            e.getRectangle().setY(player.getCoordinate().getY() + e.getY());
        }
    }

    public void renderEyBatBullets(EyeBat eyeBat) {
        int index = -1;
        for (EnemyBullet b : eyeBat.getBullets()) {
            b.getSprite().draw(batch);
            b.getDirection().add(
                screenWidth / 2f - player.getCoordinate().getX(),
                screenHeight / 2f - player.getCoordinate().getY()
            ).nor();
            Vector2 direction = b.getDirection();

            b.getSprite().setX(b.getSprite().getX() + direction.x * 5);
            b.getSprite().setY(b.getSprite().getY() + direction.y * 5);
            b.getRectangle().setX(b.getSprite().getX() + direction.x * 5);
            b.getRectangle().setY(b.getSprite().getY() + direction.y * 5);
            if (b.getRectangle().overlaps(playerController.getPlayerRectangle())) {
                index = eyeBat.getBullets().indexOf(b);
                player.setHp(player.getHp() - .2f);
            }
        }
        if (index != -1) {
            eyeBat.getBullets().remove(index);
        }
    }


    public void updateTentacles() {
        int index = -1;
        for (Tentacle b : game.getTentacles()) {
            Vector2 direction = new Vector2(
                b.getX() + player.getCoordinate().getX() - Gdx.graphics.getWidth() / 2f,
                b.getY() + player.getCoordinate().getY() - Gdx.graphics.getHeight() / 2f
            ).nor();

            b.setX(b.getX() - direction.x);
            b.setY(b.getY() - direction.y);
            b.getRectangle().setX(b.getX() + player.getCoordinate().getX() - direction.x);
            b.getRectangle().setY(b.getY() + player.getCoordinate().getY() - direction.y);
            if (b.getRectangle().overlaps(playerController.getPlayerRectangle())) {
                index = game.getTentacles().indexOf(b);
            }
        }
        if (index != -1) {
            game.getTentacles().remove(index);
            player.setHp(player.getHp() - .3f);
        }
    }

    public void updateXps() {
        int index = -1;
        for (XP e : game.getXps()) {
            if (e.getRectangle().overlaps(playerController.getPlayerRectangle())) {
                index = game.getXps().indexOf(e);
                player.setXp(player.getXp() + 3);
            }
        }
        if (index != -1) {
            game.getXps().remove(index);
        }
    }


    public void updateEyeBats(float delta) {
        int index = -1;
        for (EyeBat b : game.getEyeBats()) {
            Vector2 direction = new Vector2(
                b.getX() + player.getCoordinate().getX() - Gdx.graphics.getWidth() / 2f,
                b.getY() + player.getCoordinate().getY() - Gdx.graphics.getHeight() / 2f
            ).nor();

            b.setX(b.getX() - direction.x);
            b.setY(b.getY() - direction.y);
            b.getRectangle().setX(b.getX() + player.getCoordinate().getX() - direction.x);
            b.getRectangle().setY(b.getY() + player.getCoordinate().getY() - direction.y);
            if (b.getRectangle().overlaps(playerController.getPlayerRectangle())) {
                index = game.getEyeBats().indexOf(b);
            }
            b.setShotTime(b.getShotTime() + delta);
            if (b.getShotTime() > 10) {
                b.setShotTime(0);
                b.getBullets().add(new EnemyBullet(player.getCoordinate().getX() + b.getX(),
                    player.getCoordinate().getY() + b.getY()));
            }
        }
        if (index != -1) {
            game.getEyeBats().remove(index);
            player.setHp(player.getHp() - .3f);
        }
    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);
    }

    public void renderBackground() {
        batch.draw(BaziAssets.background, 0, 0, screenWidth, screenHeight);
    }

    public float getBackgroundWidth() {
        return BaziAssets.background.getWidth() / backMp;
    }

    public float getBackgroundHeight() {
        return BaziAssets.background.getHeight() / backMp;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (isPaused) {
            pause();
            return;
        }
        if (player.getHp() <= 0 || isLose) {
            lose();
            return;
        }

        if (game.getGamePlay() >= game.getGameMaxTime()) {
            win();
            return;
        }
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (player.isHurt()) {
            player.setIsHurtTime(player.getIsHurtTime() + delta);
            if (player.getIsHurtTime() > 1f) {
                player.setIsHurtTime(0);
                player.setHurt(false);
            }
        }

        if (player.isLevelUp()) {
            printAbilitiesMenu();
            return;
        }

        game.updateStateTime(delta);

        checkHasturBounds();
        batch.begin();
        if (user.isGrayShadow()) {
            batch.setShader(blackShader);
        } else {
            batch.setShader(circleShader);
        }
        playerController.update(delta);
        handleBackgroundWorks(delta);
        renderBackground();
        handleCheatCodes();
        playerController.handlePlayerWalk();
        playerController.render(batch);
        renderPlayerStats();
        renderTentacles();
        renderTrees();
        renderEyeBats();
        renderExplosions();
        renderXps();
        renderHasturBoss();
        batch.end();
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(20, Gdx.graphics.getHeight() - 45,
            Gdx.graphics.getWidth() - 40, 40);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(20, Gdx.graphics.getHeight() - 45,
            (Gdx.graphics.getWidth() - 40) * ((float) player.getXp() / (float) (player.getLevel() * 20)), 40);
        shapeRenderer.end();
        renderHasturBounds();

        batch.begin();
        font.draw(batch, lang.getMessage("LEVEL") + " : " + player.getLevel(),
            Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 15);
        batch.end();

    }

    public void handleCheatCodes() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            float time = Math.min(game.getGameMaxTime(), game.getGamePlay() + 60);
            game.setGamePlay(time);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            player.setLevel(player.getLevel() + 1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            player.setHp(player.getHp() + .1f);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            spawnHasturBoss();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            player.setProjectileAddition(player.getProjectileAddition() + 1);
        }
    }

    public void printAbilitiesMenu() {
        Table table = new Table();
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);

        table.add(new Label(lang.getMessage("CHOOSE_ABILITY"), skin, "title")).row();

        TextButton vitality = new TextButton("Vitality ", skin);
        TextButton damager = new TextButton("Damager ", skin);
        TextButton procrease = new TextButton("Procrease ", skin);
        TextButton amocrease = new TextButton("Amocrease ", skin);
        TextButton speedy = new TextButton("Speedy ", skin);

        vitality.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.setLevelUp(false);
                player.setHp(player.getMaxHp());
                player.getAbilities().add(1);
                stage.clear();
                setStage();
            }
        });
        damager.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.setLevelUp(false);
                player.setDamageMp(1.5f);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.setDamageMp(1f);
                        this.cancel();
                    }
                }, 10, 2);
                player.getAbilities().add(2);
                stage.clear();
                setStage();
            }
        });
        procrease.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.setLevelUp(false);
                player.setProjectileAddition(3);
                stage.clear();
                setStage();
            }
        });
        amocrease.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.setLevelUp(false);
                player.setMaxAmmoAddition(5);
                player.getAbilities().add(4);
                stage.clear();
                setStage();
            }
        });
        speedy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.setLevelUp(false);
                player.setSpeedMp(2);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.setSpeedMp(1f);
                        this.cancel();
                    }
                }, 10, 2);
                player.getAbilities().add(5);
                stage.clear();
                setStage();
            }
        });

        table.add(vitality).colspan(2);
        table.add(damager).colspan(2).row();
        table.add(procrease).colspan(2);
        table.add(amocrease).colspan(2).row();
        table.add(speedy).colspan(2).row();
        stage.addActor(table);
        stage.draw();

    }

    public void lose() {
        ScreenUtils.clear(0, 0, 0, 1);
        timer.clear();
        user.setScore(user.getScore() + (int) game.getGameMaxTime() * player.getKills());
        user.setKills(user.getKills() + player.getKills());
        if (game.getGamePlay() > user.getMostTimeAlive()) {
            user.setMostTimeAlive(game.getGamePlay());
        }
        game.setFinished(true);
        if (!user.isGuest()) {
            GameRepository.save(game);
        }
        if (!user.isGuest())
            UserRepository.save(user);
        createLooseScreen();
    }

    public void win() {
        ScreenUtils.clear(0, 0, 0, 1);
        timer.clear();
        user.setScore(user.getScore() + (int) game.getGameMaxTime() * player.getKills());
        user.setKills(user.getKills() + player.getKills());
        if (game.getGameMaxTime() > user.getMostTimeAlive()) {
            user.setMostTimeAlive(game.getGamePlay());
        }
        game.setFinished(true);
        if (!user.isGuest()) {
            GameRepository.save(game);
        }
        if (!user.isGuest())
            UserRepository.save(user);
        createWinScreen();
    }

    public void createLooseScreen() {
        Table table = new Table();
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);

        Label title = new Label(lang.getMessage("YOU_LOST"), new Label.LabelStyle(new BitmapFont(), Color.RED));
        title.setFontScale(2f);

        Table statsTable = createStatsTable();

        Table buttonTable = createButtonTable();


        table.add(title).padBottom(40).row();
        table.add(statsTable).width(400).padBottom(40).row();
        table.add(buttonTable);

        stage.draw();

    }

    public void createWinScreen() {
        Table table = new Table();
        table.setFillParent(true);
        stage.clear();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        Label title = new Label(lang.getMessage("YOU_WON"), new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        title.setFontScale(2f);

        Table statsTable = createStatsTable();

        Table buttonTable = createButtonTable();


        table.add(title).padBottom(40).row();
        table.add(statsTable).width(400).padBottom(40).row();
        table.add(buttonTable);

        stage.draw();

    }

    private Table createButtonTable() {
        Table buttonTable = new Table();
        buttonTable.defaults().pad(10).width(200).height(60);

        TextButton menuButton = new TextButton(lang.getMessage("MAIN_MENU"), skin);
        TextButton replayButton = new TextButton(lang.getMessage("PLAY_AGAIN"), skin);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new PreGameMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        buttonTable.add(replayButton).width(500).height(80).padRight(10);
        buttonTable.add(menuButton).width(500).height(80).padRight(10);

        return buttonTable;
    }

    private Table createStatsTable() {
        Table statsTable = new Table();
        statsTable.defaults().pad(10).left();

        statsTable.add(new Label(lang.getMessage("MISSION_REPORT"), skin)).colspan(2).center().row();

        addStatRow(statsTable, lang.getMessage("USERNAME") + " : ", player.getUser().getUsername(), "user-icon");
        addStatRow(statsTable, lang.getMessage("ENEMY_DEFEATED") + " : ", String.valueOf(player.getKills()), "skull-icon");
        addStatRow(statsTable, lang.getMessage("SURVIVAL_TIME") + " : ", formatTime(game.getGamePlay()), "clock-icon");
        addStatRow(statsTable, lang.getMessage("TOTAL_SCORE") + " : ", String.valueOf(game.getGamePlay() * player.getKills()),
            "star-icon");

        return statsTable;
    }

    private String formatTime(float seconds) {
        int minutes = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        return String.format("%02d:%02d", minutes, secs);
    }


    private void addStatRow(Table table, String label, String value, String iconName) {
        table.add();
        table.add(new Label(label, skin)).left().width(150);
        table.add(new Label(value, skin)).left().row();
    }

    public void handleBackgroundWorks(float delta) {
        spawnTentacles += delta;
        float time = game.addGamePlay(delta);
        spawnEyeBats += delta;
        if (time > game.getGameMaxTime()) {
        }
        spawnTentacles(time);
        updateTentacles();

        spawnEyeBats(time);
        updateEyeBats(delta);
        updateXps();

        updateHasturBoss(delta);
    }

    public void updateHasturBoss(float delta) {
        if (game.getHasturBoss() != null) {
            HasturBoss hasturBoss = game.getHasturBoss();
            hasturBoss.setWalkStateTime(hasturBoss.getWalkStateTime() + delta);
            if (hasturBoss.getWalkStateTime() >= 5) {
                hasturBoss.setWalkStateTime(0);
                Vector2 direction = new Vector2(
                    hasturBoss.getX() + player.getCoordinate().getX() - Gdx.graphics.getWidth() / 2f,
                    hasturBoss.getY() + player.getCoordinate().getY() - Gdx.graphics.getHeight() / 2f
                ).nor();

                hasturBoss.setX(hasturBoss.getX() - 200 * direction.x);
                hasturBoss.setY(hasturBoss.getY() - 200 * direction.y);
                hasturBoss.getRectangle().setX(hasturBoss.getX() + player.getCoordinate().getX() - 200 * direction.x);
                hasturBoss.getRectangle().setY(hasturBoss.getY() + player.getCoordinate().getY() - 200 * direction.y);
                if (hasturBoss.getRectangle().overlaps(playerController.getPlayerRectangle())) {
                    player.setHp(player.getHp() - .3f);
                }
                hasturBoss.getBounds().setWidth(hasturBoss.getBounds().getWidth() * .99f);
                hasturBoss.getBounds().setHeight(hasturBoss.getBounds().getHeight() * .99f);
            }

        }
    }

    public void spawnTentacles(float time) {
        if (spawnTentacles >= 3) {
            spawnTentacles = 0;
            int count = (int) Math.ceil(time / 50);
            for (int i = 0; i < count; i++) {
                float xPos = (float) (Math.random() * (getBackgroundWidth() - 30f));
                float yPos = (float) (Math.random() * (getBackgroundHeight() - 30f));
                Tentacle tentacle = new Tentacle(xPos, yPos);

                game.getTentacles().add(tentacle);
            }
        }
    }

    public void spawnEyeBats(float time) {
        if (spawnEyeBats >= 10) {
            spawnEyeBats = 0;
            int count = (int) Math.ceil((4 * time - game.getGameMaxTime() + 30f) / 90f);
            for (int i = 0; i < count; i++) {
                float xPos = (float) (Math.random() * (getBackgroundWidth() - 30f));
                float yPos = (float) (Math.random() * (getBackgroundHeight() - 30f));
                EyeBat eyeBat = new EyeBat(xPos, yPos);

                game.getEyeBats().add(eyeBat);
            }
        }
    }

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1, true);
    }


    @Override
    public void pause() {
        isPaused = true;
        Table table = createCheatTable();
        stage.addActor(table);
        stage.draw();
    }

    // In your screen class
    private Table createCheatTable() {
        Table table = new Table();
        table.setFillParent(true);

        Gdx.input.setInputProcessor(stage);

        Label cheatTitle = new Label(lang.getMessage("CHEAT_CODES"), skin, "title");
        table.add(cheatTitle).colspan(2).center().row();
        table.add().height(10).row(); // Spacer

        addCheatRow(table, "Key 1", lang.getMessage("DECREASE_GAME_TIME"));
        addCheatRow(table, "Key 2", lang.getMessage("PLAYER_LEVEL_UP"));
        addCheatRow(table, "Key 3", lang.getMessage("INCREASE_HP"));
        addCheatRow(table, "Key 4", lang.getMessage("GO_TO_BOSS_FIGHT"));
        addCheatRow(table, "Key 5", lang.getMessage("INCREASE_PROJECTILE"));

        table.add().height(20).row();
        Label abilitiesTitle = new Label(lang.getMessage("PLAYER_ABILITIES"), skin);
        table.add(abilitiesTitle).colspan(2).center().row();

        for (Integer i : player.getAbilities()) {
            if (i.equals(1)) {
                addAbilityRow(table, "Vitality", "Increase Hp");

            } else if (i.equals(2)) {
                addAbilityRow(table, "Damager", "Increase Damage");

            } else if (i.equals(3)) {
                addAbilityRow(table, "Procrease", "Increase Projectile");

            } else if (i.equals(4)) {
                addAbilityRow(table, "Amocrease", "increase Ammo max");

            } else if (i.equals(5)) {
                addAbilityRow(table, "Speedy", "Increase player speed");

            }
        }

        table.add().height(30).row();

        TextButton resumeButton = new TextButton(lang.getMessage("RESUME"), skin);
        TextButton quitButton = new TextButton(lang.getMessage("QUIT"), skin);
        TextButton giveUpButton = new TextButton(lang.getMessage("GIVE_UP"), skin);
        TextButton blackShader = new TextButton(lang.getMessage("WHITE_BLACK"), skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false;
                stage.clear();
                setStage();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!user.isGuest())
                    GameRepository.save(game);
                dispose();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        giveUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!user.isGuest())
                    GameRepository.save(game);
                isPaused = false;
                isLose = true;
                stage.clear();
                lose();
            }
        });

        blackShader.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPaused = false;
                user.setGrayShadow(!user.isGrayShadow());
                stage.clear();
                setStage();
            }
        });
        Table buttonTable = new Table();
        buttonTable.add(blackShader).width(500).height(100).colspan(5).row();
        buttonTable.defaults().pad(5).width(120).height(50);
        buttonTable.add(resumeButton).width(250).height(100);
        buttonTable.add(quitButton).width(250).height(100);
        buttonTable.add(giveUpButton).width(250).height(100);

        table.add(buttonTable).colspan(2).center();

        return table;
    }

    private void addCheatRow(Table table, String key, String description) {
        table.add(new Label(key, skin)).left().width(80);
        table.add(new Label(description, skin)).left();
        table.row();
    }

    private void addAbilityRow(Table table, String ability, String value) {
        table.add(new Label(ability, skin)).left().width(150);
        table.add(new Label(value, skin)).left();
        table.row();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.clear();
        stage.dispose();
        BaziAssets.bullet.dispose();
        BaziAssets.enemyBullet.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        if (user.getButtons()[6] == Input.Buttons.LEFT && i3 == Input.Buttons.LEFT) {
            this.playerController.getGunController().handleShot(i, i1);
        } else if (user.getButtons()[6] == Input.Buttons.RIGHT && i3 == Input.Buttons.RIGHT) {
            this.playerController.getGunController().handleShot(i, i1);
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
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

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
