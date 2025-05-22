package com.tillDawn.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.tillDawn.model.AvatarAssets;
import com.tillDawn.model.BaziAssets;
import com.tillDawn.model.Game.*;

import java.util.ArrayList;

public class GunController {
    private Texture gunTexture;
    private float originX, originY;
    private float rotation;
    private float x;
    private float y;
    private Player player;
    private Game game;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public GunController(Player player, Game game) {
        this.player = player;
        this.game = game;
        x = Gdx.graphics.getWidth() / 2f;
        y = Gdx.graphics.getHeight() / 2f;
        gunTexture = AvatarAssets.getGunTexture(player.getGunFilPath());
        originX = 0f;
        originY = 0f;
    }

    public void update(float delta) {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        rotation = 2 * (float) Math.PI - (float) Math.toDegrees(Math.atan2(
            mousePos.y - y,
            mousePos.x - x
        ));
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>(bullets);
        for (int i = 0; i < bullets.size(); i++) {
            if (isBulletOffScreen(bullets.get(i))) {
                if (i >= bullets.size())
                    bulletsToRemove.remove(i);
            }
        }
        bullets = bulletsToRemove;
    }

    public void render(SpriteBatch batch) {
        batch.draw(
            gunTexture,
            x - originX + 15f,
            y - originY + 30f,
            originX,
            originY,
            25f,
            25f,
            1,
            1,
            rotation,
            0,
            0,
            gunTexture.getWidth(),
            gunTexture.getHeight(),
            false,
            false
        );
        updateBullets(batch);
    }

    public void handleShot(int i1, int i2) {
        if (player.isReloading()) {
            return;
        }
        player.setGunAmmo(Math.max(player.getGunAmmo() - 1, 0));
        if (player.getGunAmmo() == 0) {
            if (player.getUser().isAutoReload()) {
                player.setReloading(true);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.setReloading(false);
                        player.setGunAmmo(player.getGun().getAmmoMax() + player.getMaxAmmoAddition());
                        this.cancel();
                    }
                }, player.getGun().getTimeReload(), 2);
            }
            return;
        }
        for (int i = 0; i < player.getGun().getProjectile() + player.getProjectileAddition(); i++) {
            bullets.add(new Bullet(i1, i2, x, y + i * 20f));
        }
    }


    public void updateBullets(SpriteBatch batch) {
        ArrayList<Integer> bulletsIndexToRemove = new ArrayList<>();
        for (Bullet b : bullets) {
            b.getSprite().draw(batch);

            b.getSprite().setX(b.getSprite().getX() - b.getDirection().x * 5);
            b.getSprite().setY(b.getSprite().getY() + b.getDirection().y * 5);
            b.getRectangle().setX(b.getSprite().getX() - b.getDirection().x * 5);
            b.getRectangle().setY(b.getSprite().getY() + b.getDirection().y * 5);

            ArrayList<Integer> tentacleIndexToRemove = new ArrayList<>();
            ArrayList<Integer> eyBatIndexToRemove = new ArrayList<>();
            for (Tentacle t : game.getTentacles()) {
                if (t.getRectangle().overlaps(b.getRectangle())) {
                    bulletsIndexToRemove.add(bullets.indexOf(b));
                    t.setHp(t.getHp() - b.getDamage() * player.getDamageMp());
                    Vector2 direction = b.getDirection();
                    t.setX(t.getX() - direction.x * 10);
                    t.setY(t.getY() + direction.y * 10);
                    t.getRectangle().setX(t.getRectangle().getX() - direction.x * 10);
                    t.getRectangle().setY(t.getRectangle().getY() + direction.y * 10);
                    Color shadowColor = new Color(1, 0, 0, 1);
                    t.setColor(shadowColor);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            t.setColor(null);
                            this.cancel();
                        }
                    } , .2f , 2);
                    if (t.getHp() <= 0) {
                        player.setKills(player.getKills() + 1);
                        game.getExplosions().add(new ExplosionAnimations(t.getX(), t.getY()));
                        tentacleIndexToRemove.add(game.getTentacles().indexOf(t));
                    }
                }
            }
            for (EyeBat t : game.getEyeBats()) {
                if (t.getRectangle().overlaps(b.getRectangle())) {
                    bulletsIndexToRemove.add(bullets.indexOf(b));
                    Vector2 direction = b.getDirection();
                    t.setHp(t.getHp() - b.getDamage());
                    t.setX(t.getX() - direction.x * 5);
                    t.setY(t.getY() + direction.y * 5);
                    t.getRectangle().setX(t.getRectangle().getX() - direction.x * 10);
                    t.getRectangle().setY(t.getRectangle().getY() + direction.y * 10);
                    Color shadowColor = new Color(1, 0, 0, 0.5f);
                    t.setColor(shadowColor);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            t.setColor(null);
                            this.cancel();
                        }
                    } , .2f , 2);
                    if (t.getHp() <= 0) {
                        player.setKills(player.getKills() + 1);
                        game.getExplosions().add(new ExplosionAnimations(
                            t.getX(), t.getY()));
                        eyBatIndexToRemove.add(game.getEyeBats().indexOf(t));
                    }
                }
            }
            if (game.getHasturBoss() != null) {
                HasturBoss hasturBoss = game.getHasturBoss();
                if (hasturBoss.getRectangle().overlaps(b.getRectangle())) {
                    bulletsIndexToRemove.add(bullets.indexOf(b));
                    hasturBoss.setHp(hasturBoss.getHp() - b.getDamage() * player.getDamageMp());
                    Vector2 direction = b.getDirection();
                    hasturBoss.setHp(hasturBoss.getHp() - b.getDamage());
                    hasturBoss.setX(b.getX() - direction.x * 5);
                    hasturBoss.setY(b.getY() + direction.y * 5);
                    hasturBoss.getRectangle().setX(hasturBoss.getRectangle().getX() - direction.x * 10);
                    hasturBoss.getRectangle().setY(hasturBoss.getRectangle().getY() + direction.y * 10);
                    if (hasturBoss.getHp() <= 0) {
                        game.setHasturBoss(null);
                    }
                }
            }
            for (Integer i : tentacleIndexToRemove) {
                try {
                    Tentacle t = game.getTentacles().get(i);
                    XP xp = new XP(t.getX(), t.getY());
                    game.getXps().add(xp);
                    game.getTentacles().remove(i.intValue());
                } catch (Exception e) {
                }
            }
            for (Integer i : eyBatIndexToRemove) {
                try {
                    EyeBat e = game.getEyeBats().get(i);
                    XP xp = new XP(e.getX(), e.getY());
                    game.getXps().add(xp);
                    game.getEyeBats().remove(i.intValue());
                } catch (Exception e) {
                }
            }
        }
        for (Integer i : bulletsIndexToRemove) {
            try {
                bullets.remove(i.intValue());
            } catch (IndexOutOfBoundsException e) {

            }
        }
    }

    private boolean isBulletOffScreen(Bullet bullet) {
        float x = bullet.getSprite().getX();
        float y = bullet.getSprite().getY();
        float width = bullet.getSprite().getWidth();
        float height = bullet.getSprite().getHeight();

        return x + width < 0 ||
            x > Gdx.graphics.getWidth() ||
            y + height < 0 ||
            y > Gdx.graphics.getHeight();
    }


    public void dispose() {
        gunTexture.dispose();
    }
}
