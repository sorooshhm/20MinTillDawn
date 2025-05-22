package com.tillDawn.model.enums;

public enum FrMessages {
    SIGN_UP("S'inscrire"),
    USERNAME("Nom d'utilisateur"),
    PASSWORD("Mot de passe"),
    SECURITY_QUESTION("Question de sécurité"),
    QUESTION_1("Quel était le nom de votre premier animal de compagnie ?"),
    QUESTION_2("Dans quelle ville êtes-vous né ?"),
    QUESTION_3("Quel est le nom de jeune fille de votre mère ?"),
    SECURITY_ANSWER("Réponse de sécurité"),
    REMEMBER_ME("Se souvenir de moi"),
    LOGIN("Connexion"),
    GUEST("Invité"),
    SIGN_IN("Se connecter"),
    FORGOT_PASSWORD("Mot de passe oublié"),
    ERROR("Erreur"),
    OK("OK"),
    SUCCESS("Succès"),
    // setting menu
    SETTINGS("Paramètres"),
    PAUSE_MUSIC("Pause musique"),
    RESUME_MUSIC("Reprendre la musique"),
    SFX_ON("SFX activés"),
    SFX_OFF("SFX désactivés"),
    EDIT_BUTTONS("Modifier les boutons"),
    AUTO_RELOAD_ON("Rechargement auto activé"),
    AUTO_RELOAD_OFF("Rechargement auto désactivé"),
    BLACK_WHITE_ON("Noir/Blanc activé"),
    BLACK_WHITE_OFF("Noir/Blanc désactivé"),
    BACK("Retour"),
    MUSIC("Musique"),
    MUSIC_VOLUME("Volume de la musique"),
    // main menu
    PROFILE_MENU("Menu profil"),
    PRE_GAME_MENU("Menu pré-jeu"),
    SCORE_BOARD("Tableau des scores"),
    HINT_MENU("Menu d'aide"),
    LOAD_GAME("Charger une partie"),
    EXIT("Quitter"),
    // pre-game menu
    NAME("Nom"),
    SPEED("Vitesse"),
    HP("Points de vie"),
    NEXT("Suivant"),
    AMMO_MAX("Munitions max"),
    TIME_RELOAD("Temps de rechargement"),
    PROJECTILE("Projectile"),
    DAMAGE("Dégâts"),
    GUN("Arme"),
    AVATAR("Avatar"),
    GAME_TIME("Temps de jeu"),
    START_GAME("Commencer le jeu"),
    // profile menu
    PROFILE("PROFIL"),
    SCORE("Score"),
    KILLS("Victimes"),
    MOST_TIME_ALIVE("Temps de survie record"),
    EDIT("Modifier"),
    // edit profile menu
    SELECT_AVATAR("Sélectionner un avatar"),
    UPLOAD("Téléverser"),
    SUBMIT("Soumettre"),
    LOGOUT("Déconnexion"),
    // edit buttons menu
    RESET("Réinitialiser"),
    UP("HAUT"),
    DOWN("BAS"),
    LEFT("GAUCHE"),
    RIGHT("DROITE"),
    AUTO_AIM("VISÉE AUTO"),
    RELOAD("RECHARGER"),
    SHOOT("TIRER"),
    LEFT_CLICK("Clic gauche"),
    RIGHT_CLICK("Clic droit"),
    THE_KEY_IS_IN_USED("La touche est déjà utilisée"),
    // game menu
    XP("XP"),
    KILLS_UPPER("VICTIMES"),
    AMMO("MUNITIONS"),
    RELOAD_GUN("RECHARGEMENT DE L'ARME..."),
    LEVEL("Niveau"),
    CHOOSE_ABILITY("Choisir une capacité"),
    YOU_LOST("VOUS AVEZ PERDU !!"),
    YOU_WON("VOUS AVEZ GAGNÉ !!"),
    MAIN_MENU("Menu principal"),
    PLAY_AGAIN("Rejouer"),
    MISSION_REPORT("RAPPORT DE MISSION"),
    ENEMIES_DEFEATED("Ennemis vaincus"),
    SURVIVAL_TIME("Temps de survie"),
    TOTAL_SCORE("Score total"),
    CHEAT_CODES("Codes de triche"),
    DECREASE_GAME_TIME("Réduire le temps de jeu"),
    PLAYER_LEVEL_UP("Augmenter le niveau du joueur"),
    INCREASE_HP("Augmenter les points de vie"),
    GO_TO_BOSS_FIGHT("Aller au combat contre le boss"),
    INCREASE_PROJECTILE("Augmenter les projectiles"),
    PLAYER_ABILITIES("Capacités du joueur"),
    RESUME("Reprendre"),
    QUIT("Quitter"),
    GIVE_UP("Abandonner"),
    WHITE_BLACK("Blanc/Noir")
    ;

    private String text;
    FrMessages(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public static FrMessages findByKey(String key) {
        return FrMessages.valueOf(key);
    }
}
