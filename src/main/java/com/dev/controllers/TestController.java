package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.MessageObject;
import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


@RestController
public class TestController {


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {persist.createConnectionToDatabase();}

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        return persist.signIn(username,password);

    }

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        if (!persist.doesUserExist(username)) {
            UserObject userObject = new UserObject();
            userObject.setUsername(username);
            userObject.setPassword(password);
            String hash = Utils.createHash(username, password);
            userObject.setToken(hash);
            success = persist.addAccount(userObject);
        }

        return success;
    }
    @RequestMapping("doesUserExist")
    public boolean doesUserExist(String username){
        return persist.doesUserExist(username);
    }


    @RequestMapping("add-message")
    public boolean addMessage (String token,String receiverUsername, String title, String content) {
        return persist.addMessage(token,receiverUsername,title,content);
    }


    @RequestMapping("get-messages")
    public List<MessageObject> getMessage (String token) {
        return persist.getMessagesByUser(token);
    }

    @RequestMapping("remove-message")
    public boolean removeMessage (int messageId) {
        return persist.removeMessage(messageId);
    }

    @RequestMapping("read-message")
    public boolean readMessage (int messageId) {return persist.readMessage(messageId);}









}
