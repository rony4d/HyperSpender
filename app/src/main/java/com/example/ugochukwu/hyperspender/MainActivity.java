package com.example.ugochukwu.hyperspender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ugochukwu.hyperspender.data.BudgetContract;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements FragmentDetailMonthList.Callback{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    boolean mTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           if (findViewById(R.id.detail_amount_entries_container) != null) {
               mTwoPane = true;


               if (savedInstanceState == null) {
                   getSupportFragmentManager().beginTransaction().replace(R.id.detail_amount_entries_container, new FragmentDetailAmountEntries()).commit();
               }
           } else {
               mTwoPane = false;
           }

           mTitle = mDrawerTitle = getTitle();

           // load slide menu items
           navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

           // nav drawer icons from resources
           navMenuIcons = getResources()
                   .obtainTypedArray(R.array.nav_drawer_icons);

           mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
           mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

           navDrawerItems = new ArrayList<NavDrawerItem>();

           // adding nav drawer items to array
           // Home
           navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
           // Find People
           navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
           // Photos
           navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
           // Communities, Will add a counter here
           navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
           // Communities, Will add a counter here
           navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));


           // Recycle the typed array
           navMenuIcons.recycle();

           mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

           // setting the nav drawer list adapter
           adapter = new NavDrawerListAdapter(getApplicationContext(),
                   navDrawerItems);
           mDrawerList.setAdapter(adapter);

           // enabling action bar app icon and behaving it as toggle button
           //  getSupportActionBar().setDisplayShowCustomEnabled(true);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setHomeAsUpIndicator(R.mipmap.navigation_drawer_toggle_icon);
           getSupportActionBar().setHomeButtonEnabled(true);
           //  getSupportActionBar().setDisplayShowTitleEnabled(false);
           //  getSupportActionBar().setIcon( R.mipmap.navigation_drawer_toggle_icon);


           mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                   R.mipmap.navigation_drawer_toggle_icon, //nav menu toggle icon
                   R.string.app_name, // nav drawer open - description for accessibility
                   R.string.app_name // nav drawer close - description for accessibility
           ) {
               public void onDrawerClosed(View view) {
                   getSupportActionBar().setTitle(mTitle);
                   // calling onPrepareOptionsMenu() to show action bar icons
                   invalidateOptionsMenu();
               }

               public void onDrawerOpened(View drawerView) {
                   getSupportActionBar().setTitle(mDrawerTitle);
                   // calling onPrepareOptionsMenu() to hide action bar icons
                   invalidateOptionsMenu();
               }
           };
           mDrawerLayout.setDrawerListener(mDrawerToggle);

           if (savedInstanceState == null) {
               // on first time display view for first nav item
               displayView(0);
           }

    }


    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this,ActivitySettings.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
//                fragment = new FragmentDetailMonthList();
               //overridePendingTransition(R.anim.right_in, R.anim.left_out);
               // finish();
                break;
            case 1:
                Intent intent1 = new Intent(this,ActivityCreateBudget.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intentSettings = new Intent(this,ActivitySettings.class);
                startActivity(intentSettings);

                break;
            case 3:
                Intent intentHistory = new Intent(this,ActivityHyperSpenderList.class);
                startActivity(intentHistory);

                break;
            case 4:
                Intent intentAbout = new Intent(this,ActivityAbout.class);
                startActivity(intentAbout);

                break;


            default:
                break;
        }

//        if (fragment != null) {
//            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container,fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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
            Log.i("Rony", "Amount Id" + amount_id);
            Intent intent = new Intent(this,ActivityDetailAmountEntries.class);
            Uri new_uri = Uri.parse(BudgetContract.AmountEntry.CONTENT_URI + "/" + amount_id );
            intent.putExtra(BudgetContract.AmountEntry.CONTENT_ITEM_TYPE, new_uri);
            intent.putExtra(FragmentDetailMonthList.EXTRA_MONTH_ID,month_id);
            // intent.putExtra(FragmentDetailAmountEntries.EXTRA_AMOUNT_ID,amount_id);
            startActivity(intent);
        }
    }
}
