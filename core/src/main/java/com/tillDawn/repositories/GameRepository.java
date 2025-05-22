package com.tillDawn.repositories;

import com.tillDawn.model.Game.Game;
import com.tillDawn.utilities.DBConnection;
import dev.morphia.Datastore;

import java.util.ArrayList;

public class GameRepository {
    private static Datastore db = DBConnection.getDatabase();

    public static Game findGameById(String id){
        Game game  = db.find(Game.class).filter("_id"   , id ).first();
        return game;
    }

    public static ArrayList<Game> findAllGames(){
        ArrayList<Game> games = new ArrayList<>(db.find(Game.class).iterator().toList());
        return games;
    }

    public static void save(Game game){
        db.save(game);
    }

    public static void delete(Game game){
        db.delete(game);
    }
}
