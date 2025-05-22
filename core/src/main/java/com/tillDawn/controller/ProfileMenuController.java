package com.tillDawn.controller;

import com.tillDawn.model.App;
import com.tillDawn.model.IO.Request;
import com.tillDawn.model.IO.Response;
import com.tillDawn.model.User;
import com.tillDawn.repositories.UserRepository;
import com.tillDawn.validators.SignInValidator;
import com.tillDawn.validators.Validator;

public class ProfileMenuController {
    public Response editProfile(Request req) {
        String username = req.body.get("username");
        String password = req.body.get("password");
        String avatar = req.body.get("avatar");
        User user = App.getCurrUser();

        user.setUsername(username);
        if(!Validator.validatePassword(password)) {
            return new Response(false , "Password is invalid.");
        }
        user.setPassword(password);
        user.setAvatar(avatar);

        App.setCurrUser(user);
        UserRepository.save(user);
        return new Response(true, "Profile updated successfully");
    }

    public Response logout(Request req) {
        App.setCurrUser(null);
        UserRepository.removeStayLoggedInUser();
        return new Response(true, "Logged out successfully");
    }
}
