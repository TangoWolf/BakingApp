package com.example.shaol.bakingapp.Models;

import android.support.annotation.Nullable;

/**
 * Created by shaol on 5/17/2018.
 */

public class Recipe {

    private int id;
    private String name;
    private int servings;
    private String image;

    public Recipe(int id, String name, int servings, @Nullable String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
