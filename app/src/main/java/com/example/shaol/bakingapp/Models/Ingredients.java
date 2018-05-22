package com.example.shaol.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shaol on 5/17/2018.
 */

public class Ingredients implements Parcelable {

    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredients(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getQuantity() {return quantity;}

    public void setQuantity(String quantity) {this.quantity = quantity;}

    public String getMeasure() {return measure;}

    public void setMeasure(String measure) {this.measure = measure;}

    public String getIngredients() {return ingredient;}

    public void setIngredient(String ingredient) {this.ingredient = ingredient;}

    public Ingredients(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.quantity = data[0];
        this.measure = data[1];
        this.ingredient = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parc, int flags) {
        parc.writeStringArray(new String[] {this.quantity, this.measure, this.ingredient});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
}
