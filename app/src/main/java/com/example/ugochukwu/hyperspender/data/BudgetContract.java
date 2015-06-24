package com.example.ugochukwu.hyperspender.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ugochukwu on 6/7/2015.
 */
public class BudgetContract  {


    public static final String CONTENT_AUTHORITY = "com.example.ugochukwu.hyperspender";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MONTH = "month_table";
    public static final String PATH_AMOUNT = "amount_table";

    /* Inner class that defines the table contents of the budget table */
    public static final class MonthEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MONTH).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MONTH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_MONTH;

        // Table name
        public static final String TABLE_NAME = "month_budget_table";

        public static final String COLUMN_MONTH_NAME = "month_name";

        public static final String COLUMN_BUDGET_AMOUNT = "budget_amount";

        public static final String COLUMN_COMMENT = "comment";

}
    /* Inner class that defines the table contents of the budget table */
    public static final class AmountEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_AMOUNT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_AMOUNT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_AMOUNT;

        // Table name
        public static final String TABLE_NAME = "amount_table";

        public static final String COLUMN_MONTH_KEY = "month_id";

        // Date, stored as Text with format yyyy-MM-dd
        public static final String COLUMN_DATETEXT = "date";

        public static final String COLUMN_AMOUNT_TYPE = "amount_type";

        public static final String COLUMN_AMOUNT = "amount";

        public static final String COLUMN_CATEGORY = "category";

        public static final String COLUMN_BRIEF_DESCRIPTION = "brief_description";

    }





}
