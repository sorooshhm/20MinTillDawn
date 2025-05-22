package com.tillDawn.model.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.tillDawn.model.BaziAssets;

public class XP {
    private float x;
    private float y;
    private float width;
    private float height;
    private Texture texture;
    private Rectangle rectangle;

    public XP(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = BaziAssets.dragonEgg;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        rectangle = new Rectangle(x , y, width , height);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
