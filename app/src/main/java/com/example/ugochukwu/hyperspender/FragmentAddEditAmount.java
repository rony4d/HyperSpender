package com.example.ugochukwu.hyperspender;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ugochukwu.hyperspender.data.BudgetContract.AmountEntry;
import com.example.ugochukwu.hyperspender.data.BudgetContract.MonthEntry;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class FragmentAddEditAmount extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    public static final String EXTRA_SPENT_ACCOUNT_TYPE = "com.example.ugochukwu.hyperspender/spent_account_type";
    public static final String EXTRA_MADE_ACCOUNT_TYPE = "com.example.ugochukwu.hyperspender/made_account_type";
    public static final String EXTRA_MONTH_ID = "com.example.ugochukwu.hyperspender/month_id";
    public static final String EXTRA_AMOUNT_ID =  "com.example.ugochukwu.hyperspender/amount_id";
    public static final int DETAIL_LOADER =0;

    public static String amountBriefDescription;
    public static String strAmount;
    public static double amount;
    public static String category_select;
    public static String date;

    private String[] categoryItems = {"Eating Out","Utilities","Shopping","Income","Transportation","Rent","Medical","Groceries","Uncategorized"};

    public FragmentAddEditAmount() {
        // Required empty public constructor
    }
    long month_id;
    int typeId;
    Uri uri;
    long amount_id;
    String customDate;
    TextView dateView;
    Button categoryButton;
    Button saveButton;
    EditText amount_biref_description;
    EditText txt_amount;
    String newCategory;
    String newBriefDesc;
    String newStrAmount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        amountBriefDescription = "";
        strAmount = "";
        if(getActivity().getIntent().hasExtra(EXTRA_AMOUNT_ID)){
            amount_id = Long.parseLong(getActivity().getIntent().getStringExtra(EXTRA_AMOUNT_ID)) ;
            uri = Uri.parse(AmountEntry.CONTENT_URI + "/" + amount_id);
        }
        else{
            amount_id = -1;// to prevent null pointer
        }


        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_edit_amount, container, false);
        dateView = (TextView) rootView.findViewById(R.id.amountDate);
        Calendar now = Calendar.getInstance();
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK) -1;
        int month = now.get(Calendar.MONTH);
        String strMonth= getMonthForInt(month);
        int dateInMonth = now.get(Calendar.DAY_OF_MONTH);
        int year = now.get(Calendar.YEAR);


        String[] daysOfWeekArray = {"Sun","Mon","Tue","Wed","Thurs","Fri","Sat"};
        String strDateInMonth = String.valueOf(dateInMonth);
        String strYear = String.valueOf(year);
        String strDayOfWeek = daysOfWeekArray[dayOfWeek];

        customDate = strDayOfWeek +", "+ strDateInMonth +" "+ strMonth+" "+strYear;

        dateView.setText(customDate);
        dateView.setText(customDate);
        String dbDate =  strDayOfWeek +", "+ strDateInMonth;
        date = dbDate;
        //Amount Edit text Field

            txt_amount = (EditText) rootView.findViewById(R.id.txt_amount);
            txt_amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                strAmount = txt_amount.getText().toString();
            }
        });

        //Brief desription text field
        amount_biref_description = (EditText) rootView.findViewById(R.id.amountBriefDescription);
        amount_biref_description.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                amountBriefDescription = amount_biref_description.getText().toString();

            }
        });


        //Save button click listener
        saveButton = (Button) rootView.findViewById(R.id.amountSave);
        if(amount_id > 0){
            saveButton.setText("Update");
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                switch (v.getId()) {
                    case R.id.amountSave:
                        onClickSave(rootView);
                        break;
                }
            }
        });

        //category button click listener
        categoryButton = (Button) rootView.findViewById(R.id.amountCategory);
      //  category_select = categoryButton.getText().toString();
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                switch (v.getId()) {
                    case R.id.amountCategory:
                        CategoryClick(rootView);
                        break;

                }
            }
        });
        return rootView;
    }
    //AlertDialog method
    private void SingleChoiceWithRadioButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Category List");
        builder.setSingleChoiceItems(categoryItems, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemValue = categoryItems[which];
                        category_select = categoryItems[which];
                        newCategory = categoryItems[which];
                        categoryButton.setText(itemValue);
                        Toast.makeText(getActivity(),
                                categoryItems[which] + " Selected",
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();
    }

    //AlertDialog button Click
    public void CategoryClick(View view) {
        SingleChoiceWithRadioButton();


    }


    //Save Button Click method

    public void onClickSave(View view) {


        int spentAccountType = getActivity().getIntent().getIntExtra(EXTRA_SPENT_ACCOUNT_TYPE, -1);
        int madeAccountType = getActivity().getIntent().getIntExtra(EXTRA_MADE_ACCOUNT_TYPE, -1);

        if(amount_id== -1) {
            if (spentAccountType != -1) {
                typeId = spentAccountType;
            } else {
                typeId = madeAccountType;
            }

            if (amountBriefDescription == "" || strAmount == "") {
                Toast.makeText(getActivity(), "All fields must be filled", Toast.LENGTH_LONG).show();
                amount_biref_description.setError("Field must be filled");
                txt_amount.setError("Field must be filled");

            } else {
                amount = Double.parseDouble(strAmount);
                month_id = getActivity().getIntent().getLongExtra(EXTRA_MONTH_ID, -1);
                ContentValues contentValues = new ContentValues();

                contentValues.put(AmountEntry.COLUMN_DATETEXT, date);
                contentValues.put(AmountEntry.COLUMN_CATEGORY, category_select);
                contentValues.put(AmountEntry.COLUMN_BRIEF_DESCRIPTION, amountBriefDescription);
                contentValues.put(AmountEntry.COLUMN_AMOUNT, amount);
                contentValues.put(AmountEntry.COLUMN_AMOUNT_TYPE, typeId);
                contentValues.put(AmountEntry.COLUMN_MONTH_KEY, month_id);
                Uri result = getActivity().getContentResolver().insert(AmountEntry.CONTENT_URI, contentValues);
                Intent intent = new Intent(getActivity(), ActivityDetailMonthList.class);
                Uri uri = Uri.parse(MonthEntry.CONTENT_URI + "/" + month_id);
                intent.putExtra(MonthEntry.CONTENT_ITEM_TYPE, uri);
                startActivity(intent);

            }
        }

        else if(amount_id >0){

           if(strAmount.isEmpty() || amountBriefDescription.isEmpty()){
               txt_amount.setError("Field must be filled");
               amount_biref_description.setError("Field must be filled");
           }
            else {
                    amount = Double.parseDouble(strAmount);

                ContentValues contentValues = new ContentValues();

                contentValues.put(AmountEntry.COLUMN_DATETEXT, date);
                contentValues.put(AmountEntry.COLUMN_CATEGORY, newCategory);
                contentValues.put(AmountEntry.COLUMN_BRIEF_DESCRIPTION, amountBriefDescription);
                contentValues.put(AmountEntry.COLUMN_AMOUNT, amount);
                contentValues.put(AmountEntry.COLUMN_AMOUNT_TYPE, typeId);
                contentValues.put(AmountEntry.COLUMN_MONTH_KEY, month_id);
                Uri uri = Uri.parse(AmountEntry.CONTENT_URI + "/" + amount_id);
                int result = getActivity().getContentResolver().update(uri, contentValues, null, null);
                if (result == 1) {
                    Intent intent = new Intent(getActivity(), ActivityDetailMonthList.class);
                    Uri uri_new = Uri.parse(MonthEntry.CONTENT_URI + "/" + month_id);
                    intent.putExtra(MonthEntry.CONTENT_ITEM_TYPE, uri_new);
                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_LONG);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_LONG);
                }
            }

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.CursorLoader(getActivity(),
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()){
            return;
        }
        newCategory = data.getString(data.getColumnIndex(AmountEntry.COLUMN_CATEGORY));
        categoryButton.setText(newCategory);


        newBriefDesc = data.getString(data.getColumnIndex(AmountEntry.COLUMN_BRIEF_DESCRIPTION));
        amount_biref_description.setText(newBriefDesc);

        double amount = data.getDouble(data.getColumnIndex(AmountEntry.COLUMN_AMOUNT));
        newStrAmount = String.valueOf(amount);
        txt_amount.setText(newStrAmount);

        month_id = data.getLong(data.getColumnIndex(AmountEntry.COLUMN_MONTH_KEY));

        typeId = data.getInt(data.getColumnIndex(AmountEntry.COLUMN_AMOUNT_TYPE));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(amount_id !=-1) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}
