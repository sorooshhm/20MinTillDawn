package com.tillDawn.model.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tillDawn.model.BaziAssets;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

@Embedded
public class HasturBoss {
    private float x;
    private float y;
    private float width;
    private float height;
    @Transient
    private Animation<TextureRegion> animation;
    @Transient
    private Rectangle rectangle;
    @Transient
    private Rectangle bounds;
    private float stateTime = 0;
    private float hp = 400;
    private float walkStateTime = 0;

    public HasturBoss() {
    }

    public HasturBoss(float x, float y) {
        this.x = x;
        this.y = y;
        animation = BaziAssets.initialAnimationOfHasturBoss();
        width = BaziAssets.getFramesOfHasturBoss()[0].getRegionWidth();
        height = BaziAssets.getFramesOfHasturBoss()[0].getRegionHeight();
        rectangle = new Rectangle(x, y, width, height);
        bounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
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

    public Animation<TextureRegion> getAnimation() {
        if (animation == null) {
            animation = BaziAssets.initialAnimationOfHasturBoss();
        }
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public Rectangle getRectangle() {
        if (rectangle == null) {
            rectangle = new Rectangle(x, y, width, height);
        }
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Rectangle getBounds() {
        if (bounds == null) {
            bounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public float getWalkStateTime() {
        return walkStateTime;
    }

    public void setWalkStateTime(float walkStateTime) {
        this.walkStateTime = walkStateTime;
    }
}
