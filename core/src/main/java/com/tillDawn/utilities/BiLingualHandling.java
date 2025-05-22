package com.tillDawn.utilities;

import com.tillDawn.model.enums.EnMessages;
import com.tillDawn.model.enums.FrMessages;

public class BiLingualHandling {
    private static BiLingualHandling handling;
    private String language;

    private BiLingualHandling(String language) {
        this.language = language;
    }

    public static BiLingualHandling getInstance() {
        if (handling == null) {
            handling = new BiLingualHandling("EN");
        }
        return handling;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMessage(String key){
        if(language.equals("EN")){
            return EnMessages.findByKey(key).getText();
        }else if(language.equals("FR")){
            return FrMessages.findByKey(key).getText();
        }
        return null;
    }

}
