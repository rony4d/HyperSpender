package com.example.ugochukwu.hyperspender;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ugochukwu.hyperspender.data.BudgetContract;


public class ActivityDetailMonthList extends ActionBarActivity implements FragmentDetailMonthList.Callback{
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_month_list);
        if(findViewById(R.id.detail_amount_entries_container )!= null) {
            mTwoPane = true;


            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_amount_entries_container, new FragmentDetailAmountEntries()).commit();
            }
        }
        else{
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onItemSelected(Uri uri, long month_id) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(BudgetContract.AmountEntry.CONTENT_ITEM_TYPE, uri);
            args.putLong(FragmentDetailMonthList.EXTRA_MONTH_ID, month_id);


            FragmentDetailAmountEntries fragment = new FragmentDetailAmountEntries();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_amount_entries_container,fragment).commit();
        }
        else{
            String amount_id = uri.getLastPathSegment();
            Log.i("Rony","Amount Id" + amount_id);
            Intent intent = new Intent(this,ActivityDetailAmountEntries.class);
            Uri new_uri = Uri.parse(BudgetContract.AmountEntry.CONTENT_URI + "/" + amount_id );
            intent.putExtra(BudgetContract.AmountEntry.CONTENT_ITEM_TYPE, new_uri);
            intent.putExtra(FragmentDetailMonthList.EXTRA_MONTH_ID,month_id);
           // intent.putExtra(FragmentDetailAmountEntries.EXTRA_AMOUNT_ID,amount_id);
            startActivity(intent);
        }
    }
}
