package com.example.ugochukwu.hyperspender;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ugochukwu.hyperspender.data.BudgetContract;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

/**
 * Created by ugochukwu on 5/28/2015.
 */
public class SplashActivity extends Activity{
    ShimmerTextView tv;
    Shimmer shimmer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

            tv = (ShimmerTextView) findViewById(R.id.shimmer_tv);
            shimmer = new Shimmer();
            shimmer.setDuration(1000);
            shimmer.start(tv);
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(5000);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        Uri uri = Uri.parse(BudgetContract.MonthEntry.CONTENT_URI + "/" + 1);
                        intent.putExtra(BudgetContract.MonthEntry.CONTENT_ITEM_TYPE, uri);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);

                    } catch (Exception e) {
                        Log.e("ex", e.toString());
                    } finally {

                        finish();
                    }
                }

            };
            timer.start();
        }



}
