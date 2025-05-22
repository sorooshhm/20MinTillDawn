package com.tillDawn.model;

import com.badlogic.gdx.Input;

import java.awt.*;

public enum Buttons {
    UP(Input.Keys.W),
    DOWN(Input.Keys.S),
    LEFT(Input.Keys.A),
    RIGHT(Input.Keys.D),
    Shoot(Input.Buttons.LEFT),
    AutoAim(Input.Keys.SPACE),
    ReloadGun(Input.Keys.R),;

    private final int original;
    private int code;
    Buttons(int code) {
        this.code = code;
        this.original = code;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public Buttons findButtonByCode(int code) {
        for (Buttons button : Buttons.values()) {
            if (button.getCode() == code && button != this) {
                return button;
            }
        }
        return null;
    }

    public int getOriginal() {
        return original;
    }
}
