package com.tillDawn.model.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tillDawn.model.BaziAssets;
import com.tillDawn.model.GameAssets;
import com.tillDawn.model.User;
import com.tillDawn.model.enums.Guns;
import com.tillDawn.model.enums.Heroes;
import com.tillDawn.repositories.UserRepository;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Transient;
import org.bson.types.ObjectId;

import java.util.HashSet;

@Embedded
public class Player {
    private final ObjectId _id = new ObjectId();
    private Object userId;
    private Coordinate coordinate;
    @Transient
    private transient User user = null;
    private Heroes hero;
    private String heroAvatar;
    private String gunFilPath;
    private Guns gun;
    private int gunAmmo;
    private int level = 1;
    private float hp;
    private int xp;
    private int kills = 0;
    private boolean isHurt = false;
    private float isHurtTime = 0;
    @Transient
    private boolean levelUp = false;
    @Transient
    private float damageMp = 1f;
    private float ammo;
    private int projectileAddition = 0;
    private int maxAmmoAddition = 0;
    @Transient
    private float speedMp = 1f;
    private HashSet<Integer> abilities = new HashSet<Integer>();
    @Transient
    private Animation<TextureRegion> animation;
    @Transient
    private Rectangle rectangle;
    @Transient
    private boolean isReloading = false;
    private float width;
    private float height;

    public Player() {

    }

    public void setUser(User user) {
        this.user = user;
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

    public Player(Object userId, Heroes hero, String avatar, float hp, int xp) {
        this.userId = userId;
        this.hero = hero;
        this.hp = hp;
        this.xp = xp;
        this.heroAvatar = avatar;
        this.animation = BaziAssets.initialAnimation(avatar);
        this.width = animation.getKeyFrame(0).getRegionWidth()*3;
        this.height = animation.getKeyFrame(0).getRegionHeight()*3;
        this.rectangle = new Rectangle(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, width, height);
    }

    public Animation<TextureRegion> getAnimation() {
        if (animation == null) {
            animation = BaziAssets.initialAnimation(this.heroAvatar);
            width = animation.getKeyFrame(0).getRegionWidth()*3;
            height = animation.getKeyFrame(0).getRegionHeight()*3;
        }
        return animation;
    }

    public Rectangle getRectangle() {
        if (rectangle == null) {
            rectangle = new Rectangle(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, width, height);
        }
        return rectangle;
    }

    public ObjectId get_id() {
        return _id;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Heroes getHero() {
        return hero;
    }

    public void setHero(Heroes hero) {
        this.hero = hero;
    }

    public Guns getGun() {
        return gun;
    }

    public void setGun(Guns gun) {
        this.gun = gun;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        if (hp < this.hp && isHurt) {
            return;
        }
        isHurt = true;
        if(hp > this.getHero().getHp()){
            hp = this.getHero().getHp();
        }
        this.hp = Math.max(hp, 0);
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        if (xp > level * 20) {
            level++;
            levelUp = true;
            xp = xp - (level - 1) * 20;
        }
        this.xp = xp;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }

    public User getUser() {
        if (user == null) {
            user = UserRepository.findUserById(userId.toString());
        }
        return user;
    }

    public int getSpeed() {
        return this.hero.getSpeed() * (int) speedMp;
    }

    public int getMaxHp() {
        return this.hero.getHp();
    }

    public String getHeroName() {
        return this.hero.getName();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getGunFilPath() {
        return gunFilPath;
    }

    public void setGunFilPath(String gunFilPath) {
        this.gunFilPath = gunFilPath;
    }

    public int getGunAmmo() {
        return gunAmmo;
    }

    public void setGunAmmo(int gunAmmo) {
        this.gunAmmo = gunAmmo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHurt() {
        return isHurt;
    }

    public void setHurt(boolean hurt) {
        isHurt = hurt;
    }

    public float getIsHurtTime() {
        return isHurtTime;
    }

    public void setIsHurtTime(float isHurtTime) {
        this.isHurtTime = isHurtTime;
    }

    public boolean isLevelUp() {
        return levelUp;
    }

    public void setLevelUp(boolean levelUp) {
        this.levelUp = levelUp;
    }

    public float getDamageMp() {
        return damageMp;
    }

    public void setDamageMp(float damageMp) {
        this.damageMp = damageMp;
    }

    public int getProjectileAddition() {
        return projectileAddition;
    }

    public void setProjectileAddition(int projectileAddition) {
        this.projectileAddition = projectileAddition;
    }

    public int getMaxAmmoAddition() {
        return maxAmmoAddition;
    }

    public void setMaxAmmoAddition(int maxAmmoAddition) {
        this.maxAmmoAddition = maxAmmoAddition;
    }

    public float getSpeedMp() {
        return speedMp;
    }

    public void setSpeedMp(float speedMp) {
        this.speedMp = speedMp;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public HashSet<Integer> getAbilities() {
        return abilities;
    }

    public float getAmmo() {
        return ammo;
    }

    public void setAmmo(float ammo) {
        this.ammo = ammo;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public void setReloading(boolean reloading) {
        isReloading = reloading;
    }
}
