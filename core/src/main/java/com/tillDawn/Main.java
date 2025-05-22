package com.tillDawn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.tillDawn.model.App;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.User;
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.utilities.DBConnection;
import com.tillDawn.view.EditProfileMenu;
import com.tillDawn.view.MainMenu;
import com.tillDawn.view.SignInLoginMenu;
import io.github.cdimascio.dotenv.Dotenv;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    private Sound clickSound;

    @Override
    public void create() {
        main = this;
        new GameAssets();
        Dotenv.configure()
            .directory("D:/tamrin-ha/ap/tamrin-3/core/src/main/java/com/tillDawn/.env")
            .systemProperties()
            .load();
        DBConnection.getDatabase();
        User user = UserRepository.getLoggedInUser();
        if (user != null) {
            App.setCurrUser(user);
            App.setCurrScreen(new MainMenu(main));

        } else
            App.setCurrScreen(new SignInLoginMenu(main));
//        GameAssets.EnterSandMan.play();


        App.setCurrMusic(GameAssets.EnterSandMan);
        GameAssets.CursorTexture.getTextureData().prepare();
        Pixmap cursorPixmap =  GameAssets.CursorTexture.getTextureData().consumePixmap();

        Cursor customCursor = Gdx.graphics.newCursor(cursorPixmap, 1, 2);
        Gdx.graphics.setCursor(customCursor);

        cursorPixmap.dispose();
        this.setScreen(App.getCurrScreen());
    }

    public void handleFileDrops(String[] files) {
        if(App.getCurrScreen() instanceof EditProfileMenu) {
            ((EditProfileMenu) App.getCurrScreen()).handleFileDrops(files);
        }
    }

    public static Main getMain() {
        return main;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        screen.dispose();
    }
}
