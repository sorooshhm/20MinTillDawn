package com.tillDawn.repositories;

import com.tillDawn.model.Game.Game;
import com.tillDawn.model.User;
import com.tillDawn.utilities.DBConnection;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    private static Datastore db = DBConnection.getDatabase();

    public static User findUserById(ObjectId id) {
        User user = db.find(User.class).filter("_id", id).first();
        return user;
    }

    public static ArrayList<User> findAllUsers() {
        ArrayList<User> users = new ArrayList<>(db.find(User.class).iterator().toList());
        return users;
    }

    public static User findUserById(String id) {
        User user = db.find(User.class).filter("_id", new ObjectId(id)).first();
        return user;
    }

    public static User findUserByUsername(String username) {
        User user = db.find(User.class).filter("username", username).first();
        return user;
    }

    public static User getLoggedInUser() {
        String user_id = System.getProperty("USER_ID");
        if (user_id == null) return null;
        User user = findUserById(user_id);
        return user;
    }

    public static void removeStayLoggedInUser() {
        String envFilePath = "D:/tamrin-ha/ap/tamrin-3/core/src/main/java/com/tillDawn/.env";
        String variableToRemove = "USER_ID";

        try {
            List<String> lines = Files.readAllLines(Paths.get(envFilePath));

            List<String> updatedLines = lines.stream()
                .filter(line -> !line.startsWith(variableToRemove + "="))
                .collect(Collectors.toList());

            Files.write(Paths.get(envFilePath), updatedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            System.out.println("Error updating .env file: " + e.getMessage());
        }
    }

    public static void setUserLoggedIn(User user) {
        String envFilePath = "D:/tamrin-ha/ap/tamrin-3/core/src/main/java/com/tillDawn/.env";
        String envVar = "\nUSER_ID=" + user.get_id().toString();

        try {
            Files.write(Path.of(envFilePath), envVar.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error updating .env file: " + e.getMessage());
        }
    }

    public static void save(User user) {
        db.save(user);
    }

    public static  boolean userExists(String username) {
        return db.find(User.class)
            .filter(Filters.eq("username", username))
            .first(new FindOptions()
                .projection().include("_id")) != null;
    }


}
