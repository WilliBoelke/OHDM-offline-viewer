package de.htwBerlin.ois.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.htwBerlin.ois.R;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    @BindView(R.id.bottom_navigation) BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        bottom_navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        Menu menu = bottom_navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_about:
                        break;
                    case R.id.nav_navigation:
                        Intent navigationIntent = new Intent(AboutActivity.this, NavigationActivity.class);
                        startActivity(navigationIntent);
                        break;
                    case R.id.nav_download:
                        Intent downloadIntent = new Intent(AboutActivity.this, MapDownloadActivity.class);
                        startActivity(downloadIntent);
                        break;
                    case R.id.nav_home:
                        Intent startIntent = new Intent(AboutActivity.this, HomeActivity.class);
                        startActivity(startIntent);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
