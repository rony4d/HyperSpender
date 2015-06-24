package com.example.ugochukwu.hyperspender;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ugochukwu.hyperspender.data.BudgetContract.*;
import com.example.ugochukwu.hyperspender.data.BudgetDbHelper;

import java.text.DateFormatSymbols;
import java.util.Calendar;


public class CreateBudgetFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static String monthBudgetName;
    public static Double monthBudgetAmount;
    public static String strMonthBudgetAmount="";
    public static String monthBudgetComment="";


    TextView month_budget_name;
    EditText month_budget_amount;
    EditText month_budget_comment;

    public  static String monthNamevalue;
    long month_id;

    Calendar calendar;

    int monthNumber;


    //method to conver double to string to prevent crashing of budget amount edittext
//    public double convertToDouble(String string){
//        double value;
//        value = Double.parseDouble(string);
//        return value;
//    }


    public CreateBudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        BudgetDbHelper budgetDbHelper = new BudgetDbHelper(getActivity());
        month_id = getActivity().getIntent().getLongExtra(FragmentDetailMonthList.EXTRA_MONTH_ID,-1);

        Log.i("Rony","month id is " + month_id);
        // Inflate the layout for this fragment
       final View rootView = inflater.inflate(R.layout.fragment_create_budget, container, false);

        month_budget_name = (TextView) rootView.findViewById(R.id.monthBudgetName);
        month_budget_amount = (EditText)rootView.findViewById(R.id.monthBudgetAmount);
        month_budget_comment = (EditText)rootView.findViewById(R.id.monthBudgetComment);
        calendar = Calendar.getInstance();
        monthNumber  = calendar.get(Calendar.MONTH);

         monthNamevalue = getMonthForInt(monthNumber);

        month_budget_name.setText(monthNamevalue + " " + "Budget");

        month_budget_amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                strMonthBudgetAmount = month_budget_amount.getText().toString();

            }
        });


        month_budget_comment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                System.out.println("ONtext changed " + new String(s.toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                System.out.println("beforeTextChanged " + new String(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                monthBudgetComment = month_budget_comment.getText().toString();

            }
        });

        //save button click listener
        Button saveButton = (Button) rootView.findViewById(R.id.monthSaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                switch (v.getId()) {
                    case R.id.monthSaveButton:
                        onClickMonthBudgetSave();
                        break;
                }


            }
        });
        if(month_id>0){
            saveButton.setEnabled(true);
            saveButton.setText("Update");
        }
        else if(budgetDbHelper.getMonthWithName(monthNamevalue)){
            saveButton.setEnabled(false);
            saveButton.setText("Save");
        }
        else if(month_id == -1){
            saveButton.setEnabled(true);
            saveButton.setText("Save");
        }

        return rootView;
    }

    public void onClickMonthBudgetSave(){
            if(month_id == -1){
                if(strMonthBudgetAmount==""||monthBudgetComment==""){
                    Toast.makeText(getActivity(),"All fields must be filled",Toast.LENGTH_LONG).show();
                }else {

                    monthBudgetAmount = Double.parseDouble(strMonthBudgetAmount);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MonthEntry.COLUMN_MONTH_NAME, getMonthForInt(monthNumber));
                    contentValues.put(MonthEntry.COLUMN_BUDGET_AMOUNT, monthBudgetAmount);
                    contentValues.put(MonthEntry.COLUMN_COMMENT, monthBudgetComment);

                    Uri uri = getActivity().getContentResolver().insert(MonthEntry.CONTENT_URI, contentValues);
                    Toast.makeText(getActivity(), "" + uri, Toast.LENGTH_LONG).show();
//            Fragment fragment = new HyperSpenderFragmentList();
//            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                    Intent intent = new Intent(getActivity(),ActivityHyperSpenderList.class);
                    startActivity(intent);
                }
            }else if(month_id > 0){

                if(strMonthBudgetAmount==""||monthBudgetComment==""){
                    Toast.makeText(getActivity(),"All fields must be filled",Toast.LENGTH_LONG).show();
                }else {

                    monthBudgetAmount = Double.parseDouble(strMonthBudgetAmount);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MonthEntry.COLUMN_MONTH_NAME, getMonthForInt(monthNumber));
                    contentValues.put(MonthEntry.COLUMN_BUDGET_AMOUNT, monthBudgetAmount);
                    contentValues.put(MonthEntry.COLUMN_COMMENT, monthBudgetComment);


                    Uri uri = Uri.parse(MonthEntry
                            .CONTENT_URI + "/" + month_id);
                    int result = getActivity().getContentResolver().update(uri, contentValues, null, null);

                    Toast.makeText(getActivity(), "" + uri, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(),ActivityHyperSpenderList.class);
                    startActivity(intent);
                }
            }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse(MonthEntry.CONTENT_URI + "/" + month_id);

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
        double amount = data.getDouble(data.getColumnIndex(MonthEntry.COLUMN_BUDGET_AMOUNT));
        String  strAmount = String.valueOf(amount);
        month_budget_amount.setText(strAmount);

        String comment = data.getString(data.getColumnIndex(MonthEntry.COLUMN_COMMENT));
        month_budget_comment.setText(comment);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Create Budget");
        if(month_id !=-1) {
            getLoaderManager().initLoader(0, null, this);
        }
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
