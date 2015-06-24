package com.example.ugochukwu.hyperspender;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.example.ugochukwu.hyperspender.FragmentAddEditAmount;

public class ActivityAddEditAmount extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_amount);

//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00A81D")));

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int madeAccountType = this.getIntent().getIntExtra(FragmentAddEditAmount.EXTRA_MADE_ACCOUNT_TYPE, -1);

        if(madeAccountType==1 ){
            getSupportActionBar().setTitle("Add Amount Made");
        }
        else{
            getSupportActionBar().setTitle("Add Amount Spent");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_add_edit_amount, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this,ActivitySettings.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
