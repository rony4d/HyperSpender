package com.example.ugochukwu.hyperspender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityIntro extends ActionBarActivity {


    SharedPreferences prefs;
    String prefName = "MyPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //---get the SharedPreferences object---
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        boolean check = prefs.getBoolean("authenticated",true);
        Log.i("Rony","check" + check);

        if(check){
            Intent intent = new Intent(this,DefaultIntro.class);
            startActivity(intent);
            editor.putBoolean("authenticated", false);
            editor.commit();

        }
        else{
            Intent intent = new Intent(this,ActivityLogin.class);
            startActivity(intent);
        }

        finish();



    }


}
