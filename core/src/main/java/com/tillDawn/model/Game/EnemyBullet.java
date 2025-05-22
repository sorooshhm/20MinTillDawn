package com.tillDawn.model.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tillDawn.model.BaziAssets;

public class EnemyBullet {
    private Texture texture;
    private Sprite sprite;
    private float damage;
    private Rectangle rectangle;
    private float x;
    private float y;
    private Vector2 direction;

    public EnemyBullet(float x, float y) {
        this.damage = .1f;
        texture = BaziAssets.enemyBullet;
        sprite = new Sprite(texture);
        sprite.setSize(20, 20);
        sprite.setX(x);
        sprite.setY(y);
        rectangle = new Rectangle(x, y, 20, 20);
        direction = new Vector2(
            Gdx.graphics.getWidth() / 2f - sprite.getX(),
            Gdx.graphics.getHeight() / 2f - sprite.getY()
        ).nor();
    }

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

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
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

    public Vector2 getDirection() {
        return direction;
    }
}
