package com.example.ugochukwu.hyperspender;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Calendar;


public class CustomCursorAdapter extends CursorAdapter{

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);


        CustomCursorAdapter customCursorAdapter;


        private Context mContext;
        private static final int VIEW_TYPE_COUNT = 2;
        private static final int VIEW_TYPE_MONTH_BUDGET = 0;
        private static final int VIEW_TYPE_AMOUNT_MADE_AND_SPENT = 1;

        // Flag to determine if we want to use a separate view for "today".
        private boolean mUseTodayLayout = true;

        /**
         * Cache of the children views for a forecast list item.
         */
        public static class ViewHolder {
                public final TextView budgetNameView;
                public final TextView budgetAmountView;
                public final TextView budgetCommentView;
                public final TextView amountDayView;
                public final TextView amountMonthView;
                public final TextView amountCategoryView;
                public final TextView amountView;


                public ViewHolder(View view) {
                        budgetNameView = (TextView) view.findViewById(R.id.list_item_budget_name);
                        budgetAmountView = (TextView) view.findViewById(R.id.list_item_budget_amount);
                        budgetCommentView = (TextView) view.findViewById(R.id.list_item_budget_comment);
                        amountDayView = (TextView) view.findViewById(R.id.list_item_day);
                        amountMonthView = (TextView) view.findViewById(R.id.list_item_date);
                        amountCategoryView = (TextView) view.findViewById(R.id.list_item_category);
                        amountView = (TextView) view.findViewById(R.id.list_item_amount);
                }
        }

        public void setUseTodayLayout(boolean useTodayLayout) {
                mUseTodayLayout = useTodayLayout;
        }

        @TargetApi(11)
        public CustomCursorAdapter(Context context, Cursor c, int flags) {

                super(context, c, flags);
                mContext = context;
        }

        /*
            Remember that these views are reused as needed.
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Choose the layout type

                View view = LayoutInflater.from(context).inflate(R.layout.list_item_month_lower, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);

                return view;
        }

        /*
            This is where we fill-in the views with the contents of the cursor.
         */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

                ViewHolder viewHolder = (ViewHolder) view.getTag();

                String amountDay = cursor.getString(2);
                String [] array = amountDay.split(", ",-1);
                String strAmountDay = array[0];
                viewHolder.amountDayView.setText(strAmountDay);

               // String amountMonth = cursor.getString(FragmentDetailMonthList.COLUMN_DATE);

                String strAmountMonthDate = array[1];
                viewHolder.amountMonthView.setText(strAmountMonthDate);

                String amountCategory = cursor.getString(5);
                viewHolder.amountCategoryView.setText(amountCategory);

                double amount = cursor.getDouble(3);
                // Find TextView and set weather forecast on it
                viewHolder.amountView.setText(String.format("N"+"%,.2f",amount));

                if(cursor.getInt(4)==1){
                        viewHolder.amountView.setTextColor(Color.parseColor("#50E610"));
                }
                else{
                        viewHolder.amountView.setTextColor(Color.parseColor("#FF0000"));
                }
        }


}