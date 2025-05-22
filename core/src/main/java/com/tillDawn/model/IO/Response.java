package com.tillDawn.model.IO;

import java.util.HashMap;

public class Response {
    public boolean success;
    public final String message;
    public final HashMap<String,String> body = new HashMap<>();
    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
