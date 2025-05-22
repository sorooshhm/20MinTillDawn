package com.tillDawn.validators;

import com.tillDawn.model.IO.Request;
import com.tillDawn.model.IO.Response;

public class SignInValidator {
    public static Response signIn(Request req) {
        if(!Validator.validateUsername(req.body.get("username"))){
            return new Response(false, "username format is not valid");
        }
        if(!Validator.validatePassword(req.body.get("password"))){
            return new Response(false, "password format is not valid");
        }
        return new Response(true, "OK");
    }

    public static Response changePassword(Request req) {
        if(!Validator.validatePassword(req.body.get("password"))){
            return new Response(false, "password format is not valid");
        }
        if(!req.body.get("confirmPassword").equals(req.body.get("password"))){
            return new Response(false, "confirmPassword format is not valid");
        }
        return new Response(true, "OK");
    }
}
