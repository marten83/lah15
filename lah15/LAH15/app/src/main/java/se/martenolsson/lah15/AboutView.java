package se.martenolsson.lah15;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by maroln on 15-08-21.
 */
public class AboutView extends SwipeBackActivity {
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Typeface geoSans = ((ApplicationController) getApplicationContext()).geoSans;
        TextView headerText = (TextView) findViewById(R.id.headerText);
        headerText.setTypeface(geoSans);

        TextView closeView = (TextView) findViewById(R.id.closeView);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
