package com.example.english_app.Layouts;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.english_app.Fragments.LearnFragment;
import com.example.english_app.Fragments.VocabularyFragment;
import com.example.english_app.Fragments.SearchFragment;
import com.example.english_app.R;
import com.example.english_app.Utils.DatabaseUtils.DBAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main extends AppCompatActivity implements BadgeNotifyInterface {

    BottomNavigationView bottomNav;
    BadgeDrawable badge;
    FrameLayout fragmentContainer;
    private int vocabularyWordsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //hide the title bar

        bottomNav = findViewById(R.id.bottom_nav_menu);
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        badge = bottomNav.getOrCreateBadge(R.id.nav_manage);

        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
        try {
            dbAdapter.open();
            vocabularyWordsCount = (int) dbAdapter.getCount();
            dbAdapter.close();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Error with getting words count from database", e);
        }

        badge.setNumber(vocabularyWordsCount);
        badge.setVisible(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
        }
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_manage:
                    selectedFragment = new VocabularyFragment();
                    break;
                case R.id.nav_learn:
                    selectedFragment = new LearnFragment();
                    break;
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void notifyBadge(int amount) { // 0 argument sets count of records to zero
        if (amount == 0) {
            vocabularyWordsCount = 0;
            badge.setNumber(vocabularyWordsCount);
        } else {
            badge.setNumber(vocabularyWordsCount += amount);
        }
    }
}
