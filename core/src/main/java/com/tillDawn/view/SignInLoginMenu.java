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
import com.tillDawn.utilities.BiLingualHandling;
import com.tillDawn.utilities.SoundInputProcessor;
import org.bson.types.ObjectId;

public class SignInLoginMenu implements Screen {
    private final Main main;
    private Stage stage;
    private Skin skin;
    private TextField usernameField;
    private TextField passwordField;
    private SelectBox<String> securityQuestionSelect;
    private TextField securityAnswerField;
    private CheckBox rememberMeCheckbox;
    private final SignInLoginController controller = new SignInLoginController();
    private final BiLingualHandling lang = BiLingualHandling.getInstance();

    public SignInLoginMenu(Main main) {
        this.skin = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        this.main = main;
        setStage();

        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(new Label(lang.getMessage("SIGN_UP"), skin, "title")).colspan(2).padBottom(30).row();

        table.add(new Label(lang.getMessage("USERNAME") + ":", skin)).padRight(10).right();
        usernameField = new TextField("", skin);
        table.add(usernameField).width(500).padBottom(15).row();

        table.add(new Label(lang.getMessage("PASSWORD") + ":", skin)).padRight(10).right();
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(500).padBottom(15).row();

        table.add(new Label(lang.getMessage("SECURITY_QUESTION") + ":", skin)).padRight(10).right();
        securityQuestionSelect = new SelectBox<>(skin);
        securityQuestionSelect.setItems(
            lang.getMessage("QUESTION_1"),
            lang.getMessage("QUESTION_2"),
            lang.getMessage("QUESTION_3")
        );
        table.add(securityQuestionSelect).width(500).padBottom(15).row();

        table.add(new Label(lang.getMessage("SECURITY_ANSWER") + ":", skin)).padRight(10).right();
        securityAnswerField = new TextField("", skin);
        table.add(securityAnswerField).width(500).padBottom(15).row();

        Table buttonsTable = new Table();
        buttonsTable.defaults().pad(10).width(250);

        rememberMeCheckbox = new CheckBox(" " + lang.getMessage("REMEMBER_ME"), skin);
        table.add(rememberMeCheckbox).colspan(2).padLeft(300f).left().row();

        TextButton signUpButton = new TextButton(lang.getMessage("SIGN_UP"), skin);
        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleSignUp();
            }
        });
        buttonsTable.add(signUpButton);

        TextButton loginButton = new TextButton(lang.getMessage("LOGIN"), skin);
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.reset();
                usernameField.setText("");
                passwordField.setText("");
                securityAnswerField.setText("");
                securityQuestionSelect.setSelectedIndex(0);
                rememberMeCheckbox.setChecked(false);
                createLoginUi();
            }
        });

        TextButton guestButton = new TextButton(lang.getMessage("GUEST"), skin);
        guestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                User user = new User();
                user.set_id(new ObjectId());
                user.setUsername("GUEST");
                user.setAvatar("images/avatars/3.jpg");
                user.setGuest(true);
                App.setCurrUser(user);
                dispose();
                App.setCurrScreen(new MainMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });


        buttonsTable.add(signUpButton);
        buttonsTable.add(loginButton).row();
        buttonsTable.add(guestButton).colspan(2).right();


        table.add(buttonsTable).colspan(2).padTop(20);
    }

    public void createLoginUi() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        table.add(new Label(lang.getMessage("SIGN_IN"), skin, "title")).colspan(2).padBottom(30).row();

        // Username
        table.add(new Label(lang.getMessage("USERNAME") + ":", skin)).padRight(10).right();
        usernameField = new TextField("", skin);
        table.add(usernameField).width(500).padBottom(15).row();

        // Password
        table.add(new Label(lang.getMessage("PASSWORD") + ":", skin)).padRight(10).right();
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(500).padBottom(15).row();


        rememberMeCheckbox = new CheckBox(" " + lang.getMessage("REMEMBER_ME"), skin);
        table.add(rememberMeCheckbox).colspan(2).padLeft(180f).left().row();

        // Buttons
        Table buttonsTable = new Table();
        buttonsTable.defaults().pad(10).width(250);

        // Sign Up Button
        TextButton signUpButton = new TextButton(lang.getMessage("LOGIN"), skin);
        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleLogin();
            }
        });
        TextButton signInButton = new TextButton(lang.getMessage("SIGN_IN"), skin);
        signInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.reset();
                usernameField.setText("");
                passwordField.setText("");
                securityAnswerField.setText("");
                securityQuestionSelect.setSelectedIndex(0);
                rememberMeCheckbox.setChecked(false);
                createUI();
            }
        });
        TextButton forgetBtn = new TextButton(lang.getMessage("FORGOT_PASSWORD"), skin);
        forgetBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                App.setCurrScreen(new ForgetPasswordMenu(main));
                main.setScreen(App.getCurrScreen());
            }
        });

        buttonsTable.add(signUpButton);
        buttonsTable.add(signInButton).row();
        buttonsTable.add(forgetBtn).width(500).colspan(2).right();

        table.add(buttonsTable).colspan(2).padTop(20).padLeft(150);
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String securityAnswer = securityAnswerField.getText().trim();
        String securityQuestion = securityQuestionSelect.getSelected();
        Request req = new Request("signup");
        req.body.put("username", username);
        req.body.put("password", password);
        req.body.put("securityQuestion", securityQuestion);
        req.body.put("securityAnswer", securityAnswer);
        req.body.put("rememberMe", rememberMeCheckbox.isChecked() + "");

        Response res = controller.signIn(req);
        showMessage(res);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        Request req = new Request("login");
        req.body.put("username", username);
        req.body.put("password", password);
        req.body.put("rememberMe", rememberMeCheckbox.isChecked() + "");

        Response res = controller.login(req);
        showMessage(res);
    }

    private void showError(String message) {
        Dialog dialog = new Dialog(lang.getMessage("ERROR"), skin);
        dialog.text(message).padTop(20f);
        dialog.button(lang.getMessage("OK"));
        dialog.show(stage);
    }

    private void showMessage(Response res) {
        if (res.success) {
            Dialog dialog = new Dialog(lang.getMessage("SUCCESS"), skin);
            dialog.text(res.message).padTop(20f);
            dialog.button(lang.getMessage("OK"));
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
            Dialog dialog = new Dialog(lang.getMessage("ERROR"), skin);
            dialog.text(res.message).padTop(20f);
            dialog.button(lang.getMessage("OK"));
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
        stage.dispose();
        skin.dispose();
    }
}
