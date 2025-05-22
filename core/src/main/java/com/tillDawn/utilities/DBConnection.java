package com.tillDawn.utilities;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class DBConnection {
    private static Datastore database;

    public static Datastore getDatabase() {
        if (database == null) {
            try {
                String DB = "20MinTillDawn";
                String DB_URI = "mongodb://localhost:27017";

                database = Morphia.createDatastore(MongoClients.create(DB_URI), DB);
                database.getMapper().mapPackage("com.tillDawn.models");
                database.ensureIndexes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return database;
    }
}
