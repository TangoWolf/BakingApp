package com.example.shaol.bakingapp.Models;

/**
 * Created by shaol on 5/17/2018.
 */

public class Details {

    private Ingredients[] ingredients;
    private Steps[] steps;

    public Details(Ingredients[] ingredients, Steps[] steps) {
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Ingredients[] getIngredients() {
        return ingredients;
    }

    public Steps[] getSteps() {
        return steps;
    }
}
