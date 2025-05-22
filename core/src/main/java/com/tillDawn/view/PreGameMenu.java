package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.model.App;
import com.tillDawn.model.AvatarAssets;
import com.tillDawn.model.Game.Coordinate;
import com.tillDawn.model.Game.Game;
import com.tillDawn.model.Game.Player;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.User;
import com.tillDawn.model.enums.Guns;
import com.tillDawn.model.enums.Heroes;
import com.tillDawn.repositories.GameRepository;
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.utilities.BiLingualHandling;
import com.tillDawn.utilities.SoundInputProcessor;

public class PreGameMenu implements Screen {
    private Main main;
    private Stage stage;
    private Skin skin;
    private User user;

    private enum ScreenState {AVATAR, GUN, START_GAME}

    ;
    private float gameTime = 2.5f;
    private Image currAvatarImage = new Image();
    private Image currGunImage = new Image();
    private ScreenState state = ScreenState.AVATAR;
    private Table currView;
    private Texture currentAvatar;
    private Texture currentGun;
    private Heroes currHero;
    private Guns currGuns;
    private BiLingualHandling lang = BiLingualHandling.getInstance();

    public PreGameMenu(Main main) {
        new AvatarAssets();
        this.main = main;
        skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        setStage();
        user = App.getCurrUser();
        currentAvatar = AvatarAssets.ShanaPortrait;
        currentGun = AvatarAssets.Revolver;

        showChooseAvatarScreen();
    }

    public String avatarToName(Texture t) {
        String s = t.toString().split("_")[1].trim();
        return s;
    }

