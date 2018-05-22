package com.example.shaol.bakingapp.Utils;

import android.net.Uri;
import android.util.Log;

import com.example.shaol.bakingapp.Models.Ingredients;
import com.example.shaol.bakingapp.Models.Recipe;
import com.example.shaol.bakingapp.Models.Details;
import com.example.shaol.bakingapp.Models.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shaol on 5/17/2018.
 */

public final class JsonUtils {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String STEPS = "steps";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "poster_path";

    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";

    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEOURL = "videoURL";
    private static final String THUMBNAILURL = "thumbnailURL";

    public static Recipe[] getBasicInfoFromJson(String JsonString) throws JSONException {
        Recipe[] parsedRecipeInfo = null;

        JSONArray startString = new JSONArray(JsonString);

        parsedRecipeInfo = new Recipe[startString.length()];

        for (int i = 0; i < startString.length(); i++) {
            JSONObject recipe = startString.getJSONObject(i);

            int id = recipe.getInt(ID);
            String name = recipe.getString(NAME);
            int servings = recipe.getInt(SERVINGS);
            String image = "waltz";
            if (recipe.has(IMAGE)) {
                image = recipe.getString(IMAGE);
            }

            Recipe aRecipe = new Recipe(id, name, servings, image);
            parsedRecipeInfo[i] = aRecipe;
        }
        return parsedRecipeInfo;
    }

    public static Details getStepsInfoFromJson(String JsonString, int recipeId) throws JSONException {
        Details parsedDetailsInfo = null;
        Ingredients[] parsedIngredientInfo = null;
        Steps[] parsedStepsInfo = null;

        JSONArray startString = new JSONArray(JsonString);

        for (int i = 0; i < startString.length(); i++) {
            JSONObject recipe = startString.getJSONObject(i);

            int idCheck = recipe.getInt(ID);

            if (idCheck == recipeId) {
                JSONArray ingredients = recipe.getJSONArray(INGREDIENTS);
                parsedIngredientInfo = new Ingredients[ingredients.length()];

                for (int j = 0; j < ingredients.length(); j++) {
                    JSONObject ingredientObject = ingredients.getJSONObject(j);

                    double mQuantity = ingredientObject.getDouble(QUANTITY);
                    String quantity = "" + mQuantity;
                    String measure = ingredientObject.getString(MEASURE);
                    String ingredient = ingredientObject.getString(INGREDIENT);

                    Ingredients aIngredient = new Ingredients(quantity, measure, ingredient);
                    parsedIngredientInfo[j] = aIngredient;
                }

                JSONArray steps = recipe.getJSONArray(STEPS);
                parsedStepsInfo = new Steps[steps.length()];

                for (int k = 0; k < steps.length(); k++) {
                    JSONObject stepObject = steps.getJSONObject(k);

                    int id = stepObject.getInt(ID);
                    String shortDescription = stepObject.getString(SHORT_DESCRIPTION);
                    String description = stepObject.getString(DESCRIPTION);
                    String videoURL = "tango";
                    if (stepObject.has(VIDEOURL)) {
                        videoURL = stepObject.getString(VIDEOURL);
                    }
                    String thumbnailURL = "forxtrot";
                    if (stepObject.has(THUMBNAILURL)) {
                        thumbnailURL = stepObject.getString(THUMBNAILURL);
                    }

                    Steps aStep = new Steps(id, shortDescription, description, videoURL, thumbnailURL);
                    parsedStepsInfo[k] = aStep;
                }
                parsedDetailsInfo = new Details(parsedIngredientInfo, parsedStepsInfo);
            }
        }
        return parsedDetailsInfo;
    }
}