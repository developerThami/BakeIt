package com.inc.thamsanqa.bakeit;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.inc.thamsanqa.bakeit.IngredientsContract.*;

public class RecipeContentProvider extends ContentProvider {


    public static final int CODE_INGREDIENT = 100;
    public static final int CODE_INGREDIENT_WITH_ID = 101;

    public static final int CODE_CLEAR = 500;

    /*
     * The URI Matcher used by this content provider. The leading "s" in this variable name
     * signifies that this UriMatcher is a static member variable of WeatherProvider and is a
     * common convention in Android programming.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDatabaseHelper databaseHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String ingredientAuthority = IngredientsContract.CONTENT_AUTHORITY;

        matcher.addURI(ingredientAuthority, IngredientsContract.INGREDIENT_PATH, CODE_INGREDIENT);

        matcher.addURI(ingredientAuthority,null, CODE_CLEAR);
        matcher.addURI(ingredientAuthority, IngredientsContract.INGREDIENT_PATH + "/#", CODE_INGREDIENT_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        databaseHelper = new RecipeDatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor returnCursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_INGREDIENT:
                returnCursor = db.query(IngredientEntry.TABLE_NAME, projection, selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);

        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {

            case CODE_INGREDIENT:
                long ingredientId = db.insert(IngredientEntry.TABLE_NAME, null, values);

                returnUri = (ingredientId > 0)
                        ? ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, ingredientId)
                        : null;
                break;

            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int count;
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_CLEAR:
                count = db.delete(IngredientEntry.TABLE_NAME, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }
}
