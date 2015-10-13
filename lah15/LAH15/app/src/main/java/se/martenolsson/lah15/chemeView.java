package se.martenolsson.lah15;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import se.martenolsson.lah15.classes.SlidingTabLayout;
import se.martenolsson.lah15.classes.TinyDB;

/**
 * Created by maroln on 15-08-19.
 */
public class chemeView extends ActionBarActivity {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Ons","Tors","Fre","Lör"};
    int Numboftabs =4;
    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        tinydb = new TinyDB(this);

        final ImageButton removenofollow =(ImageButton)findViewById(R.id.removenofollow);
        final ImageButton shownofollow =(ImageButton)findViewById(R.id.shownofollow);

        if(tinydb.getBoolean("removefollow") == true){
            shownofollow.setVisibility(View.VISIBLE);
            removenofollow.setVisibility(View.GONE);
        }

        removenofollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Tab1.webview != null) {
                    Tab1.webview.loadUrl("javascript:removeNoFollow();");
                }
                if(Tab2.webview != null) {
                    Tab2.webview.loadUrl("javascript:removeNoFollow();");
                }
                if(Tab3.webview != null) {
                    Tab3.webview.loadUrl("javascript:removeNoFollow();");
                }
                if(Tab4.webview != null) {
                    Tab4.webview.loadUrl("javascript:removeNoFollow();");
                }
                tinydb.putBoolean("removefollow",true);

                shownofollow.setVisibility(View.VISIBLE);
                removenofollow.setVisibility(View.GONE);
            }
        });
        shownofollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tab1.webview != null) {
                    Tab1.webview.loadUrl("javascript:showNoFollow();");
                }
                if (Tab2.webview != null) {
                    Tab2.webview.loadUrl("javascript:showNoFollow();");
                }
                if (Tab3.webview != null) {
                    Tab3.webview.loadUrl("javascript:showNoFollow();");
                }
                if (Tab4.webview != null) {
                    Tab4.webview.loadUrl("javascript:showNoFollow();");
                }
                tinydb.putBoolean("removefollow", false);
                shownofollow.setVisibility(View.GONE);
                removenofollow.setVisibility(View.VISIBLE);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Spelschema");


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.primary_dark_material_dark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Top Icon
            case android.R.id.home:
                this.finish();
                break;

        }
        return false;
    }
}
