package finalproject.homie.controllers;

import android.app.Application;

import finalproject.homie.model.Model;

/**
 * Created by I311044 on 09/03/2017.
 */

public class BaseApplication extends Application {
    private Model model;
    private String token = "";

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.model = new Model();
    }
}
