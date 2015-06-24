package com.example.ugochukwu.hyperspender.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.ugochukwu.hyperspender.data.BudgetContract.*;

/**
 * Created by ugochukwu on 6/7/2015.
 */

public class BudgetProvider  extends ContentProvider{
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BudgetDbHelper mOpenHelper;
    private static final int MONTH_BUDGET = 300;
    private static final int MONTH_BUDGET_ID = 301;
    private static final int AMOUNT = 200;
    private static final int AMOUNT_ID = 201;
    private static final int AMOUNT_WITH_BUDGET_ID = 400;



    private static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BudgetContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, BudgetContract.PATH_MONTH, MONTH_BUDGET);
        matcher.addURI(authority, BudgetContract.PATH_MONTH + "/#", MONTH_BUDGET_ID);
        matcher.addURI(authority, BudgetContract.PATH_AMOUNT, AMOUNT);
        matcher.addURI(authority, BudgetContract.PATH_AMOUNT + "/#", AMOUNT_ID);
        matcher.addURI(authority, BudgetContract.PATH_AMOUNT + "/month/#", AMOUNT_WITH_BUDGET_ID);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BudgetDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // check if the caller has requested a column which does not exists

        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case MONTH_BUDGET:
                // set the table
                queryBuilder.setTables(MonthEntry.TABLE_NAME);
                break;
            case AMOUNT_ID:
                // set the table
                queryBuilder.setTables(AmountEntry.TABLE_NAME);
                queryBuilder.appendWhere(AmountEntry._ID + "=" + uri.getLastPathSegment());
                break;
            case AMOUNT_WITH_BUDGET_ID:
                queryBuilder.setTables(AmountEntry.TABLE_NAME);
                queryBuilder.appendWhere(AmountEntry.COLUMN_MONTH_KEY+ "=" + uri.getLastPathSegment());
                break;
            case MONTH_BUDGET_ID:
                queryBuilder.setTables(MonthEntry.TABLE_NAME);
                queryBuilder.appendWhere(MonthEntry._ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case AMOUNT_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(AmountEntry.TABLE_NAME, AmountEntry._ID + "=" + id, null);
                    Log.i("RowsUpdated",String.valueOf(rowsDeleted));
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated = 0;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case AMOUNT_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(AmountEntry.TABLE_NAME, values, AmountEntry._ID + "=" + id, null);
                    Log.i("RowsUpdated",String.valueOf(rowsUpdated));
                }else{
                    rowsUpdated = db.update(AmountEntry.TABLE_NAME,values,AmountEntry._ID + "=" +id + "and " +selection,selectionArgs);
                }
                break;
            case MONTH_BUDGET_ID:
                String month_budget_id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(MonthEntry.TABLE_NAME, values, MonthEntry._ID + "=" + month_budget_id, null);
                    Log.i("RowsUpdated",String.valueOf(rowsUpdated));
                }else{
                    rowsUpdated = db.update(AmountEntry.TABLE_NAME,values,AmountEntry._ID + "=" +month_budget_id + "and " +selection,selectionArgs);
                }
                break;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {

            case MONTH_BUDGET: {
                long _id = db.insert(MonthEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = uri;
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case AMOUNT: {
                long _id = db.insert(AmountEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = uri;
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }






}