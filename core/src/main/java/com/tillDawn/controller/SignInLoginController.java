package com.tillDawn.controller;

import com.tillDawn.model.App;
import com.tillDawn.model.IO.Request;
import com.tillDawn.model.IO.Response;
import com.tillDawn.model.User;
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.validators.SignInValidator;

public class SignInLoginController {
    public Response signIn(Request req) {
        Response validation = SignInValidator.signIn(req);
        if (!validation.success) {
            return validation;
        }
        String username = req.body.get("username");
        String password = req.body.get("password");
        String securityQuestion = req.body.get("securityQuestion");
        String securityAnswer = req.body.get("securityAnswer");
        String rememberMe = req.body.get("rememberMe");

        if (UserRepository.userExists(username)) {
            return new Response(false, "This username is already in use");
        }
        User user = new User(username, password, securityQuestion, securityAnswer);
        int random = (int) (Math.random() * 4) + 1;
        String avatar = "images/avatars/" + random + ".jpg";
        user.setAvatar(avatar);

        UserRepository.save(user);
        App.setCurrUser(user);
        if (rememberMe.equals("true")) {
            UserRepository.setUserLoggedIn(user);
        }
        return new Response(true, username + " registered in successfully");
    }

    public Response login(Request req) {
        Response validation = SignInValidator.signIn(req);
        if (!validation.success) {
            return validation;
        }
        String username = req.body.get("username");
        String password = req.body.get("password");
        String rememberMe = req.body.get("rememberMe");

        User user = UserRepository.findUserByUsername(username);
        if (user == null) {
            return new Response(false, "User not found");
        }
        if (!password.equals(user.getPassword())) {
            return new Response(false, "Wrong password");
        }
        App.setCurrUser(user);
        if (rememberMe.equals("true")) {
            UserRepository.setUserLoggedIn(user);
        }
        return new Response(true, "Successfully logged in");

    }

    public Response changePassword(Request req) {
        Response validation = SignInValidator.changePassword(req);
        if (!validation.success) {
            return validation;
        }
        String username = req.body.get("username");
        String password = req.body.get("password");
        User user = UserRepository.findUserByUsername(username);
        if (user == null) {
            return new Response(false, "User not found");
        }
        user.setPassword(password);
        UserRepository.save(user);
        App.setCurrUser(user);
        return new Response(true, "Successfully changed password");
    }
}
