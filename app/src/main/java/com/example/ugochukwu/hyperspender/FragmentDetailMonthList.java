package com.example.ugochukwu.hyperspender;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ugochukwu.hyperspender.data.BudgetContract.AmountEntry;
import com.example.ugochukwu.hyperspender.data.BudgetContract.MonthEntry;
import com.example.ugochukwu.hyperspender.data.BudgetDbHelper;


public class FragmentDetailMonthList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SELECTED_KEY = "com.example.ugochukwu.hyperspender/SELECTEDKEY";

    BudgetDbHelper budgetDbHelper;

    long month_id;
    TextView txtNetBalance;
    TextView txtNetBalanceValue;
    TextView txtGrossBalance;
    TextView txtGrossBalanceValue;
    int mPosition;

    TextView txtMonthName;
    TextView txtAmount;
    TextView txtComment;

    Bundle savedInstanceState;

    public static final String EXTRA_MONTH_ID = "com.example.ugochukwu.hyperspender/EXTRAMONTHID";
    public static final String EXTRA_AMOUNT_ID = "com.example.ugochukwu.hyperspender/EXTRAAMOUNTID";
    private static final int DETAIL_LOADER = 0;
    CustomCursorAdapter customCursorAdapter;
    ListView listView;
    public static String month;

    static final int COLUMN_BUDGET_NAME = 0;
    static final int COLUMN_BUDGET_AMOUNT = 1;
    static final int COLUMN_BUDGET_COMMENT = 2;
    static final int COLUMN_DATE = 3;
    static final int COLUMN_AMOUNT = 4;
    static final int COLUMN_CATEGORY = 5;
    static final int COLUMN_AMOUNT_TYPE = 6;

    SharedPreferences sharedPrefs;
    public FragmentDetailMonthList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getActivity().getIntent().hasExtra(MonthEntry.CONTENT_ITEM_TYPE)){
            month_id = 1;
        }
        else if(getActivity().getIntent().hasExtra(FragmentDetailMonthList.EXTRA_MONTH_ID)){
            month_id = getActivity().getIntent().getLongExtra(FragmentDetailMonthList.EXTRA_MONTH_ID,-1);
        }
        sharedPrefs = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if(month_id != 0){
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putLong("monthId", month_id);
            editor.commit();

        }

        if(sharedPrefs.contains("monthId")){
            month_id = sharedPrefs.getLong("monthId",-1);
        }
        this.savedInstanceState = savedInstanceState;
        budgetDbHelper = new BudgetDbHelper(getActivity());
        double totalMadeForMonth = budgetDbHelper.getTotalMadeForMonth(month_id);
        double totalSpentForMonth = budgetDbHelper.getTotalSpentForMonth(month_id);

        double netBalanceForMonth =(totalMadeForMonth )- (totalSpentForMonth);
        double budgetAmount = budgetDbHelper.getBudgetamount(month_id);
        double grossBalanceForMonth = (budgetAmount) +( netBalanceForMonth);

        customCursorAdapter = new CustomCursorAdapter(getActivity(),null,0);
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_detail_month_list, container, false);
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.list_view_grand_parent);

        listView = (ListView) rootView.findViewById(R.id.detailMonthListView);
        listView.smoothScrollToPosition(0);
        listView.setSelection(0);
        View myView = inflater.inflate(R.layout.list_item_month_upper,container);

        // for the month budget details
        txtMonthName = (TextView)myView.findViewById(R.id.list_item_budget_name);
        txtAmount = (TextView)myView.findViewById(R.id.list_item_budget_amount);
        txtComment = (TextView)myView.findViewById(R.id.list_item_budget_comment);

        Uri monthUri = Uri.parse(MonthEntry.CONTENT_URI + "/" + month_id);
        Cursor cursor = getActivity().getContentResolver().query(monthUri, null, null, null, null );
        if(cursor != null  && cursor.moveToFirst()) {
            String monthStr = cursor.getString(cursor.getColumnIndex(MonthEntry.COLUMN_MONTH_NAME));
            txtMonthName.setText(monthStr + ", 2015 Budget");
            Double amount = cursor.getDouble(cursor.getColumnIndex(MonthEntry.COLUMN_BUDGET_AMOUNT));
            String amountStr = String.format("N" + "%,.2f", amount);
            txtAmount.setText(amountStr);
            txtComment.setTypeface(null, Typeface.ITALIC);
            txtComment.setText(cursor.getString(cursor.getColumnIndex(MonthEntry.COLUMN_COMMENT)));
        }else {
            txtAmount.setTextColor(Color.RED);
            txtAmount.setTypeface(null, Typeface.BOLD);
            txtAmount.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ActivityCreateBudget.class);
                    startActivity(i);
                }
            });
        }

        listView.addHeaderView(myView);
        ViewParent view = relativeLayout;

        listView.setAdapter(customCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    long amount_id = cursor.getLong(cursor.getColumnIndex(AmountEntry._ID));
                    Log.i("Rony", "Amount Id" + amount_id);
//                    Intent intent = new Intent(getActivity(),ActivityDetailAmountEntries.class);
                    Uri uri = Uri.parse(AmountEntry.CONTENT_URI + "/" + amount_id);

//                    intent.putExtra(AmountEntry.CONTENT_ITEM_TYPE, uri);
//                   intent.putExtra(EXTRA_MONTH_ID,month_id);
//                    intent.putExtra(FragmentDetailAmountEntries.EXTRA_AMOUNT_ID,amount_id);
//                    startActivity(intent);

                    ((Callback) getActivity()).onItemSelected(uri, month_id);

                }
                mPosition = position;
            }
        });
        setHasOptionsMenu(true);

        txtNetBalance = (TextView) rootView.findViewById(R.id.footer_net_balance);
        txtNetBalanceValue = (TextView)rootView.findViewById(R.id.footer_net_balance_value);
        txtGrossBalance = (TextView)rootView.findViewById(R.id.footer_gross_balance);
        txtGrossBalanceValue = (TextView) rootView.findViewById(R.id.footer_gross_balance_value);

