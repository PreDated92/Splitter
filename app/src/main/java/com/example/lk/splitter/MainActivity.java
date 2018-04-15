package com.example.lk.splitter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Create Navigation drawer and inflate layout
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Adding menu icon to Toolbar
//        ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar != null) {
//            VectorDrawableCompat indicator = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
//            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
//            supportActionBar.setHomeAsUpIndicator(indicator);
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//        }

        // Set behavior of Navigation drawer
//        navigationView.setNavigationItemSelectedListener(
//            new NavigationView.OnNavigationItemSelectedListener() {
//                // This method will trigger on item Click of navigation menu
//                @Override
//                public boolean onNavigationItemSelected(MenuItem menuItem) {
//                    // Set item in checked state
//                    menuItem.setChecked(true);
//
//                    // TODO: handle navigation
//
//                    // Closing drawer on item click
//                    mDrawerLayout.closeDrawers();
//                    return true;
//                }
//            });

        // Adding Floating Action Button to bottom right of main view
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(v, "Hello Snackbar!",
//                        Snackbar.LENGTH_LONG).show();
//            }
//        });
    }

//    static class Adapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public Adapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Pax", "Tab2", "Tab3" };
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return PaxFragment.newInstance(position + 1);

                case 1:
                    return BillFragment.newInstance(position + 1);

                case 2:
                    return BillFragment.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
