package com.tillDawn.model.enums;

public enum EnMessages {
    // signing menu
    SIGN_UP("Sign Up"),
    USERNAME("UserName"),
    PASSWORD("Password"),
    SECURITY_QUESTION("Security Question"),
    QUESTION_1("What was your first pet's name?"),
    QUESTION_2("What city were you born in?"),
    QUESTION_3("What is your mother's maiden name?"),
    SECURITY_ANSWER("Security Answer"),
    REMEMBER_ME("Remember Me"),
    LOGIN("Login"),
    GUEST("Guest"),
    SIGN_IN("Sign In"),
    FORGOT_PASSWORD("Forgot Password"),
    ERROR("Error"),
    OK("OK"),
    SUCCESS("Success"),
    // setting menu
    SETTINGS("Settings"),
    PAUSE_MUSIC("Pause Music"),
    RESUME_MUSIC("Resume Music"),
    SFX_ON("SFX On"),
    SFX_OFF("SFX Off"),
    EDIT_BUTTONS("Edit Buttons"),
    AUTO_RELOAD_ON("Auto Reload On"),
    AUTO_RELOAD_OFF("Auto Reload Off"),
    BLACK_WHITE_ON("Black/White On"),
    BLACK_WHITE_OFF("Black/White Off"),
    BACK("Back"),
    MUSIC("Music"),
    MUSIC_VOLUME("Music Volume"),
    // main menu
    PROFILE_MENU("Profile Menu"),
    PRE_GAME_MENU("Pre-Game Menu"),
    SCORE_BOARD("Score Board"),
    HINT_MENU("Hint Menu"),
    LOAD_GAME("Load Game"),
    EXIT("Exit"),
    // pre-game menu
    NAME("Name"),
    SPEED("Speed"),
    HP("Hp"),
    NEXT("Next"),
    AMMO_MAX("Ammo Max"),
    TIME_RELOAD("Time Reload"),
    PROJECTILE("Projectile"),
    DAMAGE("Damage"),
    GUN("Gun"),
    AVATAR("Avatar"),
    GAME_TIME("Game Time"),
    START_GAME("Start Game"),
    // profile menu
    PROFILE("PROFILE"),
    SCORE("Score"),
    KILLS("Kills"),
    MOST_TIME_ALIVE("Most Time Alive"),
    EDIT("Edit"),
    // edit profile menu
    SELECT_AVATAR("Select Avatar"),
    UPLOAD("Upload"),
    SUBMIT("Submit"),
    LOGOUT("Logout"),
    // edit buttons menu
    RESET("Reset"),
    UP("UP"),
    DOWN("DOWN"),
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    AUTO_AIM("AUTO AIM"),
    RELOAD("RELOAD"),
    SHOOT("SHOOT"),
    LEFT_CLICK("Left Click"),
    RIGHT_CLICK("Right Click"),
    THE_KEY_IS_IN_USED("The Key Is In Used"),
    // game menu
    XP("XP"),
    KILLS_UPPER("KILLS"),
    AMMO("AMMO"),
    RELOAD_GUN("RELOADING GUN ..."),
    LEVEL("Level"),
    CHOOSE_ABILITY("Choose Ability"),
    YOU_LOST("YOU LOST !!"),
    YOU_WON("YOU WON !!"),
    MAIN_MENU("Main Menu"),
    PLAY_AGAIN("PLay Again"),
    MISSION_REPORT("MISSION REPORT"),
    ENEMIES_DEFEATED("Enemies Defeated"),
    SURVIVAL_TIME("Survival Time"),
    TOTAL_SCORE("Total Score"),
    CHEAT_CODES("Cheat Codes"),
    DECREASE_GAME_TIME("Decrease game time"),
    PLAYER_LEVEL_UP("Player level up"),
    INCREASE_HP("Increase Hp"),
    GO_TO_BOSS_FIGHT("Go to boss fight"),
    INCREASE_PROJECTILE("Increase projectile"),
    PLAYER_ABILITIES("Player Abilities"),
    RESUME("Resume"),
    QUIT("Quit"),
    GIVE_UP("Give Up"),
    WHITE_BLACK("White/Black"),

    ;

    private String text;

    EnMessages(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static EnMessages findByKey(String key) {
        return EnMessages.valueOf(key);
    }
}
