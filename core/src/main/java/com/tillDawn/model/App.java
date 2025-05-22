package com.tillDawn.model;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.tillDawn.model.Game.Game;
import com.tillDawn.view.SignInLoginMenu;

public class App {
    private static Screen currScreen;
    private static User currUser;
    private static Music currMusic;
    private static boolean sfxStage = true;
    private static final Buttons[] buttons = {Buttons.UP, Buttons.DOWN, Buttons.LEFT, Buttons.RIGHT,
        Buttons.AutoAim, Buttons.ReloadGun, Buttons.Shoot};


    public static Screen getCurrScreen() {
        return currScreen;
    }

    public static void setCurrScreen(Screen screen) {
        currScreen = screen;
    }

    public static User getCurrUser() {
        return currUser;
    }

    public static void setCurrUser(User currUser) {
        App.currUser = currUser;
    }

    public static Music getCurrMusic() {
        return currMusic;
    }

    public static void setCurrMusic(Music currMusic) {
        App.currMusic = currMusic;
    }

    public static boolean isSfxStage() {
        return sfxStage;
    }

    public static void setSfxStage(boolean sfxStage) {
        App.sfxStage = sfxStage;
    }

    public static Buttons[] getButtons() {
        return buttons;
    }
}
