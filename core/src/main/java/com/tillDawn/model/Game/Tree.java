package com.tillDawn.model.Game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tillDawn.model.BaziAssets;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;


@Embedded
public class Tree {
    @Transient
    private Animation<TextureRegion> animation;
    @Transient
    private Rectangle rectangle;
    private float x;
    private float y;
    private float width;
    private float height;
    private float stateTime;
    private float damage = 0.1f;

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

    public Tree() {
    }

    public Tree(float x, float y) {
        this.x = x;
        this.y = y;
        width = BaziAssets.getFramesOfTree()[0].getRegionWidth()*1.5f;
        height = BaziAssets.getFramesOfTree()[0].getRegionHeight()*1.5f;
        rectangle = new Rectangle(x, y, width/1.5f, height/1.5f);
        animation = BaziAssets.initialAnimationOfTree();
    }

    public Animation<TextureRegion> getAnimation() {
        if (animation == null) {
            animation = BaziAssets.initialAnimationOfTree();
        }
        return animation;
    }

    public Rectangle getRectangle() {
        if (rectangle == null) {
            rectangle = new Rectangle(x, y, width, height);
        }
        return rectangle;
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

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public float getDamage() {
        return damage;
    }
}
