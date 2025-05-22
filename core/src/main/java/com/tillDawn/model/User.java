package com.tillDawn.model;

import com.tillDawn.model.Game.Game;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

@Entity("users")
public class User {
    @Id
    private ObjectId _id;
    private String username;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private String avatar;
    @Reference(lazy = true, idOnly = true, ignoreMissing = true)
    private Game currentGame;
    private int score;
    private int[] buttons = {Buttons.UP.getCode(), Buttons.DOWN.getCode(), Buttons.LEFT.getCode(), Buttons.RIGHT.getCode(),
        Buttons.AutoAim.getCode(), Buttons.ReloadGun.getCode(), Buttons.Shoot.getCode()};
    private boolean autoReload = true;
    private boolean grayShadow = false;
    private int kills = 0;
    private float mostTimeAlive = 0;
    private boolean isGuest = false;

    public User() {

    }

    public int[] getButtons() {
        return buttons;
    }

    public void setButtons(int[] buttons) {
        this.buttons = buttons;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public User(String username, String password, String securityQuestion, String securityAnswer) {
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int findButtonWithCode(int code) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == code) {
                return i;
            }
        }
        return -1;
    }
    public String getFormattedTime() {
        int minutes = (int)(mostTimeAlive / 60);
        int seconds = (int)(mostTimeAlive % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public boolean isGrayShadow() {
        return grayShadow;
    }

    public void setGrayShadow(boolean grayShadow) {
        this.grayShadow = grayShadow;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public float getMostTimeAlive() {
        return mostTimeAlive;
    }

    public void setMostTimeAlive(float mostTimeAlive) {
        this.mostTimeAlive = mostTimeAlive;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }
}
