package com.tillDawn.model.Game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Transient;
import org.bson.types.ObjectId;

import java.util.ArrayList;

@Entity("games")
public class Game {
    @Id
    private ObjectId _id;
    private Player player;
    private ArrayList<Tree> trees = new ArrayList<>();
    private ArrayList<Tentacle> tentacles = new ArrayList<>();
    private ArrayList<EyeBat> eyeBats = new ArrayList<>();
    @Transient
    private ArrayList<XP> xps = new ArrayList<>();
    @Transient
    private ArrayList<ExplosionAnimations> explosions = new ArrayList<>();
    private float backWidth;
    private float backHeight;
    private float gamePlay = 0;
    private float gameMaxTime = 2.5f * 60;
    private HasturBoss hasturBoss = null;
    private boolean isFinished = false;

    public Game() {

    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public void initializeTrees(float xBound, float yBound, int count) {
        for (int i = 0; i < count; i++) {
            float xPos = (float) (Math.random() * (xBound * 3));
            float yPos = (float) (Math.random() * (yBound * 3));
            Tree tree = new Tree(xPos, yPos);
            trees.add(tree);
        }
    }

    public Game(Player player) {
        this.player = player;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void updateStateTime(float delta) {
        for (Tree tree : trees) {
            tree.setStateTime(tree.getStateTime() + delta);
        }
        for (Tentacle tentacle : tentacles) {
            tentacle.setStateTime(tentacle.getStateTime() + delta);
        }
        for (EyeBat eyeBat : eyeBats) {
            eyeBat.setStateTime(eyeBat.getStateTime() + delta);
        }
        for (ExplosionAnimations explosionAnimation : explosions) {
            explosionAnimation.setStateTime(explosionAnimation.getStateTime() + delta);
        }
        if (hasturBoss != null) {
            hasturBoss.setStateTime(hasturBoss.getStateTime() + delta);
        }
    }

    public float getBackWidth() {
        return backWidth;
    }

    public void setBackWidth(float backWidth) {
        this.backWidth = backWidth;
    }

    public float getBackHeight() {
        return backHeight;
    }

    public void setBackHeight(float backHeight) {
        this.backHeight = backHeight;
    }

    public float getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(float gamePlay) {
        this.gamePlay = gamePlay;
    }

    public float addGamePlay(float delta) {
        gamePlay += delta;
        return gamePlay;
    }

    public ArrayList<Tentacle> getTentacles() {
        return tentacles;
    }

    public float getGameMaxTime() {
        return gameMaxTime;
    }

    public ArrayList<EyeBat> getEyeBats() {
        return eyeBats;
    }

    public ArrayList<XP> getXps() {
        return xps;
    }

    public ArrayList<ExplosionAnimations> getExplosions() {
        return explosions;
    }

    public HasturBoss getHasturBoss() {
        return hasturBoss;
    }

    public void setHasturBoss(HasturBoss hasturBoss) {
        this.hasturBoss = hasturBoss;
    }

    public void setGameMaxTime(float gameMaxTime) {
        this.gameMaxTime = gameMaxTime * 60;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
