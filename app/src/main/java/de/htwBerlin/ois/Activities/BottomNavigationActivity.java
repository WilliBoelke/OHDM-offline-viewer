package de.htwBerlin.ois.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.htwBerlin.ois.R;

public class BottomNavigationActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        ButterKnife.bind(this);

        Menu menu = bottom_navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_about:
                        Intent aboutIntent = new Intent(BottomNavigationActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    case R.id.nav_navigation:
                        Intent navigationIntent = new Intent(BottomNavigationActivity.this, NavigationActivity.class);
                        startActivity(navigationIntent);
                        break;
                    case R.id.nav_download:
                        Intent downloadIntent = new Intent(BottomNavigationActivity.this, MapDownloadActivity.class);
                        startActivity(downloadIntent);
                        break;
                }
                return false;
            }
        });
    }
}
