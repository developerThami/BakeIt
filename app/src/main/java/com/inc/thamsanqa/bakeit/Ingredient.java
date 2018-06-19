package com.inc.thamsanqa.bakeit;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    public static final String _INGREDIENT = "ingredient";
    public static final String _QUANTITY = "quantity";
    public static final String _MEASURE = "measure";

    private String ingredient;
    private int quantity;
    private String measure;

    protected Ingredient(Parcel in) {
        ingredient = in.readString();
        quantity = in.readInt();
        measure = in.readString();
    }

    public Ingredient() {
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredient);
        dest.writeInt(quantity);
        dest.writeString(measure);
    }
}
