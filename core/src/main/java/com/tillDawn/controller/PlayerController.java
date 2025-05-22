package com.tillDawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.tillDawn.model.App;
import com.tillDawn.model.BaziAssets;
import com.tillDawn.model.Game.*;
import com.tillDawn.view.GameMenu;

import java.util.TimerTask;

public class PlayerController {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float x, y;
    private float width, height;
    private Player player;
    private Game game;
    private Rectangle playerRectangle;
    private GunController gunController;
    private GameMenu gameMenu;
    private boolean isTreeDamaging = false;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getPlayerRectangle() {
        return playerRectangle;
    }

    public PlayerController(Player player, Game game, GameMenu gameMenu) {
        this.player = player;
        this.game = game;
        animation = player.getAnimation();
        x = Gdx.graphics.getWidth()/2f;
        y = Gdx.graphics.getHeight()/2f;
        width = player.getWidth();
        height = player.getHeight();
        playerRectangle = player.getRectangle();
        gunController = new GunController(player, game);
        stateTime = 0f;
        this.gameMenu = gameMenu;
    }

    public GunController getGunController() {
        return this.gunController;
    }

    public void update(float delta) {
        stateTime += delta;


        gameMenu.getCircleShader().setUniformf("u_lightPos", x, y);

        gameMenu.getCircleShader().setUniformf("u_radius", 250f);
        gunController.update(delta);
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, width, height);
        gunController.render(batch);
    }

    public void handlePlayerWalk() {
        float mp = 1.5f;
        float newX = player.getCoordinate().getX();
        float newY = player.getCoordinate().getY();

        boolean canMove = true;
        if (Gdx.input.isKeyPressed(player.getUser().getButtons()[0])) {
            float y = player.getCoordinate().getY();
            newY = (y - player.getSpeed());
            canMove = handleCollisionWithTree(newX, newY);
        }
        if (Gdx.input.isKeyPressed(player.getUser().getButtons()[1])) {
            float y = player.getCoordinate().getY();
            newY = (y + player.getSpeed());
            canMove = handleCollisionWithTree(newX, newY);
        }
        if (Gdx.input.isKeyPressed(player.getUser().getButtons()[3])) {
            float x = player.getCoordinate().getX();
            newX = (x - player.getSpeed() * mp);
            canMove = handleCollisionWithTree(newX, newY);
        }
        if (Gdx.input.isKeyPressed(player.getUser().getButtons()[2])) {
            float x = player.getCoordinate().getX();
            newX = (x + player.getSpeed() * mp);
            canMove = handleCollisionWithTree(newX, newY);
        }
        if (Gdx.input.isKeyJustPressed(player.getUser().getButtons()[4])) {
            autoAim();
        }
        if(Gdx.input.isKeyJustPressed(player.getUser().getButtons()[5])) {
            player.setReloading(true);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    player.setReloading(false);
                    player.setGunAmmo(player.getGun().getAmmoMax() + player.getMaxAmmoAddition());
                    this.cancel();
                }
            } , player.getGun().getTimeReload() , 2);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameMenu.setPaused(true);
            return;
        }
        if(player.getUser().getButtons()[6] != Input.Buttons.RIGHT && player.getUser().getButtons()[6] != Input.Buttons.LEFT) {
            if(Gdx.input.isKeyJustPressed(player.getUser().getButtons()[6])){
                gunController.handleShot(Gdx.input.getX(), Gdx.input.getY());
                return;
            }
        }
        if (canMove) {
            isTreeDamaging = false;
            player.getCoordinate().setX(newX);
            player.getCoordinate().setY(newY);
        }
    }

    public void autoAim() {
        Tentacle target = null;
        double distance = Double.POSITIVE_INFINITY;
        for (Tentacle t : game.getTentacles()) {
            double d = (player.getCoordinate().getX() + t.getX() - x) * (player.getCoordinate().getX() + t.getX() - x)
                + (player.getCoordinate().getY() + t.getY() - y) * (player.getCoordinate().getY() + t.getY() - y);
            if (d < distance) {
                distance = d;
                target = t;
            }
        }
        EyeBat eye = null;
        for (EyeBat t : game.getEyeBats()) {
            double d = (player.getCoordinate().getX() + t.getX() - x) * (player.getCoordinate().getX() + t.getX() - x)
                + (player.getCoordinate().getY() + t.getY() - y) * (player.getCoordinate().getY() + t.getY() - y);
            if (d < distance) {
                distance = d;
                target = null;
                eye = t;
            }
        }
        HasturBoss h = null;
        if (game.getHasturBoss() != null) {
            HasturBoss boss = game.getHasturBoss();
            double d = (player.getCoordinate().getX() + boss.getX() - x) * (player.getCoordinate().getX() + boss.getX() - x)
                + (player.getCoordinate().getY() + boss.getY() - y) * (player.getCoordinate().getY() + boss.getY() - y);
            if (d < distance) {
                distance = d;
                h = boss;
                target = null;
                eye = null;
            }
        }
        if (target != null) {
            Gdx.input.setCursorPosition((int) (player.getCoordinate().getX() + target.getX()),
                Gdx.graphics.getHeight() - (int) (player.getCoordinate().getY() + target.getY()));
            gunController.handleShot((int) (player.getCoordinate().getX() + target.getX()),
                Gdx.graphics.getHeight() - (int) (player.getCoordinate().getY() + target.getY()));
        }
        if (eye != null) {
            Gdx.input.setCursorPosition((int) (player.getCoordinate().getX() + eye.getX()),
                Gdx.graphics.getHeight() - (int) (player.getCoordinate().getY() + eye.getY()));
            gunController.handleShot((int) (player.getCoordinate().getX() + eye.getX()),
                Gdx.graphics.getHeight() - (int) (player.getCoordinate().getY() + eye.getY()));
        }
        if (h != null) {
            Gdx.input.setCursorPosition((int) (player.getCoordinate().getX() + h.getX()),
                Gdx.graphics.getHeight() - (int) (player.getCoordinate().getY() + h.getY()));
            gunController.handleShot((int) (player.getCoordinate().getX() + h.getX()),
                Gdx.graphics.getHeight() - (int) (player.getCoordinate().getY() + h.getY()));
        }
    }

    private boolean handleCollisionWithTree(float x, float y) {
        boolean collision = false;
        for (Tree tree : game.getTrees()) {
            Rectangle r = new Rectangle(x + tree.getX(), y + tree.getY(), tree.getWidth(), tree.getHeight());
            if (r.overlaps(playerRectangle)) {
                if (!isTreeDamaging) {
                    isTreeDamaging = true;
                    player.setHp(player.getHp() - tree.getDamage());
                }
                collision = true;
                break;
            }
        }
        return true;
    }

    private void handleOutOfBounds() {
        if (x < player.getCoordinate().getX()) {
            player.getCoordinate().setX(x);
        }
        if (x > player.getCoordinate().getX() + gameMenu.getBackgroundWidth() - width) {
            player.getCoordinate().setX(x - gameMenu.getBackgroundWidth() + width);
        }
        if (y < player.getCoordinate().getY()) {
            player.getCoordinate().setY(y);
        }
        if (y > player.getCoordinate().getY() + gameMenu.getBackgroundHeight() - height) {
            player.getCoordinate().setY(y - gameMenu.getBackgroundHeight() + height);
        }
    }

    public boolean isPlayerXOutOfBounds() {
        return x < player.getCoordinate().getX() || x > player.getCoordinate().getX() + gameMenu.getBackgroundWidth();
    }

    public boolean isPlayerYOutOfBounds() {
        return y < player.getCoordinate().getY() || y > player.getCoordinate().getY() + gameMenu.getBackgroundHeight();
    }
}