    public void showChooseAvatarScreen() {
        clearScreen();
        state = ScreenState.AVATAR;

        currView = new Table();
        currView.setFillParent(true);

        Texture profileTexture = currentAvatar;
        currAvatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(profileTexture)));

        currAvatarImage.setScaling(Scaling.fit);

        String name = avatarToName(currentAvatar);
        currHero = Heroes.findHeroByName(name);
        currView.add(new Label(lang.getMessage("NAME") + " : " + name, skin)).colspan(2);
        currView.add(new Label(lang.getMessage("SPEED")+" : " + currHero.getSpeed(), skin)).colspan(2);
        currView.add(new Label(lang.getMessage("HP")+": " + currHero.getHp(), skin)).colspan(2);
        currView.add(currAvatarImage).colspan(5).center().padBottom(15).row();

        TextButton backBtn = new TextButton(lang.getMessage("BACK"), skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearScreen();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        currView.add(backBtn).colspan(2);


        createAvatar(AvatarAssets.ShanaPortrait);
        createAvatar(AvatarAssets.DasherPortrait);
        createAvatar(AvatarAssets.DiamondPortrait);
        createAvatar(AvatarAssets.LilithPortrait);
        createAvatar(AvatarAssets.ScarlettPortrait);

        TextButton nxtBtn = new TextButton(lang.getMessage("NEXT"), skin);
        nxtBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearScreen();
                showChooseGunScreen();
            }
        });
        currView.add(nxtBtn).colspan(2).row();
        stage.addActor(currView);
    }

    public void createAvatar(Texture avatar) {
        Image profileImage = new Image();
        Texture profileTexture = avatar;

        Color shadowColor = new Color(0, 0, 0, 0.5f);

        profileImage.setDrawable(new TextureRegionDrawable(new TextureRegion(profileTexture)));

        if (currentAvatar != null && !currentAvatar.equals(avatar)) {
            profileImage.setColor(shadowColor);
        }
        profileImage.setScaling(Scaling.fit);

        profileImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentAvatar = avatar;
                currAvatarImage.setDrawable(new TextureRegionDrawable(
                    new TextureRegion(currentAvatar)));
                clearScreen();
                showChooseAvatarScreen();
            }
        });
        currView.add(profileImage).size(150, 150).pad(10);
    }

    public void showChooseGunScreen() {
        clearScreen();
        state = ScreenState.GUN;

        currView = new Table();
        currView.setFillParent(true);

        Texture gunTexture = currentGun;
        currGunImage.setDrawable(new TextureRegionDrawable(new TextureRegion(gunTexture)));

        currGunImage.setScaling(Scaling.fit);

        String name = gunToName(currentGun);
        currGuns = Guns.findGunByName(name);
        currView.add(new Label(lang.getMessage("AMMO_MAX")+" : " + currGuns.getAmmoMax(), skin)).colspan(3);
        currView.add(new Label(lang.getMessage("TIME_RELOAD")+" : " + currGuns.getTimeReload(), skin)).colspan(3).row();
        currView.add(new Label(lang.getMessage("PROJECTILE")+" : " + currGuns.getProjectile(), skin)).colspan(3);
        currView.add(new Label(lang.getMessage("DAMAGE")+" : " + currGuns.getDamage(), skin)).colspan(3).row();
        currView.add(currGunImage).height(200).width(200).colspan(6).padBottom(15).row();

        TextButton backBtn = new TextButton(lang.getMessage("BACK"), skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearScreen();
                showChooseAvatarScreen();
            }
        });
        currView.add(backBtn).colspan(2);

        createGun(AvatarAssets.Revolver);
        createGun(AvatarAssets.Shotgun);
        createGun(AvatarAssets.Smg);

        TextButton nxtBtn = new TextButton(lang.getMessage("NEXT"), skin);
        nxtBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearScreen();
                showStartGameScreen();
            }
        });
        currView.add(nxtBtn).colspan(2).row();
        stage.addActor(currView);
    }

    public void createGun(Texture gun) {
        Image gunImage = new Image();
        Texture gunTexture = gun;

        Color shadowColor = new Color(0, 0, 0, 0.5f);

        gunImage.setDrawable(new TextureRegionDrawable(new TextureRegion(gunTexture)));

        if (currentGun != null && !currentGun.equals(gun)) {
            gunImage.setColor(shadowColor);
        }
        gunImage.setScaling(Scaling.fit);

        gunImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentGun = gun;
                currGunImage.setDrawable(new TextureRegionDrawable(
                    new TextureRegion(currentGun)));
                clearScreen();
                showChooseGunScreen();
            }
        });
        currView.add(gunImage).size(150, 150).pad(10);
    }

    public String gunToName(Texture g) {
        return g.toString().split("/")[1].split("\\.")[0];
    }

    public void showStartGameScreen() {
        clearScreen();
        state = ScreenState.START_GAME;

        currView = new Table();
        currView.setFillParent(true);

        Texture gunTexture = currentGun;
        Texture avatarTexture = currentAvatar;
        currGunImage.setDrawable(new TextureRegionDrawable(new TextureRegion(gunTexture)));
        currAvatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(avatarTexture)));

        currView.add(new Label(lang.getMessage("GUN"), skin, "subtitle")).colspan(6).pad(15);
        currView.add(new Label(lang.getMessage("AVATAR"), skin, "subtitle")).colspan(6).pad(15).row();
        currView.add(currGunImage).height(200).width(200).colspan(6).pad(15);
        currView.add(currAvatarImage).height(200).width(200).colspan(6).pad(15).row();

        TextButton backBtn = new TextButton(lang.getMessage("BACK"), skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearScreen();
                showChooseGunScreen();
            }
        });
        currView.add(backBtn).colspan(6).padRight(20);

        SelectBox<Float> timeSelect = new SelectBox<>(skin);
        timeSelect.setItems(new Array<Float>(new Float[]{2.5f, 5f, 10f, 20f}));
        timeSelect.setSelected(gameTime);

        timeSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                gameTime = timeSelect.getSelected();
            }
        });

        currView.add(timeSelect).colspan(2);
        currView.add(new Label(lang.getMessage("GAME_TIME"), skin)).padLeft(10).row();
        TextButton startGameBtn = new TextButton(lang.getMessage("START_GAME"), skin);
        startGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearScreen();
                startNewGame();
                App.setCurrScreen(new GameMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });
        currView.add(startGameBtn).colspan(11).pad(20).row();

        stage.addActor(currView);
    }

    public void startNewGame() {
        Game game = new Game();
        Player player = new Player();
        player.setUserId(user.get_id());
        player.setHero(currHero);
        player.setGun(currGuns);
        player.setGunAmmo(currGuns.getAmmoMax());
        Coordinate coord = new Coordinate();
        coord.setX(Gdx.graphics.getWidth() / 2f);
        coord.setY(Gdx.graphics.getHeight() / 2f);
        player.setCoordinate(coord);
        player.setGunFilPath(currentGun.toString());
        player.setHeroAvatar(currentAvatar.toString());
        player.setHp(currHero.getHp());
        player.setXp(0);
        game.setPlayer(player);
        game.setGameMaxTime(gameTime);
        if (!user.isGuest())
            GameRepository.save(game);
        user.setCurrentGame(game);
        if (!user.isGuest())
            UserRepository.save(user);
        if (user.isGuest())
            player.setUser(user);
    }

    public void clearScreen() {
        if (currView != null) {
            currView.remove();
        }
    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        Image back = new Image(GameAssets.PreGameMenuBackground);
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
        stage.dispose();
        skin.dispose();
    }
}
