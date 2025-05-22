package com.tillDawn.model.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tillDawn.model.BaziAssets;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;

import java.util.ArrayList;

@Embedded
public class EyeBat {
    private float x;
    private float y;
    private float width;
    private float height;
    @Transient
    private Animation<TextureRegion> animation;
    @Transient
    private Rectangle rectangle;
    private float hp = 50;
    private float stateTime = 0;
    private float shotTime = 0;
    @Transient
    private ArrayList<EnemyBullet> bullets = new ArrayList<>();
    @Transient
    private Color color = null;

    public EyeBat(){

    }

    public EyeBat(float x, float y) {
        this.x = x;
        this.y = y;
        animation = BaziAssets.initialAnimationOfEyeBat();
        width = BaziAssets.getFramesOfEyeBat()[0].getRegionWidth();
        height = BaziAssets.getFramesOfEyeBat()[0].getRegionHeight();
        rectangle = new Rectangle(x, y, width, height);
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
            animation = BaziAssets.initialAnimationOfEyeBat();
        }
        return animation;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public Rectangle getRectangle() {
        if(rectangle == null){
            rectangle = new Rectangle(x, y, width, height);
        }
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public float getShotTime() {
        return shotTime;
    }

    public void setShotTime(float shotTime) {
        this.shotTime = shotTime;
    }

    public ArrayList<EnemyBullet> getBullets() {
        if(bullets == null){
            bullets = new ArrayList<>();
        }
        return bullets;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
