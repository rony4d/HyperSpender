package com.example.ugochukwu.hyperspender;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.ugochukwu.hyperspender.data.BudgetContract;
import com.example.ugochukwu.hyperspender.data.BudgetContract.*;
import com.example.ugochukwu.hyperspender.FragmentDetailMonthList;

/**
 * Created by ugochukwu on 6/10/2015.
 */
public class HyperSpenderFragmentList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter simpleCursorAdapter;

    private ListView customListView;

    private static final String SELECTED_KEY = "selected_position";

    private static final int BUDGET_LOADER = 0;




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                MonthEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    simpleCursorAdapter.swapCursor(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // The ForecastAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        String[] column = new String[]{MonthEntry.COLUMN_MONTH_NAME};

        int [] to = new int[]{R.id.month_name};

       simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),R.layout.fragment_month_list_item,null,column,to);



        View rootView = inflater.inflate(R.layout.hyperspender_fragment_list,container,false);
        // Get a reference to the ListView, and attach this adapter to it.
        customListView = (ListView) rootView.findViewById(R.id.listViewForFragment);
        customListView.setAdapter(simpleCursorAdapter);


        // We'll call our MainActivity
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if(cursor!=null){
                    long id = cursor.getLong(cursor.getColumnIndex(MonthEntry._ID));
                    Intent intent = new Intent(getActivity(),ActivityDetailMonthList.class);
                    Uri uri = Uri.parse(MonthEntry.CONTENT_URI + "/" + id );
                    intent.putExtra(MonthEntry.CONTENT_ITEM_TYPE, uri);
                    intent.putExtra(FragmentDetailMonthList.EXTRA_MONTH_ID,id);

                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(BUDGET_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Budget History");
    }
}
