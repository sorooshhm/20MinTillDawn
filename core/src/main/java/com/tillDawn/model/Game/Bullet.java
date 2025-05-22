package com.tillDawn.model.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tillDawn.model.BaziAssets;

public class Bullet {
    private Texture texture;
    private Sprite sprite;
    private int damage;
    private Rectangle rectangle;
    private int x;
    private int y;
    private Vector2 direction;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bullet(int x, int y, float srcX, float srcY) {
        this.damage = 5;
        this.x = x;
        this.y = y;
        texture = BaziAssets.bullet;
        sprite = new Sprite(texture);
        sprite.setSize(20, 20);
        sprite.setX(Gdx.graphics.getWidth() / 2f);
        sprite.setY(Gdx.graphics.getHeight() / 2f);
        rectangle = new Rectangle(x, y, 20, 20);
        direction = new Vector2(
            srcX - x,
            srcY - y
        ).nor();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Vector2 getDirection() {
        return direction;
    }
}
