package com.tillDawn.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BaziAssets {
    public static Texture background;
    public static Texture bullet;
    public static Texture enemyBullet;
    public static Texture dragonEgg;
    public static Texture damage;

    public BaziAssets() {
        background = new Texture("images/background.png");
        bullet = new Texture("images/bullet.png");
        enemyBullet =  new Texture("images/Sprite/T_Shotgun_SS_1.png");
        dragonEgg = new Texture("images/Sprite/T_DragonEgg.png");
        damage = new Texture("images/Sprite/ShoggothWindup.png");
    }

    public static TextureRegion[] getFrames(String Path) {
        String name = AvatarAssets.avatarToName(Path);
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            frames[i] = new TextureRegion(new Texture("images/Sprite/heroes/" + name + "/" + i + ".png"));
        }
        return frames;
    }

    public static Animation<TextureRegion> initialAnimation(String Path) {
        TextureRegion[] frames = getFrames(Path);
        return new Animation<>(0.1f, frames);
    }

    public static TextureRegion[] getFramesOfTree() {
        TextureRegion[] frames = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            frames[i] = new TextureRegion(new Texture("images/Sprite/T_TreeMonster_" + i + ".png"));
        }
        return frames;
    }

    public static Animation<TextureRegion> initialAnimationOfTree() {
        TextureRegion[] frames = getFramesOfTree();
        return new Animation<>(0.5f, frames);
    }

    public static TextureRegion[] getFramesOfTentacle() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("images/Sprite/BrainMonster_" + i + ".png"));
        }
        return frames;
    }

    public static Animation<TextureRegion> initialAnimationOfTentacle() {
        TextureRegion[] frames = getFramesOfTentacle();
        return new Animation<>(0.5f, frames);
    }

    public static TextureRegion[] getFramesOfEyeBat() {
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = new TextureRegion(new Texture("images/Sprite/T_EyeBat_" + i + ".png"));
        }
        return frames;
    }

    public static Animation<TextureRegion> initialAnimationOfEyeBat() {
        TextureRegion[] frames = getFramesOfEyeBat();
        return new Animation<>(0.15f, frames);
    }

    public static TextureRegion[] getFramesOfExplosion() {
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            frames[i] = new TextureRegion(new Texture("images/Sprite/ExplosionFX_" + i + ".png"));
        }
        return frames;
    }

    public static Animation<TextureRegion> initialAnimationOfExplosion(){
        TextureRegion[] frames = getFramesOfExplosion();
        return new Animation<>(.1f, frames);
    }

    public static TextureRegion[] getFramesOfHasturBoss() {
        TextureRegion[] frames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            frames[i] = new TextureRegion(new Texture("images/Sprite/T_HasturBoss_" + i + ".png"));
        }
        return frames;
    }

    public static Animation<TextureRegion> initialAnimationOfHasturBoss(){
        TextureRegion[] frames = getFramesOfHasturBoss();
        return new Animation<>(0.2f, frames);
    }
}
