package com.tillDawn.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class GameAssets {
    public static Texture SignInMenuBackground;
    public static Texture HintMenuBackground;
    public static Texture MainMenuTitle;
    public static Texture MainMenuLeaves;
    public static Texture PreGameMenuBackground;
    public static Texture MainMenuLeaves2;
    public static Texture SettingMenuBackground;
    public static Texture ProfileMenuBackground;
    public static Texture EditButtonsMenuBackground;
    public static Texture OpenEyeTexture;
    public static Texture HalfClosedEyeTexture;
    public static Texture ClosedEyeTexture;
    public static Texture CursorTexture;
    public static Music EnterSandMan;
    public static Music ShowMustGoOn;
    public static Music SouthOfHeaven;
    public static Sound clickSound ;

    public GameAssets() {
        HintMenuBackground = new Texture(Gdx.files.internal("images/MainMenuBackground.png"));
        SignInMenuBackground = new Texture("images/SignInMenuBackground.jpg");
        MainMenuTitle = new Texture("images/Sprite/T_20Logo.png");
        MainMenuLeaves = new Texture("images/Sprite/T_TitleLeaves.png");
        MainMenuLeaves2 = new Texture("images/Sprite/T_TitleLeaves2.png");
        SettingMenuBackground = new Texture("images/SettingMenuBackground.jpg");
        ProfileMenuBackground = new Texture("images/ProfileMenuBackground.jpg");
        EditButtonsMenuBackground = new Texture("images/EditButtonsBackground.png");
        EnterSandMan = Gdx.audio.newMusic(Gdx.files.internal("music/Enter-Sandman.mp3"));
        ShowMustGoOn = Gdx.audio.newMusic(Gdx.files.internal("music/show-must-go-on.mp3"));
        SouthOfHeaven = Gdx.audio.newMusic(Gdx.files.internal("music/South-Of-Heaven.mp3"));
        OpenEyeTexture = new Texture("images/Sprite/T_EyeBlink_0.png");
        HalfClosedEyeTexture = new Texture("images/Sprite/T_EyeBlink_1.png");
        ClosedEyeTexture = new Texture("images/Sprite/T_EyeBlink_2.png");
        CursorTexture = new Texture("images/Sprite/T_CursorSprite.png");
        PreGameMenuBackground = new Texture("images/PreGameMenuBackground.jpg");
        clickSound = Gdx.audio.newSound(Gdx.files.internal("AudioClip/click.wav"));

    }
}
