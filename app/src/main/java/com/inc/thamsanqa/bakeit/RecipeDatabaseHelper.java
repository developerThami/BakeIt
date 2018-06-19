package com.inc.thamsanqa.bakeit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.inc.thamsanqa.bakeit.IngredientsContract.*;


public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ingredient.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_NOT_NULL = " TEXT NOT NULL, ";
    private static final String CREATE_TABLE = "CREATE TABLE ";

    private static final String CLOSE_BRACE = " )";
    private static final String OPEN_BRACE = " (";

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_INGREDIENT_TABLE = new StringBuilder(CREATE_TABLE)
                .append(IngredientEntry.TABLE_NAME.concat(OPEN_BRACE))
                .append(IngredientEntry.COLUMN_RECIPE_ID.concat(TEXT_NOT_NULL))
                .append(IngredientEntry.COLUMN_INGREDIENT.concat(TEXT_NOT_NULL))
                .append(IngredientEntry.COLUMN_MEASURE.concat(TEXT_NOT_NULL))
                .append(IngredientEntry.COLUMN_QUANTITY.concat(" TEXT NOT NULL" + CLOSE_BRACE)).toString();

        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(db);
    }
}
