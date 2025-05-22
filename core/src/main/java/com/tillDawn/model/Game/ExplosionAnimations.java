package com.tillDawn.model.Game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tillDawn.model.BaziAssets;

public class ExplosionAnimations {
    private Animation<TextureRegion> animation;
    private float stateTime = 0;
    private float x = 0;
    private float y = 0;
    private float width ;
    private float height ;

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

    public ExplosionAnimations(float x, float y) {
        this.animation = BaziAssets.initialAnimationOfExplosion();
        this.x = x;
        this.y = y;
        this.width = BaziAssets.getFramesOfExplosion()[0].getRegionWidth();
        this.height = BaziAssets.getFramesOfExplosion()[0].getRegionHeight();
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }
}