//        String strNetBalanceForMonth = String.valueOf(netBalanceForMonth);
//        String strGrossBalanceForMonth = String.valueOf(grossBalanceForMonth);
        txtNetBalanceValue.setText(String.format("N"+"%,.2f",netBalanceForMonth));
        txtGrossBalanceValue.setText(String.format("N"+"%,.2f",grossBalanceForMonth));

        //NetBalance Text View click listener
        txtNetBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                switch (v.getId()) {
                    case R.id.footer_net_balance:
                        onClickNetBalance(rootView);
                        break;
                }


            }
        });

        //GrossBalance Text View click listener
        txtGrossBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                switch (v.getId()) {
                    case R.id.footer_gross_balance:
                        onClickGrossBalance(rootView);
                        break;
                }


            }
        });
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }



        return rootView;
    }



    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri = Uri.parse(AmountEntry.CONTENT_URI + "/month/" + 1);

        return new android.support.v4.content.CursorLoader(getActivity(),
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        customCursorAdapter.swapCursor(data);
        if(mPosition != ListView.INVALID_POSITION){
            listView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

            inflater.inflate(R.menu.menu_detail_month_list, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri monthUri = Uri.parse(MonthEntry.CONTENT_URI + "/" + month_id);
        Cursor cursor = getActivity().getContentResolver().query(monthUri, null, null, null, null );
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_amount_made) {

            if(cursor!= null && cursor.moveToFirst()){
                Intent intent = new Intent(getActivity(),ActivityAddEditAmount.class);
                int accountType = 1;
                intent.putExtra(FragmentAddEditAmount.EXTRA_MADE_ACCOUNT_TYPE,accountType);
                intent.putExtra(FragmentAddEditAmount.EXTRA_MONTH_ID,month_id);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(),"Add budget first",Toast.LENGTH_LONG).show();
            }
            return true;
        }else if(id == R.id.action_add_amount_spent){
            if(cursor!= null && cursor.moveToFirst()){
                Intent intent = new Intent(getActivity(),ActivityAddEditAmount.class);
                intent.putExtra(EXTRA_MONTH_ID,month_id);
                int accountType = 0;
                intent.putExtra(FragmentAddEditAmount.EXTRA_SPENT_ACCOUNT_TYPE,accountType);
                intent.putExtra(FragmentAddEditAmount.EXTRA_MONTH_ID,month_id);

                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(),"Add budget first",Toast.LENGTH_LONG).show();
            }

        }else if(id == R.id.action_edit_budget){
            if(cursor!= null && cursor.moveToFirst()){
                Intent intent = new Intent(getActivity(),ActivityCreateBudget.class);
                intent.putExtra(EXTRA_MONTH_ID,month_id);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(),"Add budget first",Toast.LENGTH_LONG).show();
            }

        }else if(id == R.id.action_budget_graph){
            if(cursor!= null && cursor.moveToFirst()){
                Intent intent = new Intent(getActivity(),ActivityGraphDisplay.class);
                intent.putExtra(FragmentDetailMonthList.EXTRA_MONTH_ID,month_id);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(),"Add budget first",Toast.LENGTH_LONG).show();
            }

        }
        else if(id == R.id.action_settings){
            Intent intent = new Intent(getActivity(),ActivitySettings.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        customCursorAdapter.swapCursor(null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Rony", "onResume Called");
        double totalMadeForMonth = budgetDbHelper.getTotalMadeForMonth(month_id);
        double totalSpentForMonth = budgetDbHelper.getTotalSpentForMonth(month_id);

        double netBalanceForMonth =(totalMadeForMonth )- (totalSpentForMonth);
        double budgetAmount = budgetDbHelper.getBudgetamount(month_id);
        double grossBalanceForMonth = (budgetAmount) +( netBalanceForMonth);
        txtNetBalanceValue.setText(String.format("N" + "%,.2f",netBalanceForMonth));
        txtGrossBalanceValue.setText(String.format("N" + "%,.2f", grossBalanceForMonth));
        Log.i("Rony", "" + netBalanceForMonth);
        Log.i("Rony", "" + grossBalanceForMonth);
        Log.i("Rony","" + month_id );



        getLoaderManager().restartLoader(DETAIL_LOADER,null,this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Rony", "onStart Called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mPosition != ListView.INVALID_POSITION){
            outState.putInt(SELECTED_KEY,mPosition);
        }

        outState.putLong("monthId", month_id);
    }


    //Callback interface that all activities containing this interface must implement
    public interface Callback{
        public void onItemSelected(Uri uri,long month_id);
    }

    public void onClickNetBalance(View view){
        netBalanceExplanationDialog();
    }

    public void onClickGrossBalance(View view){
        grossBalanceExplanationDialog();
    }

    //AlertDialog method
    private void netBalanceExplanationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Hint: Net balance");
        builder.setMessage("The net balance is the difference between the total amount spent and total amount made");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();
    }
    //AlertDialog method
    private void grossBalanceExplanationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Hint: Gross Balance");
        builder.setMessage("The gross balance is the sum of the net balance and budget amount" +
                " Note: If the net balance is negative, the gross balance will be the difference between " +
                "the budget amount and net balance");
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();
    }







}
