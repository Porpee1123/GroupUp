package com.example.groupup;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class inviteFriends extends AppCompatActivity {
    Button bu_slide;
    View table_row;
    boolean isUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_invite_friends);
        bu_slide = findViewById(R.id.slide);
        table_row = findViewById(R.id.invite_table_friend);
        //set up
        bu_slide.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
        table_row.setVisibility(View.INVISIBLE);
        isUp = true;
    }

    // slide the view from below itself to the current position
    public void slideUp(View view) {
        Log.d("slide", "Hello up");
        bu_slide.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
        table_row.setVisibility(View.GONE);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        Log.d("slide", "Hello down");
        bu_slide.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_black_24dp, 0);
        table_row.setVisibility(View.VISIBLE);
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown(table_row);
            bu_slide.setText("Slide up");
        } else {
            slideUp(table_row);
            bu_slide.setText("Slide down");

        }
        isUp = !isUp;
    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new invite_head(), "invite_head");
//        adapter.addFragment(new invite_attendant(), "invite_Attendant");
//        viewPager.setAdapter(adapter);
//    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}