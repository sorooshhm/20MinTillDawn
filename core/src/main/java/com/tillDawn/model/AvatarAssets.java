package com.tillDawn.model;

import com.badlogic.gdx.graphics.Texture;

public class AvatarAssets {
    public static Texture DasherPortrait;
    public static Texture DiamondPortrait;
    public static Texture LilithPortrait;
    public static Texture ScarlettPortrait;
    public static Texture ShanaPortrait;
    public static Texture Revolver;
    public static Texture Shotgun;
    public static Texture Smg;

    public AvatarAssets() {
        DasherPortrait = new Texture("images/Sprite/T_Dasher_Portrait.png");
        DiamondPortrait = new Texture("images/Sprite/T_Diamond_Portrait.png");
        LilithPortrait = new Texture("images/Sprite/T_Lilith_Portrait.png");
        ScarlettPortrait = new Texture("images/Sprite/T_Scarlett_Portrait.png");
        ShanaPortrait = new Texture("images/Sprite/T_Shana_Portrait.png");
        Revolver = new Texture("images/Revolver.png");
        Shotgun = new Texture("images/Shotgun.png");
        Smg = new Texture("images/Smg.png");
    }

    public static Texture getAvatarTexture(String path) {
        String name = avatarToName(path);
        if (name.equals("Dasher")) {
            return DasherPortrait;
        } else if (name.equals("Diamond")) {
            return DiamondPortrait;
        } else if (name.equals("Lilith")) {
            return LilithPortrait;
        } else if (name.equals("Scarlett")) {
            return ScarlettPortrait;
        } else if (name.equals("Shana")) {
            return ShanaPortrait;
        }
        return null;
    }

    public static Texture getGunTexture(String path) {
        String name = gunToName(path);
        if (name.equals("Revolver")) {
            return Revolver;
        } else if (name.equals("Shotgun")) {
            return Shotgun;
        } else if (name.equals("Smg")) {
            return Smg;
        }
        return null;
    }

    public static String gunToName(String g) {
        return g.split("/")[1].split("\\.")[0];
    }


    public static String avatarToName(String t) {
        String s = t.split("_")[1].trim();
        return s;
    }
}
