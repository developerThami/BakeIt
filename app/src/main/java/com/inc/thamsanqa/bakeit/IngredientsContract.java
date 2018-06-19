package com.inc.thamsanqa.bakeit;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientsContract {

    public static final String CONTENT_AUTHORITY = "com.inc.thamsanqa.bakeit";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String INGREDIENT_PATH = "ingredient";

    public final static class IngredientEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(INGREDIENT_PATH)
                .build();

        public static final String TABLE_NAME = "ingredient";

        public static final String COLUMN_RECIPE_ID = "id";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";

    }

}
