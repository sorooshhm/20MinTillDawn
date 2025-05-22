package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.tillDawn.controller.ProfileMenuController;
import com.tillDawn.model.App;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.IO.Request;
import com.tillDawn.model.IO.Response;
import com.tillDawn.model.User;
import com.tillDawn.utilities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditProfileMenu implements Screen {
    private static final Logger log = LoggerFactory.getLogger(EditProfileMenu.class);
    private Main main;
    private Stage stage;
    private Skin skin;
    private TextField usernameField;
    private TextField passwordField;
    private String currentAvatar;
    private Image profileImage;
    private User user;
    private final ProfileMenuController controller = new ProfileMenuController();
    private boolean logout = false;
    private BiLingualHandling lang = BiLingualHandling.getInstance();

    public EditProfileMenu(Main main) {
        this.main = main;
        skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        setStage();
        user = App.getCurrUser();
        profileImage = new Image();
        createUI();
    }

    public void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        Texture profileTexture = new Texture(Gdx.files.internal(user.getAvatar()));
        profileImage.setDrawable(new TextureRegionDrawable(new TextureRegion(profileTexture)));

        profileImage.setScaling(Scaling.fit);
        table.add(profileImage).size(150, 150);
        Table infoTable = new Table();
        infoTable.defaults().left();

        infoTable.add(new Label(lang.getMessage("USERNAME") + ":", skin)).row();
        usernameField = new TextField(user.getUsername(), skin);

        passwordField = new TextField(user.getPassword(), skin);

        currentAvatar = user.getAvatar();
        infoTable.add(usernameField).width(500).padBottom(15).row();
        infoTable.add(new Label(lang.getMessage("PASSWORD") + ":", skin)).row();
        infoTable.add(passwordField).width(500).padBottom(15).row();
        ArrayList<String> arr = new ArrayList<>();

        arr.add("images/avatars/1.jpg");
        arr.add("images/avatars/2.jpg");
        arr.add("images/avatars/3.jpg");
        arr.add("images/avatars/4.jpg");
        Selector avatarSelector = new Selector(skin, arr);

        TextButton selectBtn = new TextButton(lang.getMessage("SELECT_AVATAR"), skin);
        selectBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                avatarSelector.toggleVisibility();

                if (avatarSelector.getSelectedAvatarPath() != null) {
                    currentAvatar = avatarSelector.getSelectedAvatarPath();
                    profileImage.setDrawable(new TextureRegionDrawable(
                        new TextureRegion(new Texture(Gdx.files.internal(
                            currentAvatar)))));
                }
            }
        });

        TextButton fileButton = new TextButton(lang.getMessage("UPLOAD"), skin);
        fileButton.setPosition(100, 200);
        fileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                File file = FileExplorer.openFileExplorer();
                if (file != null) {
                    FileSaver.saveFile(file, "images");
                    currentAvatar = file.getAbsolutePath();
                    profileImage.setDrawable(new TextureRegionDrawable(
                        new TextureRegion(new Texture(Gdx.files.internal(
                            currentAvatar)))));
                }
            }
        });

        table.add(fileButton).size(150, 150).row();

        TextButton submitBtn = new TextButton(lang.getMessage("SUBMIT"), skin);
        submitBtn.addListener(new

                                  ClickListener() {
                                      @Override
                                      public void clicked(InputEvent event, float x, float y) {
                                          handleUpdateProfile();
                                      }
                                  });

        TextButton backBtn = new TextButton(lang.getMessage("BACK"), skin);
        backBtn.addListener(new

                                ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        dispose();
                                        App.setCurrScreen(new ProfileMenu(main));
                                        main.setScreen(App.getCurrScreen());
                                    }
                                });

        TextButton logoutBtn = new TextButton(lang.getMessage("LOGOUT"), skin);
        logoutBtn.addListener(new

                                  ClickListener() {
                                      @Override
                                      public void clicked(InputEvent event, float x, float y) {
                                          handleLogout();
                                      }
                                  });

        infoTable.add(selectBtn).

            width(500).

            height(50).

            padBottom(20).

            row();
        infoTable.add(avatarSelector.getContainer()).

            center().

            row();
        infoTable.add(submitBtn).

            width(200).

            height(50).

            center().

            padTop(5).

            row();
        infoTable.add(backBtn).

            width(200).

            height(50).

            center().

            padTop(5).

            row();
        infoTable.add(logoutBtn).

            width(200).

            height(50).

            center().

            padTop(5).

            row();
        table.add(infoTable).

            colspan(2).

            padBottom(40f);
        infoTable.pad(40f);
    }

    public void handleLogout() {
        Response res = controller.logout(new Request("logout"));
        logout = true;
        showMessage(res);
    }

    public void handleFileDrops(String[] files) {
        Gdx.app.postRunnable(() -> {
            for (String filePath : files) {
                FileHandle source = Gdx.files.absolute(filePath);
                FileHandle dest = Gdx.files.local("images/" + source.name());

                try {
                    source.copyTo(dest);
                    Gdx.app.log("FileDrop", "Copied: " + source.name());
                    currentAvatar = filePath;
                    profileImage.setDrawable(new TextureRegionDrawable(
                        new TextureRegion(new Texture(Gdx.files.internal(
                            currentAvatar)))));
                } catch (Exception e) {
                    Gdx.app.error("FileDrop", "Error copying file", e);
                }
            }
        });
    }

    private void handleUpdateProfile() {
        Request req = new Request("profileUpdate");
        req.body.put("username", usernameField.getText().trim());
        req.body.put("password", passwordField.getText().trim());
        req.body.put("avatar", currentAvatar);

        Response res = controller.editProfile(req);
        showMessage(res);
    }

    private void showMessage(Response res) {
        if (res.success) {
            Dialog dialog = new Dialog(lang.getMessage("SUCCESS"), skin);
            dialog.text(res.message).padTop(20f);
            dialog.button(lang.getMessage("OK"));
            dialog.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (logout) {
                        dispose();
                        App.setCurrScreen(new SignInLoginMenu(main));
                        main.setScreen(App.getCurrScreen());
                    }
                }
            });
            dialog.show(stage);
        } else {
            Dialog dialog = new Dialog(lang.getMessage("ERROR"), skin);
            dialog.text(res.message).padTop(20f);
            dialog.button(lang.getMessage("OK"));
            dialog.show(stage);
        }
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
