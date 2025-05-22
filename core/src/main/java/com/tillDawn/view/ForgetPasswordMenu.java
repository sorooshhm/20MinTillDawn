package com.tillDawn.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tillDawn.Main;
import com.tillDawn.controller.SignInLoginController;
import com.tillDawn.model.App;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.IO.Request;
import com.tillDawn.model.IO.Response;
import com.tillDawn.model.User;
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.utilities.SoundInputProcessor;

public class ForgetPasswordMenu implements Screen {
    private Main main;
    private Stage stage;
    private Skin skin;
    private TextField usernameField;
    private TextField answerField;
    private TextField passwordField;
    private TextField confirmPasswordField;
    private Table table;

    public ForgetPasswordMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        setStage();

        createUI();
    }

    public void createUI() {
        table = new Table();
        table.setFillParent(true);

        stage.addActor(table);

        table.add(new Label("Forget Password", skin, "title")).colspan(2).padBottom(15).row();
        table.add(new Label("Username:", skin)).padRight(10).center();
        usernameField = new TextField("", skin);
        table.add(usernameField).width(500).padBottom(15).row();

        TextButton findBtn = new TextButton("Find", skin);
        findBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                User user = UserRepository.findUserByUsername(username);
                if(user == null) {
                    showError("User not found");
                }else {
                    showAnswerSection(user);
                }
            }
        });

        table.add(findBtn).width(500).padBottom(15).padTop(15).colspan(2).row();
    }

    private void showError(String message) {
        Dialog dialog = new Dialog("Error", skin);
        dialog.text(message).padTop(20f);
        dialog.button("OK");
        dialog.show(stage);
    }

    public void showAnswerSection(User user){
        String question = user.getSecurityQuestion();
        table.add(new Label(question , skin)).padBottom(20).colspan(2).row();
        answerField = new TextField("", skin);
        table.add(answerField).width(500).padBottom(15).colspan(2).row();

        TextButton answerBtn = new TextButton("Answer", skin);
        answerBtn.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               String answer = answerField.getText();
               if(user.getSecurityAnswer().equals(answer)) {
                   table.reset();
                   createForgetPassUI();
               }
           }
        });
        table.add(answerBtn).width(500).padBottom(15).colspan(2).row();
    }

    public void createForgetPassUI(){
        table.add(new Label("Change Password" , skin , "title")).row();

        table.add(new Label("Password:", skin)).padRight(10).center();
        passwordField = new TextField("", skin);
        table.add(passwordField).width(500).padBottom(15).row();

        table.add(new Label("Confirm Password:", skin)).padRight(10).center();
        confirmPasswordField = new TextField("", skin);
        table.add(confirmPasswordField).width(500).padBottom(15).row();

        TextButton confirmBtn = new TextButton("Confirm", skin);
        confirmBtn.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
                handleChangePassword();
           }
        });

        table.add(confirmBtn).width(500).padBottom(15).colspan(2).row();

    }

    public void handleChangePassword(){
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String username = usernameField.getText();

        Request req = new Request("change-password");
        req.body.put("username", username);
        req.body.put("password", password);
        req.body.put("confirmPassword", confirmPassword);
        Response res = new SignInLoginController().changePassword(req);
        showMessage(res);
    }

    private void showMessage(Response res) {
        if (res.success) {
            Dialog dialog = new Dialog("Success", skin);
            dialog.text(res.message).padTop(20f);
            dialog.button("OK");
            dialog.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    App.setCurrScreen(new MainMenu(Main.getMain()));
                    dispose();
                    main.setScreen(App.getCurrScreen());
                }
            });
            dialog.show(stage);
        } else {
            Dialog dialog = new Dialog("Error", skin);
            dialog.text(res.message).padTop(20f);
            dialog.button("OK");
            dialog.show(stage);
        }
    }

    private void setStage() {
        stage = new Stage(new ScreenViewport());
        Image back = new Image(GameAssets.SignInMenuBackground);
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

    }
}
