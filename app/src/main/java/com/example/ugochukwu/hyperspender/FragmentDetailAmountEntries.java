package com.example.ugochukwu.hyperspender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ugochukwu.hyperspender.data.BudgetContract;
import com.example.ugochukwu.hyperspender.data.BudgetContract.AmountEntry;


public class FragmentDetailAmountEntries extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>{

    public static final String EXTRA_AMOUNT_ID = "com.example.ugochukwu.hyperspender/amount_id";

    public static final int DETAIL_LOADER = 0;


    Uri uri;
    LinearLayout  linearLayout1;
    TextView txtDetailAmountValue;
    TextView txtDetailCategoryValue;
    TextView txtDetailBriefDescriptionValue;
    TextView txtDetailDateValue;
    Button   btnDetailEditButton;
    Button   btnDetailDeleteButton;
    Button   btnDetailCloseButton;
    TextView noDataTextView;
    LinearLayout linearLayout;

    Bundle savedInstanceState;

    public FragmentDetailAmountEntries() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle arguments = getArguments();
        this.savedInstanceState = savedInstanceState;
        final View rootView = inflater.inflate(R.layout.fragment_detail_amount_entries, container, false);

        if(getActivity().getIntent().hasExtra(AmountEntry.CONTENT_ITEM_TYPE)){
            uri = getActivity().getIntent().getParcelableExtra(AmountEntry.CONTENT_ITEM_TYPE);
            Log.i("Rony","Uri phone view "+uri);
        }
        else if (arguments!= null){
            uri = arguments.getParcelable(BudgetContract.AmountEntry.CONTENT_ITEM_TYPE);
            Log.i("Rony","Uri is "+ uri);
        }


        noDataTextView = (TextView) rootView.findViewById(R.id.txt_no_data);
        linearLayout1 = (LinearLayout) rootView.findViewById(R.id.linear_layout_parent);
        txtDetailAmountValue =(TextView) rootView.findViewById(R.id.txt_detail_amount_value);
        txtDetailCategoryValue =(TextView) rootView.findViewById(R.id.txt_detail_category_value);
        txtDetailBriefDescriptionValue =(TextView) rootView.findViewById(R.id.txt_detail_brief_description_value);
        txtDetailDateValue =(TextView) rootView.findViewById(R.id.txt_detail_date_value);
        btnDetailEditButton=(Button) rootView.findViewById(R.id.btn_detail_edit_button);
        btnDetailDeleteButton =(Button) rootView.findViewById(R.id.btn_detail_delete_button);
        btnDetailCloseButton =(Button) rootView.findViewById(R.id.btn_detail_close_button);

        //Edit button click
        btnDetailEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                switch (v.getId()) {
                    case R.id.btn_detail_edit_button:
                        onClickEdit(rootView);
                        break;
                }


            }
        });
        //Delete button click
        btnDetailDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Pass the fragmentView through to the handler
                // so that findViewById can be used to get a handle on
                // the fragments own views.
                switch (v.getId()) {
                    case R.id.btn_detail_delete_button:
                        onClickDelete(rootView);
                        break;
                }


            }
        });
         btnDetailCloseButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getActivity().finish();
             }
         });

        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != uri) {
            return new android.support.v4.content.CursorLoader(getActivity(),
                    uri,
                    null,
                    null,
                    null,
                    null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()){
            linearLayout1.setVisibility(View.GONE);
            noDataTextView.setVisibility(View.VISIBLE);
            return;
        }
        double amount = data.getDouble( data.getColumnIndex(AmountEntry.COLUMN_AMOUNT));
        String strAmount = String.valueOf(amount);
        txtDetailAmountValue.setText(strAmount);

        String category = data.getString(data.getColumnIndex(AmountEntry.COLUMN_CATEGORY));
        txtDetailCategoryValue.setText(category);

        String briefDesc = data.getString(data.getColumnIndex(AmountEntry.COLUMN_BRIEF_DESCRIPTION));
        txtDetailBriefDescriptionValue.setText(briefDesc);

        String date = data.getString(data.getColumnIndex(AmountEntry.COLUMN_DATETEXT));
        txtDetailDateValue.setText(date);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       getLoaderManager().initLoader(DETAIL_LOADER, null, this );
    }

    public void onClickEdit(View view){
        Intent intent = new Intent(getActivity(),ActivityAddEditAmount.class);
        String amount_id = uri.getLastPathSegment();
        intent.putExtra(FragmentAddEditAmount.EXTRA_AMOUNT_ID, amount_id);
        startActivity(intent);
    }

    public void onClickDelete(View view){
      deleteAletrDialog();

    }

    public void deleteAletrDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                String amount_id = uri.getLastPathSegment();
                Uri new_uri = Uri.parse(AmountEntry.CONTENT_URI + "/" + amount_id);

                int result = getActivity().getContentResolver().delete(new_uri, null, null);
                if (result == 1) {
                    Bundle arguments = getArguments();
                    Toast.makeText(getActivity(), "Successfully Deleted", Toast.LENGTH_LONG);
                    long month_id = -1;
                    if (getActivity().getIntent().hasExtra(FragmentDetailMonthList.EXTRA_MONTH_ID)) {
                        month_id = getActivity().getIntent().getLongExtra(FragmentDetailMonthList.EXTRA_MONTH_ID, -1);
                    }
                    else if(arguments != null){
                        month_id = arguments.getLong(FragmentDetailMonthList.EXTRA_MONTH_ID);
                    }
                    Intent intent = new Intent(getActivity(), ActivityDetailMonthList.class);
                    intent.putExtra(FragmentDetailMonthList.EXTRA_MONTH_ID, month_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_LONG);
                }
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing

                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        long amount_id = getActivity().getIntent().getLongExtra(EXTRA_AMOUNT_ID, -1);

      // getLoaderManager().restartLoader(DETAIL_LOADER,null,this);
        onCreate(savedInstanceState);
//     // Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);
//        Log.i("Rony",String.valueOf(cursor));
//        if(!cursor.moveToFirst()){
//            getActivity().finish();
//        }


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Rony","onStop Called");
    }
}
